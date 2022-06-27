package com.myproject.expo.expositions.service.facade;

import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.exception.custom.InValidException;
import com.myproject.expo.expositions.service.ExpoService;
import com.myproject.expo.expositions.service.HallService;
import com.myproject.expo.expositions.service.ThemeService;
import com.myproject.expo.expositions.validator.Validate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import javax.xml.bind.ValidationException;

@Component
@Slf4j
public class ExpoServiceFacade {
    private final ExpoService expoService;
    private final HallService hallService;
    private final ThemeService themeService;
    private final Validate validate;
    private final Build<ExpoDto, Exposition> build;


    @Autowired
    public ExpoServiceFacade(ExpoService expoService, HallService hallService, ThemeService themeService,
                             Validate validate,@Qualifier("expoBuild") Build<ExpoDto, Exposition> build) {
        this.expoService = expoService;
        this.hallService = hallService;
        this.themeService = themeService;
        this.validate = validate;
        this.build = build;
    }


    public String update(@PathVariable("id") Long id, @ModelAttribute("expo") ExpoDto expoDto, BindingResult bindingResult,
                         Model model) {
        //TODO REWRITE FIX ME
        Pageable pageable = PageRequest.of(0,5);
        ExpoDto foundExpoFromDb = expoService.getById(expoDto.getId());
        log.info("income expodto {}", expoDto);

        try {
            Exposition exposition = build.toModel(expoDto);
            if (!returnBackThemeOrHallNotValid(expoDto, model, pageable).isEmpty()) {
                return returnBackThemeOrHallNotValid(expoDto, model, pageable);
            }
            if (validateDateTime(id, model, pageable, exposition)) {
                //return setErrMsgAndPathBack(model, "err.date_time_input", ADMIN_SHOW_URL);
            }
            expoService.update(id, exposition);
        } catch (Exception e) {
            log.warn("Cannot update the expo with id {}", expoDto.getId());
            //return setErrMsgAndPathBack(model,
           //         setExpoToTheModel(foundExpoFromDb, model, "err.expo_update"), ADMIN_SHOW_URL);
        }
        return "redirect:/admin/expos";
    }

    private String returnBackThemeOrHallNotValid(ExpoDto expo, Model model, Pageable pageable) {
        if (!validate.validateThemeHasIdFromInput(expo)) {
//            return setErrMsgAndPathBack(model,
//                    setExpoToTheModel(expo, model, "err.theme_input_expo_update"), ADMIN_SHOW_URL);
            throw new InValidException("err.theme_input_expo_update");
        }
        if (validate.validateHallNotEmpty(expo)) {
            log.info("Hall empty");
//            return setErrMsgAndPathBack(model,
//                    setExpoToTheModel(expo, model, "err.hall_input_expo_update"), ADMIN_SHOW_URL);
            throw new InValidException("err.hall_input_expo_update");
        }
        return "";
    }

    private boolean validateDateTime(Long id, Model model, Pageable pageable, Exposition exposition) {
        if ((validate.isDateValid(exposition.getExpoDate()) && validate.isTimeInReqDiapason(exposition.getExpoTime()))) {
            expoService.update(id, exposition);
        } else {
           // setAllExposToTheModel(model, pageable, "");
            return true;
        }
        return false;
    }
}
