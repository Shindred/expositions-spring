package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.controller.util.ControllerHelper;
import com.myproject.expo.expositions.controller.util.ExpoControllerUtil;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.Hall;
import com.myproject.expo.expositions.entity.Theme;
import com.myproject.expo.expositions.exception.custom.ExpoException;
import com.myproject.expo.expositions.service.ExpoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.myproject.expo.expositions.util.Constant.*;

/**
 * The ExpoController class do CRUD operations with exposition
 */
@Controller
public class ExpoController {
    private static final Logger log = LogManager.getLogger(ExpoController.class);
    private final ExpoControllerUtil expoUtilController;
    private final ExpoService expoService;
    private final ControllerHelper controllerHelper;

    public ExpoController(ExpoControllerUtil expoUtilController,
                          ExpoService expoService,
                          ControllerHelper controllerHelper) {
        this.expoUtilController = expoUtilController;
        this.expoService = expoService;
        this.controllerHelper = controllerHelper;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/addExpo")
    public String addExposition(Model model) {
        log.info("add Exposition method works");
        model.addAttribute(EXPO, new ExpoDto());
        return expoUtilController.getPageToAddExpo(model);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin/addExpo")
    public String addExpo(@ModelAttribute("expo") @Valid ExpoDto expo, BindingResult bindingResult, Model model) {
        String url;
        try {
            url = expoUtilController.addExpo(expo, bindingResult, model);
        } catch (ExpoException e) {
            log.info("cannot add the expo in controller");
            model.addAttribute(ERR_MSG, e.getMessage());
            return URL.ADMIN_ADD_EXPO;
        }
        return url;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping(value = {"*/expos/{id}", "*/search/{id}"})
    public String show(@PathVariable("id") Long id, Model model, HttpServletRequest req) {
        return expoUtilController.show(id, model, req);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/update/{id}")
    public String getUpdatePage(@PathVariable("id") Long id, Model model) {
        ExpoDto expoDto = expoService.getById(id);
        log.debug("Show exposition {} ", expoDto);
        setUpDataToModelForUpdateExpoPage(model, expoDto);
        return URL.ADMIN_UPDATE;
    }

    private void setUpDataToModelForUpdateExpoPage(Model model, ExpoDto expoDto) {
        model.addAttribute(HALLS_SHOW, expoUtilController.getAllHalls());
        model.addAttribute(THEMES_SHOW, expoUtilController.getAllThemes());
        model.addAttribute(EXPO, expoDto);
        model.addAttribute(FORM, new ExpoDto());
        controllerHelper.setDateTimeFormatterToModel(model);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin/update/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("expo") @Valid ExpoDto expoDto,
                         BindingResult bindingResult, Model model) {
        log.info("Updating expo {}", expoDto);
        List<Hall> halls = expoUtilController.getAllHalls();
        List<Theme> themes = expoUtilController.getAllThemes();
        setRequiredDataToModel(expoDto, model, halls, themes);
        if (controllerHelper.inputHasErrors(bindingResult)) {
            log.info("Updating the exposition {} was failed", expoDto.getId());
            model.addAttribute("expo", expoDto);
            return URL.ADMIN_UPDATE;
        }
        model.addAttribute(HALLS, halls);
        model.addAttribute(THEMES, themes);
        return expoUtilController.update(id, expoDto, model);

    }

    private void setRequiredDataToModel(ExpoDto expoDto, Model model, List<Hall> halls, List<Theme> themes) {
        model.addAttribute(HALLS_SHOW, halls);
        model.addAttribute(THEMES_SHOW, themes);
        model.addAttribute("expoDate", expoDto.getExpoDate());
        model.addAttribute("expoTime", expoDto.getExpoTime());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/admin/show/{id}")
    public String changeExpoStatus(@PathVariable("id") Long id,
                                   @RequestParam("status") String status) {
        log.info("change status id {}  status {}", id, status);
        Integer statusId = controllerHelper.defineStatusId(status);
        expoService.changeStatus(id, statusId);
        return URL.REDIRECT_ADMIN_HOME;

    }
}
