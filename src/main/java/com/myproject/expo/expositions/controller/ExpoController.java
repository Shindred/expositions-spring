package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.controller.util.ControllerUtils;
import com.myproject.expo.expositions.controller.util.ExpoUtilController;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.Hall;
import com.myproject.expo.expositions.entity.Theme;
import com.myproject.expo.expositions.exception.custom.ExpoException;
import com.myproject.expo.expositions.service.ExpoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static com.myproject.expo.expositions.util.Constant.*;

/**
 * The ExpoController class do CRUD operations with exposition
 */
@Controller
public class ExpoController implements ControllerUtils {
    private static final Logger log = LogManager.getLogger(ExpoController.class);
    private final ExpoUtilController expoUtilController;
    private final ExpoService expoService;

    @Autowired
    public ExpoController(ExpoUtilController expoUtilController,
                          ExpoService expoService) {
        this.expoUtilController = expoUtilController;
        this.expoService = expoService;
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
    public String addExpo(@ModelAttribute("expo") @Valid ExpoDto expo,
                          BindingResult bindingResult, Model model) {
        String url = "/admin/addExpo";
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
    public String show(@PathVariable("id") Long id, Model model) {
        expoUtilController.show(id, model);
        return URL.ADMIN_HOME_SLASH;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/update/{id}")
    public String getUpdatePage(@PathVariable("id") Long id, Model model) {
        ExpoDto expoDto = expoService.getById(id);
        log.info("Show exposition " + expoDto);
        model.addAttribute(HALLS_SHOW, expoUtilController.getAllHalls());
        model.addAttribute(THEMES_SHOW, expoUtilController.getAllThemes());
        model.addAttribute(EXPO, expoDto);
        model.addAttribute(FORM, new ExpoDto());
        setDateTimeFormatterToModel(model);
        return URL.ADMIN_UPDATE;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin/update/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("expo") @Valid ExpoDto expoDto,
                         BindingResult bindingResult, Model model) {
        List<Hall> halls = expoUtilController.getAllHalls();
        List<Theme> themes = expoUtilController.getAllThemes();
        model.addAttribute(HALLS_SHOW, halls);
        model.addAttribute(THEMES_SHOW, themes);
        if (inputHasErrors(bindingResult)) {
            log.info("Updating the exposition {} was failed",expoDto.getId());
            return URL.ADMIN_UPDATE;
        }
        model.addAttribute(HALLS, halls);
        model.addAttribute(THEMES, themes);
        return expoUtilController.update(id, expoDto, model);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/admin/show/{id}")
    public String changeExpoStatus(@PathVariable("id") Long id,
                                   @RequestParam("status") String status) {
        log.info("change status id {}  status {}", id, status);
        Integer statusId = defineStatusId(status);
        expoService.changeStatus(id, statusId);
        return URL.REDIRECT_ADMIN_HOME;

    }
}
