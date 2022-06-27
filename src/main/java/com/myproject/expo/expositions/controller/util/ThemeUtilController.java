package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.dto.ThemeDto;
import com.myproject.expo.expositions.entity.Theme;
import com.myproject.expo.expositions.exception.custom.ThemeException;
import com.myproject.expo.expositions.service.ThemeService;
import com.myproject.expo.expositions.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

@Component
@Slf4j
public class ThemeUtilController implements ControllerUtils {
    private static final String PATH_BACK = "/admin/themes";
    private final ThemeService themeService;
    private List<Theme> list;

    @Autowired
    public ThemeUtilController(ThemeService themeService,List<Theme> list) {
        this.themeService = themeService;
        this.list = list;
    }

    public Page<Theme> getAllThemes(Pageable pageable) {
//        try {
//            Page<Theme> allThemes = themeService.getAll(getPageableFromPageSize(pageable));
//            list = themeService.getAll();
//            setAllDAtaToTheModelGetAllThemesMethod(model, pageable, allThemes);
//        } catch (Exception e) {
//            log.warn("Cannot get all themes");
//            return setErrMsgAndPathBack(model, "err.cant_get_themes", PATH_BACK);
//        }
//        return "admin/themes";
        return  themeService.getAll(getPageableFromPageSize(pageable));
    }

    public int countNoRequiredPages(Pageable pageable){
        list = themeService.getAll();
        return countNoOfRequiredPagesForPage(list.size(), pageable.getPageSize());
    }

    private void setAllDAtaToTheModelGetAllThemesMethod(Model model, Pageable pageable, Page<Theme> allThemes) {
        model.addAttribute("numberOfPages",countNoOfRequiredPagesForPage(list.size(), pageable.getPageSize()));
        model.addAttribute("themes", allThemes);
        model.addAttribute("themeObj", new ThemeDto());
        model.addAttribute("page", pageable);
    }

    public Theme saveTheTheme(ThemeDto themeDto) {
//        setPageableAndHallsToTheModel(model, pageable, themeDto);
//        if (inputHasErrors(bindingResult)) {
//            return PATH_BACK;
//        }
//        try {
//            themeService.save(themeDto);
//        } catch (Exception e) {
//            log.warn("Cannot save the given {} theme",themeDto.getName());
//            setAllThemesToTheModel(model, pageable);
//            return setErrMsgAndPathBack(model, "err.theme_save", PATH_BACK);
//        }
//        return Constant.REDIRECT + PATH_BACK;
        return  themeService.save(themeDto);
    }

    public String cancelAddTHeme(String cancel) {
        return cancel != null ? Constant.REDIRECT + PATH_BACK : PATH_BACK;
    }

    public boolean updateTheTheme(Long id, ThemeDto themeDto) {
        themeDto.setId(id);
        try {
            return themeService.update(themeDto) > 0;
        } catch (Exception e) {
            log.warn("Cannot update the theme {} with id {}",themeDto.getName(),themeDto.getId());
            throw new ThemeException("err.update_theme");
        }
    }

    private void setPageableAndHallsToTheModel(Model model, Pageable pageable, ThemeDto themeDto) {
        model.addAttribute("page", pageable);
        setAllThemesToTheModel(model, pageable);
        model.addAttribute("themeObj", themeDto);
    }

    public boolean deleteTheme(Long id) {
        return themeService.delete(id) > 0;

    }

    private void setAllThemesToTheModel(Model model, Pageable pageable) {
        model.addAttribute("themes", themeService.getAll(pageable));
    }

}
