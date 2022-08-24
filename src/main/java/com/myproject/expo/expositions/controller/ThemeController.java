package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.controller.util.ControllerHelper;
import com.myproject.expo.expositions.controller.util.ThemeControllerUtil;
import com.myproject.expo.expositions.dto.ThemeDto;
import com.myproject.expo.expositions.entity.Theme;
import com.myproject.expo.expositions.exception.custom.ThemeException;
import com.myproject.expo.expositions.service.ThemeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.myproject.expo.expositions.util.Constant.*;

/**
 * The ThemeController class do CRUD operations with Theme Dto
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
@Slf4j
public class ThemeController {
    private final ControllerHelper controllerHelper;
    private final ThemeService themeService;
    private List<Theme> list;

    public ThemeController(ControllerHelper controllerHelper,ThemeService themeService) {
        this.controllerHelper = controllerHelper;
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public String getThemes(Model model, @PageableDefault(sort = {ID_THEME},page = 1,direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute(THEME_OBJ, new ThemeDto());
        model.addAttribute(NUMBER_OF_PAGES, countNoRequiredPages(pageable));
        log.info("The PAGE number is {} and the amount of pages {} ",pageable,countNoRequiredPages(pageable));
        model.addAttribute(PAGE, pageable);

        try {
            model.addAttribute(THEMES, getAllThemes(pageable));
        } catch (Exception e) {
            log.warn("Cannot get all themes for admin");
            model.addAttribute(ERR_MSG,e.getMessage());
            return URL.ADMIN_HOME_SLASH;
        }
        return URL.ADMIN_HOME_SLASH;
    }

    public int countNoRequiredPages(Pageable pageable) {
        list = themeService.getAll();
        return controllerHelper.countNoOfRequiredPagesForPage(list.size(), pageable.getPageSize());
    }

    public Page<Theme> getAllThemes(Pageable pageable) {
        return themeService.getAll(controllerHelper.getPageableFromPageSize(pageable));
    }


    @PostMapping("/themes")
    public String saveTheme(@ModelAttribute(THEME_OBJ) @Valid ThemeDto themeDto, BindingResult bindingResult,
                            Model model, @PageableDefault(sort = {ID_THEME}, page = 0,
                                    direction = Sort.Direction.DESC) Pageable pageable) {
        setDataToTheModelAfterCrud(themeDto, model, pageable);
        if (controllerHelper.inputHasErrors(bindingResult)) {
            log.warn("Theme name was input incorrect");
            return URL.ADMIN_HOME_SLASH;
        }
        try{
            themeService.save(themeDto);
        }catch (ThemeException e){
            log.warn("Cannot save the theme with name {}. te theme with such name already exists.",themeDto.getName());
            model.addAttribute(ERR_MSG,e.getMessage());
            return URL.ADMIN_HOME_SLASH;
        }
        return URL.REDIRECT_ADMIN_HOME;
    }

    private void setDataToTheModelAfterCrud(ThemeDto themeDto, Model model, Pageable pageable) {
        model.addAttribute(PAGE, pageable);
        model.addAttribute(THEME_OBJ, themeDto);
        model.addAttribute(THEMES,getAllThemes(pageable));
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

        if (controllerHelper.inputHasErrors(bindingResult)) {
            return URL.ADMIN_HOME_SLASH;
        }
        setDataToTheModelAfterCrud(themeDto, model, pageable);
        try{
            updateTheTheme(id, themeDto);
        }catch (ThemeException e){
            log.warn("CONTROLLER FAILED. Cannot update the theme. Please recheck the input data");
            model.addAttribute(ERR_MSG,e.getMessage());
            return URL.ADMIN_HOME_SLASH;
        }
        return URL.REDIRECT_ADMIN_HOME;
    }

    public boolean updateTheTheme(Long id, ThemeDto themeDto) {
        themeDto.setId(id);
        try {
            return themeService.update(themeDto) > 0;
        } catch (Exception e) {
            log.warn("Cannot update the theme {} with id {}. Such theme already exists in the system",
                    themeDto.getName(), themeDto.getId());
            throw new ThemeException("err.update_theme");
        }
    }

    @DeleteMapping("/themes/{id}")
    public String delete(@PathVariable(ID) Long id, Model model,
                         @PageableDefault(sort = {ID_THEME},
                                 direction = Sort.Direction.DESC) Pageable pageable) {
        setDataToTheModelAfterCrud(new ThemeDto(),model,pageable);
        try{
            deleteTheme(id);
        }catch (ThemeException e){
            log.warn("The theme with id {} is in usage.Cannot delete it",id);
            model.addAttribute(ERR_MSG,e.getMessage());
            return URL.ADMIN_HOME_SLASH;
        }
        return URL.REDIRECT_ADMIN_HOME;
    }

    public boolean deleteTheme(Long id) {
        try {
            return themeService.delete(id) > 0;
        } catch (Exception e) {
            throw new ThemeException("err.delete_theme");
        }
    }


}

