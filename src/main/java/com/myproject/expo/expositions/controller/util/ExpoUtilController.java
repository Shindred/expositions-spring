package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.entity.Hall;
import com.myproject.expo.expositions.entity.Statistic;
import com.myproject.expo.expositions.entity.Theme;
import com.myproject.expo.expositions.exception.custom.ExpoException;
import com.myproject.expo.expositions.service.ExpoService;
import com.myproject.expo.expositions.service.HallService;
import com.myproject.expo.expositions.service.ThemeService;
import com.myproject.expo.expositions.validator.Validate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class ExpoUtilController implements ControllerUtils {
    private final static String ADMIN_SHOW_URL = "/admin/show";
    private final static String REDIRECT_ADMIN_EXPOS = "redirect:/admin/expos";
    private final ExpoService expoService;
    private final HallService hallService;
    private final ThemeService themeService;
    private final Validate validate;
    private final Build<ExpoDto, Exposition> build;

    @Autowired
    public ExpoUtilController(ExpoService expoService, HallService hallService,
                              ThemeService themeService, Validate validate,
                              @Qualifier("expoBuild") Build<ExpoDto, Exposition> build) {
        this.expoService = expoService;
        this.hallService = hallService;
        this.themeService = themeService;
        this.validate = validate;
        this.build = build;
    }

    public String getPageToAddExpo(Model model) {
        List<Hall> halls = hallService.getAll();
        List<Theme> themes = themeService.getAll();
        addExpoSetRequiredDataToTheModel(model, halls, themes);
        log.info("returning to the client from expo util get page to add expo");
        return "/admin/addExpo";
    }

    private void addExpoSetRequiredDataToTheModel(Model model, List<Hall> halls, List<Theme> themes) {
        ExpoDto expoDto = new ExpoDto();
        expoDto.setStatistic(new Statistic());
        model.addAttribute("expo", expoDto);
        model.addAttribute("halls", halls);
        model.addAttribute("addHalls", new ArrayList<Hall>());
        model.addAttribute("themes", themes);
    }

    public String addExpo(@ModelAttribute("expo") ExpoDto expoDto,
                          @RequestParam("hal") @NotNull List<Long> halls, Model model) {
        log.info("Expo income {}", expoDto);
        validate.validateProperDate(expoDto.getExpoDate());
        validate.validateProperTime(expoDto.getExpoTime());
        if (isHallBusyAtTheTimeAndDate(expoService.getAll(), expoDto, halls)) {
            log.info("HALL IS BUSY ALREADY");
            throw new ExpoException("err.already_busy_hall");
        }
        try {
            expoService.addExpo(expoDto, halls);
        } catch (Exception e) {
            log.warn("Cannot add Exposition with name {}", expoDto.getName());
            setErrMsgAndPathBack(model, e.getMessage(), "/admin/addExpo");
        }
        return REDIRECT_ADMIN_EXPOS;
    }

    public String show(@PathVariable("id") Long id, Model model) {
        showOneExpoSetDataToTheModel(id, model, hallService.getAll(), themeService.getAll());
        return ADMIN_SHOW_URL;
    }

    private void showOneExpoSetDataToTheModel(Long id, Model model, List<Hall> halls, List<Theme> themes) {
        ExpoDto expoDto = expoService.getById(id);
        log.info("Show exposition " + expoDto);
        model.addAttribute("hallsShow", halls);
        model.addAttribute("themesShow", themes);
        model.addAttribute("expo", expoDto);
        model.addAttribute("form", new ExpoDto());
    }

    public List<Hall> getAllHalls() {
        return hallService.getAll();
    }

    public List<Theme> getAllThemes() {
        return themeService.getAll();
    }


    public String update(@PathVariable("id") Long id, @ModelAttribute("expo") ExpoDto expoDto, Model model) {
        Pageable pageable = getPageable(0, 5);
        setPageableAndExposToTheModel(model, pageable, expoDto);
        ExpoDto foundExpoFromDb = expoService.getById(expoDto.getId());
        try {
            Exposition exposition = build.toModel(expoDto);
            setAllExposToTheModel(model, pageable, "");
            if (!returnBackThemeOrHallNotValid(expoDto, model, pageable).isEmpty()) {
                return returnBackThemeOrHallNotValid(expoDto, model, pageable);
            }
            if (!validateDateTime(id, exposition)) {
                return setErrMsgAndPathBack(model, "err.date_time_input", "/admin/home");
            }
        } catch (Exception e) {
            log.warn("Cannot update the expo with id {}", expoDto.getId());
            return setErrMsgAndPathBack(model,
                    setExpoToTheModel(foundExpoFromDb, model, "err.expo_update"), "/admin/home");
        }
        return "redirect:/admin/home";
    }

    private String returnBackThemeOrHallNotValid(ExpoDto expo, Model model, Pageable pageable) {
        if (!validate.validateThemeHasIdFromInput(expo)) {
            return setErrMsgAndPathBack(model,
                    setExpoToTheModel(expo, model, "err.theme_input_expo_update"), ADMIN_SHOW_URL);
        }
        if (validate.validateHallNotEmpty(expo)) {
            log.info("Hall empty");
            return setErrMsgAndPathBack(model,
                    setExpoToTheModel(expo, model, "err.hall_input_expo_update"), ADMIN_SHOW_URL);
        }
        return "";
    }

    private boolean validateDateTime(Long id, Exposition exposition) {
        if ((validate.isDateValid(exposition.getExpoDate()) && validate.isTimeInReqDiapason(exposition.getExpoTime()))) {
            log.warn("CHECK DATE AND TIME");
            expoService.update(id, exposition);
            return true;
        }
        return false;
    }

    private String setExpoToTheModel(ExpoDto expoDto, Model model, String errMsg) {
        model.addAttribute("expo", expoDto);
        return errMsg;
    }

    private void setPageableAndExposToTheModel(Model model, Pageable pageable, ExpoDto expoDto) {
        model.addAttribute("page", pageable);
        setAllExposToTheModel(model, pageable, "");
        model.addAttribute("expo", expoDto);
    }

    private String setAllExposToTheModel(Model model, Pageable pageable, String errMsg) {
        model.addAttribute("expos", expoService.getAll(pageable));
        return errMsg;
    }

    private boolean isHallBusyAtTheTimeAndDate(List<Exposition> list, ExpoDto expo, List<Long> halls) {
        return list.stream()
                .filter(e -> e.getExpoTime().compareTo(expo.getExpoTime()) == 0)
                .filter(e -> e.getHalls().stream()
                        .anyMatch(elem -> halls.stream()
                                .anyMatch(val -> Objects.equals(val, elem.getIdHall()))))
                .anyMatch(exposition -> expo.getExpoDate().compareTo(exposition.getExpoDate()) == 0);
    }
}
