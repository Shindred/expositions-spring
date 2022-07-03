package com.myproject.expo.expositions.service.facade;

import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.exception.custom.InValidException;
import com.myproject.expo.expositions.service.ExpoService;
import com.myproject.expo.expositions.validator.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Component
public class ExpoServiceFacade {
    private static final Logger log = LogManager.getLogger(ExpoServiceFacade.class);
    private final ExpoService expoService;
    private final Validate validate;
    private final Build<ExpoDto, Exposition> build;


    @Autowired
    public ExpoServiceFacade(ExpoService expoService, Validate validate, @Qualifier("expoBuild") Build<ExpoDto, Exposition> build) {
        this.expoService = expoService;
        this.validate = validate;
        this.build = build;
    }

    public String update(@PathVariable("id") Long id, @ModelAttribute("expo") ExpoDto expoDto) {
        Pageable pageable = PageRequest.of(0, 5);
        ExpoDto foundExpoFromDb = expoService.getById(expoDto.getId());
        log.info("income expodto {}", expoDto);

        try {
            Exposition exposition = build.toModel(expoDto);
            if (!returnBackThemeOrHallNotValid(expoDto).isEmpty()) {
                return returnBackThemeOrHallNotValid(expoDto);
            }
            if (validateDateTime(id, exposition)) {
            }
            expoService.update(id, exposition);
        } catch (Exception e) {
            log.warn("Cannot update the expo with id {}", expoDto.getId());
        }
        return "redirect:/admin/expos";
    }

    private String returnBackThemeOrHallNotValid(ExpoDto expo) {
        if (!validate.validateThemeHasIdFromInput(expo)) {
            throw new InValidException("err.theme_input_expo_update");
        }
        if (validate.validateHallNotEmpty(expo)) {
            log.info("Hall empty");
            throw new InValidException("err.hall_input_expo_update");
        }
        return "";
    }

    private boolean validateDateTime(Long id, Exposition exposition) {
        if ((validate.isDateValid(exposition.getExpoDate()) && validate.isTimeInReqDiapason(exposition.getExpoTime()))) {
            expoService.update(id, exposition);
        } else {
            return true;
        }
        return false;
    }
}
