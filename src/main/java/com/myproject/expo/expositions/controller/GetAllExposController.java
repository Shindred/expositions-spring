package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.controller.util.ControllerUtils;
import com.myproject.expo.expositions.controller.util.ExpoUtilController;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.entity.Role;
import com.myproject.expo.expositions.exception.custom.ExpoException;
import com.myproject.expo.expositions.service.ExpoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Controller
@Slf4j
//@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
public class GetAllExposController implements ControllerUtils {
    private final ExpoUtilController expoUtilController;
    private final ExpoService expoService;
    private List<Exposition> list;

    @Autowired
    public GetAllExposController(ExpoUtilController expoUtilController,
                                 ExpoService expoService, List<Exposition> list) {
        this.expoUtilController = expoUtilController;
        this.expoService = expoService;
        this.list = list;
    }

    @GetMapping(value = {"/admin/expos", "/user/expos", "/index/**"})
    public String getAllExpos(@RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "5") Integer size,
                              @RequestParam(defaultValue = "idExpo") String sortBy,
                              @PageableDefault(page = 1, size = 5,sort = {"idExpo"}) Pageable pageable,
                              Model model, HttpServletRequest req) {
        Pageable pageableRes = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),defineSortingOrder(sortBy));
        System.out.println("pageable " + pageableRes);
        setDataToTheModel(page, size, sortBy, model);
        list = expoService.getAll();
        model.addAttribute("numberOfPages", countNoOfRequiredPagesForPage(list.size(), pageableRes.getPageSize()));
        model.addAttribute("page", pageableRes);
        Page<ExpoDto> expos = getAllExpos(pageableRes);
        expos.forEach(System.out::println);
        model.addAttribute("expos", expos);
        return defineBackPathToUser(req);
    }

    private String defineBackPathToUser(HttpServletRequest req) {
        if (req.getServletPath().contains("admin")) {
            return "/admin/home";
        } else if (req.getServletPath().contains("user")) {
            return "/user/home";
        } else {
            return "/index";
        }
       // return req.getServletPath();
    }

    private Page<ExpoDto> getAllExpos(Pageable pageable) {
        return expoService.getAll(getPageableFromPageSize(pageable));
    }

    private Pageable getPageable(Integer offset, Integer size, String sortBy) {
        return getResPageable(PageRequest.of(offset, size), sortBy);
    }

    private void setDataToTheModel(Integer offset, Integer size, String sortBy, Model model) {
        model.addAttribute("currPage", offset);
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        setDateTimeFormatterToModel(model);
        model.addAttribute("expoObj", new ExpoDto());
    }

    @GetMapping("*/search")
    public String search(@RequestParam("search") String search,
                         @RequestParam("selected") String selected, Model model, HttpServletRequest req) {
        log.info("search {} selected {} path {}", search, selected, req.getServletPath());
        Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute("dateFormat", setDateFormat(locale));
        model.addAttribute("timeFormat", setTimeFormat(locale));
        try {
            model.addAttribute("searchedExpos", expoService.getSearchedExpos(search, selected));
        } catch (ExpoException e) {
            log.info("err works");
            model.addAttribute("errMSg", e.getMessage());
            return defineBackPathToUser(req);
        }
      //  return "/index/searched";
        return defineBackPathToUser(req);
    }
}
