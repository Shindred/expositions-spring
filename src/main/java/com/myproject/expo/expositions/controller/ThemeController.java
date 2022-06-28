package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.controller.util.ControllerUtils;
import com.myproject.expo.expositions.controller.util.ThemeUtilController;
import com.myproject.expo.expositions.dto.ThemeDto;
import com.myproject.expo.expositions.exception.custom.ThemeException;
import com.myproject.expo.expositions.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
@Slf4j
public class ThemeController implements ControllerUtils {
    private static final String PATH_BACK = "/admin/themes";
    private final ThemeUtilController themeUtilController;

    @Autowired
    public ThemeController(ThemeUtilController themeUtilController) {
        this.themeUtilController = themeUtilController;
    }

    @GetMapping("/themes")
    public String getThemes(Model model, @PageableDefault(sort = {"idTheme"},page = 1,direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("themeObj", new ThemeDto());
        model.addAttribute("numberOfPages", themeUtilController.countNoRequiredPages(pageable));
        log.info("PAGE {} number pages {} ",pageable,themeUtilController.countNoRequiredPages(pageable));
        model.addAttribute("page", pageable);

        try {
            model.addAttribute("themes", themeUtilController.getAllThemes(pageable));
        } catch (Exception e) {
            model.addAttribute("err.Msg",e.getMessage());
            return "/admin/home";
        }
        return "admin/home";
    }

    @PostMapping("/themes")
    public String saveTheme(@ModelAttribute("themeObj") @Valid ThemeDto themeDto, BindingResult bindingResult,
                            Model model, @PageableDefault(sort = {"idTheme"}, page = 0,
                                    direction = Sort.Direction.DESC) Pageable pageable) {
        setDataToTheModelAfterCrud(themeDto, model, pageable);
        if (inputHasErrors(bindingResult)) {
            log.info("theme name was input incorrect");
            return "/admin/home";
        }
        try{
            themeUtilController.saveTheTheme(themeDto);
        }catch (ThemeException e){
            model.addAttribute("errMsg",e.getMessage());
            return "/admin/home";
        }
        return "redirect:/admin/home";
    }

    private void setDataToTheModelAfterCrud(ThemeDto themeDto, Model model, Pageable pageable) {
        model.addAttribute("page", pageable);
        model.addAttribute("themeObj", themeDto);
        model.addAttribute("themes",themeUtilController.getAllThemes(pageable));
    }

    @PostMapping("/themes/cancel")
    public String cancelAddTHeme(@RequestParam(value = "cancel", required = false) String cancel) {
        return cancel != null ? Constant.REDIRECT + "/admin/home" : "/admin/home";
    }

    @PatchMapping("/themes/{id}")
    public String updateTheme(@PathVariable("id") Long id,
                              @ModelAttribute("themeObj") @Valid ThemeDto themeDto,
                              BindingResult bindingResult, Model model,
                              @PageableDefault(sort = {"idTheme"},
                                      direction = Sort.Direction.DESC) Pageable pageable) {

        if (inputHasErrors(bindingResult)) {
            return "/admin/home";
        }
        setDataToTheModelAfterCrud(themeDto, model, pageable);
        try{
            themeUtilController.updateTheTheme(id, themeDto);
        }catch (ThemeException e){
            log.warn("CONTROLLER FAILED UPDATE THEME");
            model.addAttribute("errMsg",e.getMessage());
            return "/admin/home";
        }
        return "redirect:/admin/home";
    }

    private void setDataToTheModel(ThemeDto themeDto, Model model, Pageable pageable) {
        model.addAttribute("page", pageable);
        model.addAttribute("themeObj", themeDto);
        model.addAttribute("themes",themeUtilController.getAllThemes(pageable));
    }

    @DeleteMapping("/themes/{id}")
    public String delete(@PathVariable("id") Long id, Model model,
                         @PageableDefault(sort = {"idTheme"},
                                 direction = Sort.Direction.DESC) Pageable pageable) {
        setDataToTheModelAfterCrud(new ThemeDto(),model,pageable);
        try{
            themeUtilController.deleteTheme(id);
        }catch (ThemeException e){
            model.addAttribute("errMsg",e.getMessage());
            return "/admin/home";
        }
        return "redirect:/admin/home";
    }

}

