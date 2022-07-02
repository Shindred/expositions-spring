package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.entity.Status;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Locale;

public interface ControllerUtils {
    default boolean inputHasErrors(BindingResult bindingResult) {
        return bindingResult.hasErrors();
    }

    default String checkClientInput(BindingResult bindingResult, String urlBack) {
        return bindingResult.hasErrors() ? urlBack : "";
    }

    default boolean isInputHasErrors(String x) {
        return !x.isEmpty();
    }

    default DateTimeFormatter setDateFormat(Locale locale) {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                .withLocale(locale);
    }

    default DateTimeFormatter setTimeFormat(Locale locale) {
        return DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                .withLocale(locale);
    }

    default Integer defineStatusId(String status) {
        return Arrays.stream(Status.values())
                .filter(val -> val.getName().equals(status))
                .map(Status::getStatusId)
                .mapToInt(Long::intValue)
                .findFirst().orElse(1);
    }

    default String setErrMsgAndPathBack(Model model, String e, String url) {
        model.addAttribute("errMsg", e);
        return url;
    }

    default Pageable getResPageable(Pageable pageable, String sortBy) {
        Pageable pageableRes = null;
        if (pageable.getPageNumber() >= 2) {
            pageableRes = PageRequest
                    .of(pageable.getPageNumber() - 1, pageable.getPageSize(), defineSortingOrder(sortBy));
            System.out.println(pageableRes);
        } else if (pageable.getPageNumber() == 1) {
            pageableRes = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), defineSortingOrder(sortBy));
        } else {
            pageableRes = PageRequest.of(0, pageable.getPageSize(), defineSortingOrder(sortBy));
        }

        return pageableRes;
    }

    default Sort defineSortingOrder(String sortBy) {
        switch (sortBy) {
            case "price":
            case "theme_name":
            case "expoDate":
            case "statistic_tickets":
                return Sort.by(sortBy).ascending();
            default:
                return Sort.by(sortBy).descending();
        }
    }

    default Pageable getPageableFromPageSize(Pageable pageable) {
        Pageable res;
        if (pageable.getPageNumber() > 0) {
            res = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        } else {
            res = PageRequest.of(0, pageable.getPageSize(), pageable.getSort());
        }
        return res;
    }

    default int countNoOfRequiredPagesForPage(int totalRecords, int noOfRecordsPerPage) {
        if (totalRecords % 2 == 0) {
            return (totalRecords / noOfRecordsPerPage);
        } else {
            double result = ((totalRecords * 0.1) / noOfRecordsPerPage);
            return (result % noOfRecordsPerPage > 0)
                    ? (totalRecords / noOfRecordsPerPage) + 1 : (totalRecords / noOfRecordsPerPage);
        }
    }

    default LocalDate parseStrToLocalDate(String date) {
        final DateTimeFormatter datePatternEng = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, datePatternEng);
    }

    default LocalTime parseStrToLocalTime(String time) {
        final DateTimeFormatter timePatternUkr = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(time, timePatternUkr);
    }

    default Pageable getPageable(int page, int size) {
        return PageRequest.of(page, size);
    }

    default String getPathBackForUser(HttpServletRequest req) {
        if (req.getServletPath().contains("admin")) {
            return "/admin/home";
        } else if (req.getServletPath().contains("user")) {
            return "/user/home";
        } else {
            return "/index";
        }
    }

    default void setDateTimeFormatterToModel(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute("dateFormat", setDateFormat(locale));
        model.addAttribute("timeFormat", setTimeFormat(locale));
    }
}
