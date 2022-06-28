package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.controller.util.ControllerUtils;
import com.myproject.expo.expositions.controller.util.ExpoUtilController;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.exception.custom.ExpoException;
import com.myproject.expo.expositions.service.ExpoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
@Slf4j
public class ExpoController implements ControllerUtils {
    private static final String ADMIN = "/admin/";
    private final static String ADMIN_SHOW_URL = "/admin/show";
    private final static String REDIRECT_ADMIN_EXPOS = "redirect:/admin/expos";
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
    public String getPage(Model model) {
        log.info("add Exposition method works");
        return expoUtilController.getPageToAddExpo(model);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(ADMIN + "/addExpo")
    public String addExpo(@ModelAttribute("expo") ExpoDto expoDto,
                          @RequestParam("hal") @NotNull List<Long> halls, Model model, HttpSession session) {
        try{
            expoUtilController.addExpo(expoDto, halls,model);
        }catch (ExpoException e){
            log.info("cannot add the expo in controller");
            session.setAttribute("infMsg",e.getMessage());
            return "redirect:/admin/home";
        }
        return "redirect:/admin/home";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping(value = {"*/expos/{id}","*/search/{id}"})
    public String show(@PathVariable("id") Long id, Model model) {
       expoUtilController.show(id, model);
       return "/admin/home";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin/update/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("expo") @Valid ExpoDto expoDto,
                         BindingResult bindingResult, Model model) {
        log.warn("Updating exposition");
        if (inputHasErrors(bindingResult)) {
            return "/admin/home";
        }
        model.addAttribute("halls", expoUtilController.getAllHalls());
        model.addAttribute("themes", expoUtilController.getAllThemes());
        return expoUtilController.update(id,expoDto,model);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/admin/show/{id}")
    public String changeExpoStatus(@PathVariable("id") Long id,
                                   @RequestParam("status") String status) {
        log.info("change status id {}  status {}", id,status);
        Integer statusId = defineStatusId(status);
        expoService.changeStatus(id,statusId);
        return "redirect:/admin/home";

    }
}
