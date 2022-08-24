package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.controller.util.ControllerUtil;
import com.myproject.expo.expositions.controller.util.ThemeUtilController;
import com.myproject.expo.expositions.dto.ThemeDto;
import com.myproject.expo.expositions.exception.custom.ThemeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.myproject.expo.expositions.util.Constant.*;

/**
 * The ThemeController class do CRUD operations with Theme Dto
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
@Slf4j
public class ThemeController implements ControllerUtil {
    private final ThemeUtilController themeUtilController;

    @Autowired
    public ThemeController(ThemeUtilController themeUtilController) {
        this.themeUtilController = themeUtilController;
    }

    @GetMapping("/themes")
    public String getThemes(Model model, @PageableDefault(sort = {ID_THEME},page = 1,direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute(THEME_OBJ, new ThemeDto());
        model.addAttribute(NUMBER_OF_PAGES, themeUtilController.countNoRequiredPages(pageable));
        log.info("PAGE {} number pages {} ",pageable,themeUtilController.countNoRequiredPages(pageable));
        model.addAttribute(PAGE, pageable);

        try {
            model.addAttribute(THEMES, themeUtilController.getAllThemes(pageable));
        } catch (Exception e) {
            log.warn("Cannot get all themes for admin");
            model.addAttribute(ERR_MSG,e.getMessage());
            return URL.ADMIN_HOME_SLASH;
        }
        return URL.ADMIN_HOME_SLASH;
    }

    @PostMapping("/themes")
    public String saveTheme(@ModelAttribute(THEME_OBJ) @Valid ThemeDto themeDto, BindingResult bindingResult,
                            Model model, @PageableDefault(sort = {ID_THEME}, page = 0,
                                    direction = Sort.Direction.DESC) Pageable pageable) {
        setDataToTheModelAfterCrud(themeDto, model, pageable);
        if (inputHasErrors(bindingResult)) {
            log.warn("theme name was input incorrect");
            return URL.ADMIN_HOME_SLASH;
        }
        try{
            themeUtilController.saveTheTheme(themeDto);
        }catch (ThemeException e){
            log.warn("cannot save the theme with name {}",themeDto.getName());
            model.addAttribute(ERR_MSG,e.getMessage());
            return URL.ADMIN_HOME_SLASH;
        }
        return URL.REDIRECT_ADMIN_HOME;
    }

    private void setDataToTheModelAfterCrud(ThemeDto themeDto, Model model, Pageable pageable) {
        model.addAttribute(PAGE, pageable);
        model.addAttribute(THEME_OBJ, themeDto);
        model.addAttribute(THEMES,themeUtilController.getAllThemes(pageable));
    }

    @PostMapping("/themes/cancel")
    public String cancelAddTHeme(@RequestParam(value = "cancel", required = false) String cancel) {
        return cancel != null ? REDIRECT + URL.ADMIN_HOME_SLASH : URL.ADMIN_HOME_SLASH;
    }

    @PatchMapping("/themes/{id}")
    public String updateTheme(@PathVariable(ID) Long id,
                              @ModelAttribute(THEME_OBJ) @Valid ThemeDto themeDto,
                              BindingResult bindingResult, Model model,
                              @PageableDefault(sort = {ID_THEME},
                                      direction = Sort.Direction.DESC) Pageable pageable) {

        if (inputHasErrors(bindingResult)) {
            return URL.ADMIN_HOME_SLASH;
        }
        setDataToTheModelAfterCrud(themeDto, model, pageable);
        try{
            themeUtilController.updateTheTheme(id, themeDto);
        }catch (ThemeException e){
            log.warn("CONTROLLER FAILED UPDATE THEME");
            model.addAttribute(ERR_MSG,e.getMessage());
            return URL.ADMIN_HOME_SLASH;
        }
        return URL.REDIRECT_ADMIN_HOME;
    }

    @DeleteMapping("/themes/{id}")
    public String delete(@PathVariable(ID) Long id, Model model,
                         @PageableDefault(sort = {ID_THEME},
                                 direction = Sort.Direction.DESC) Pageable pageable) {
        setDataToTheModelAfterCrud(new ThemeDto(),model,pageable);
        try{
            themeUtilController.deleteTheme(id);
        }catch (ThemeException e){
            log.warn("Cannot delete the theme with id {}",id);
            model.addAttribute(ERR_MSG,e.getMessage());
            return URL.ADMIN_HOME_SLASH;
        }
        return URL.REDIRECT_ADMIN_HOME;
    }

}

