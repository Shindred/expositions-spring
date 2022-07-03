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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.myproject.expo.expositions.util.Constant.*;

/**
 * The ExpoUtilController class validate the input data and redirects the request to the service or back to the user
 */
@Component
public class ExpoUtilController implements ControllerUtils {
    private static final Logger log = LogManager.getLogger(ExpoUtilController.class);
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
        return URL.ADMIN_ADD_EXPO;
    }

    private void addExpoSetRequiredDataToTheModel(Model model, List<Hall> halls, List<Theme> themes) {
        ExpoDto expoDto = new ExpoDto();
        expoDto.setStatistic(new Statistic());
        model.addAttribute(EXPO, expoDto);
        model.addAttribute(HALLS, halls);
        model.addAttribute(ADD_HALLS, new ArrayList<Hall>());
        model.addAttribute(THEMES, themes);
    }

    public String addExpo(@ModelAttribute("expo") @Valid ExpoDto expoDto, BindingResult bindingResult, Model model) {
        model.addAttribute(HALLS, hallService.getAll());
        model.addAttribute(THEMES, themeService.getAll());
        if (inputHasErrors(bindingResult)) {
            return URL.ADMIN_ADD_EXPO;
        }
        Exposition exposition = parseDateTimeToLocalDateTimeExpo(expoDto);
        if (caseThemeHallNotValid(expoDto, model))
            return returnBackThemeOrHallNotValid(expoDto, model, URL.ADMIN_ADD_EXPO);
        if (dateTimeValidation(exposition))
            return setErrMsgAndPathBack(model, "err.date_time_input", URL.ADMIN_ADD_EXPO);
        caseCheckHallNotBusyForDate(expoDto);
        try {
            expoService.addExpo(expoDto, new ArrayList<>());
        } catch (Exception e) {
            log.warn("Cannot add Exposition with name {}", expoDto.getName());
            setErrMsgAndPathBack(model, e.getMessage(), URL.ADMIN_ADD_EXPO);
        }
        return URL.REDIRECT_ADMIN_EXPOS;
    }

    private boolean caseThemeHallNotValid(@ModelAttribute("expo") @Valid ExpoDto expoDto, Model model) {
        return !returnBackThemeOrHallNotValid(expoDto, model, URL.ADMIN_ADD_EXPO).isEmpty();
    }

    private boolean dateTimeValidation(Exposition exposition) {
        return !validateDateTime(exposition);
    }

    private void caseCheckHallNotBusyForDate(ExpoDto expoDto) {
        if (isHallBusyAtTheTimeAndDate(expoService.getAll(), expoDto)) {
            log.info("HALL IS BUSY ALREADY");
            throw new ExpoException("err.already_busy_hall");
        }
    }

    public String show(Long id, Model model) {
        showOneExpoSetDataToTheModel(id, model, hallService.getAll(), themeService.getAll());
        setDateTimeFormatterToModel(model);
        return URL.ADMIN_SHOW_URL;
    }


    private void showOneExpoSetDataToTheModel(Long id, Model model, List<Hall> halls, List<Theme> themes) {
        ExpoDto expoDto = expoService.getById(id);
        log.info("Show exposition " + expoDto);
        model.addAttribute(HALLS_SHOW, halls);
        model.addAttribute(THEMES_SHOW, themes);
        model.addAttribute(EXPO, expoDto);
        model.addAttribute(FORM, new ExpoDto());
    }

    public List<Hall> getAllHalls() {
        return hallService.getAll();
    }

    public List<Theme> getAllThemes() {
        return themeService.getAll();
    }


    public String update(Long id, ExpoDto expoDto, Model model) {
        Pageable pageable = getPageable(0, 5);
        setPageableAndExposToTheModel(model, pageable, expoDto);
        ExpoDto foundExpoFromDb = expoService.getById(expoDto.getId());
        try {
            Exposition exposition = parseDateTimeToLocalDateTimeExpo(expoDto);
            setAllExposToTheModel(model, pageable, "");
            if (caseThemeHallInvalidInput(expoDto, model))
                return returnBackThemeOrHallNotValid(expoDto, model, URL.ADMIN_UPDATE_URL);
            if (validateDateTime(id, foundExpoFromDb, exposition))
                return setErrMsgAndPathBack(model, "err.date_time_input", URL.ADMIN_UPDATE_URL);
        } catch (Exception e) {
            log.warn("Cannot update the expo with id {}", expoDto.getId());
            return setErrMsgAndPathBack(model,
                    setExpoToTheModel(foundExpoFromDb, model, "err.expo_update"), URL.ADMIN_UPDATE_URL);
        }
        return URL.REDIRECT_ADMIN_EXPOS;
    }

    private boolean validateDateTime(Long id, ExpoDto foundExpoFromDb, Exposition exposition) {
        if (validateDateTime(exposition, foundExpoFromDb)) {
            expoService.update(id, exposition);
        } else {
            return true;
        }
        return false;
    }

    private boolean caseThemeHallInvalidInput(@ModelAttribute(EXPO) ExpoDto expoDto, Model model) {
        return !returnBackThemeOrHallNotValid(expoDto, model, URL.ADMIN_UPDATE_URL).isEmpty();
    }

    private Exposition parseDateTimeToLocalDateTimeExpo(ExpoDto expoDto) {
        expoDto.setExpoDate(parseStrToLocalDate(expoDto.getExpoDateStr()));
        expoDto.setExpoTime(parseStrToLocalTime(expoDto.getExpoTimeStr()));
        return build.toModel(expoDto);
    }

    private String returnBackThemeOrHallNotValid(ExpoDto expo, Model model, String pathBack) {
        if (!validate.validateThemeHasIdFromInput(expo)) {
            return setErrMsgAndPathBack(model,
                    setExpoToTheModel(expo, model, "err.theme_input_expo_update"), pathBack);
        }
        if (validate.validateHallNotEmpty(expo)) {
            log.info("Hall empty");
            return setErrMsgAndPathBack(model,
                    setExpoToTheModel(expo, model, "err.hall_input_expo_update"), pathBack);
        }
        return "";
    }

    private boolean validateDateTime(Exposition expoFromClient, ExpoDto expoFromDB) {
        if ((validate.isDateValid(expoFromClient.getExpoDate()) && validate.isTimeInReqDiapason(expoFromClient.getExpoTime()))
                || (expoFromClient.getExpoDate().isEqual(expoFromDB.getExpoDate())
                && expoFromClient.getExpoTime().compareTo(expoFromDB.getExpoTime()) == 0)) {
            log.warn("CHECK DATE AND TIME");
            return true;
        }
        return false;
    }

    private boolean validateDateTime(Exposition expoFromClient) {
        if ((validate.isDateValid(expoFromClient.getExpoDate()) && validate.isTimeInReqDiapason(expoFromClient.getExpoTime()))) {
            log.warn("CHECK DATE AND TIME");
            return true;
        }
        return false;
    }

    private String setExpoToTheModel(ExpoDto expoDto, Model model, String errMsg) {
        model.addAttribute(EXPO, expoDto);
        return errMsg;
    }

    private void setPageableAndExposToTheModel(Model model, Pageable pageable, ExpoDto expoDto) {
        model.addAttribute(PAGE, pageable);
        setAllExposToTheModel(model, pageable, "");
        model.addAttribute(EXPO, expoDto);
    }

    private String setAllExposToTheModel(Model model, Pageable pageable, String errMsg) {
        model.addAttribute(EXPOS, expoService.getAll(pageable));
        return errMsg;
    }

    private boolean isHallBusyAtTheTimeAndDate(List<Exposition> list, ExpoDto expo) {
        return list.stream()
                .filter(e -> e.getExpoTime().compareTo(expo.getExpoTime()) == 0)
                .filter(e -> e.getHalls().stream()
                        .anyMatch(elem -> expo.getHalls().stream()
                                .anyMatch(val -> Objects.equals(val.getIdHall(), elem.getIdHall()))))
                .anyMatch(exposition -> expo.getExpoDate().compareTo(exposition.getExpoDate()) == 0);
    }
}
