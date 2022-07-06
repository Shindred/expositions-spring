package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.controller.util.ControllerUtils;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.exception.custom.ExpoException;
import com.myproject.expo.expositions.service.ExpoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static com.myproject.expo.expositions.util.Constant.*;

/**
 * The GetAllExposController class returns all expositions from DB with pagination.
 * Search the element by search value and its option
 */
@Controller
public class GetAllExposController implements ControllerUtils {
    private static final Logger log = LogManager.getLogger(GetAllExposController.class);
    private final ExpoService expoService;

    @Autowired
    public GetAllExposController(ExpoService expoService) {
        this.expoService = expoService;
    }

    @GetMapping(value = {"/admin/expos", "/user/expos", "/index/**"})
    public String getAllExpos(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size,
                              @RequestParam(defaultValue = "idExpo") String sortBy,
                              @PageableDefault(page = 1, size = 5, sort = {"idExpo"}) Pageable pageable,
                              Model model, HttpServletRequest req) {
        Pageable pageableRes = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defineSortingOrder(sortBy));
        setDataToTheModel(page, size, sortBy, model);
        model.addAttribute(NUMBER_OF_PAGES, countNoOfRequiredPagesForPage(expoService.getAll().size(), pageableRes.getPageSize()));
        model.addAttribute(PAGE, pageableRes);
        Page<ExpoDto> expos = getAllExpos(pageableRes);
        model.addAttribute(EXPOS, expos);
        setDateTimeFormatterToSession(req);
        return defineBackPathToUser(req);
    }

    private String defineBackPathToUser(HttpServletRequest req) {
        if (req.getServletPath().contains(ADMIN)) {
            return URL.ADMIN_HOME_SLASH;
        } else if (req.getServletPath().contains(USER)) {
            return URL.USER_HOME;
        } else {
            return URL.INDEX;
        }
    }

    private Page<ExpoDto> getAllExpos(Pageable pageable) {
        return expoService.getAll(getPageableFromPageSize(pageable));
    }

    private void setDataToTheModel(Integer offset, Integer size, String sortBy, Model model) {
        model.addAttribute(CURR_PAGE, offset);
        model.addAttribute(SIZE, size);
        model.addAttribute(SORT_BY, sortBy);
        model.addAttribute(EXPO_OBJ, new ExpoDto());
    }

    @GetMapping("*/search")
    public String search(@RequestParam("search") String search, @RequestParam("selected") String selected, Model model, HttpServletRequest req) {
        Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute(DATE_FORMAT, setDateFormat(locale));
        model.addAttribute(TIME_FORMAT, setTimeFormat(locale));
        try {
            model.addAttribute(SEARCHED_EXPOS, expoService.getSearchedExpos(search, selected));
        } catch (ExpoException e) {
            log.info("Searching element {} by selected option {} was failed", search, selected);
            model.addAttribute(ERR_MSG, e.getMessage());
            return defineBackPathToUser(req);
        }
        return defineBackPathToUser(req);
    }
}
