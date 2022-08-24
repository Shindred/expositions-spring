package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.config.userdetails.CustomUserDetails;
import com.myproject.expo.expositions.controller.util.ControllerHelper;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.entity.User;
import com.myproject.expo.expositions.exception.custom.UserException;
import com.myproject.expo.expositions.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.myproject.expo.expositions.util.Constant.*;

/**
 * The UserController class receives requests from the client and send response to required endpoints.
 * Get requests related to user actions
 */
@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('USER')")
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);
    private static final String ONLY_DIGITS = "^\\d+$";
    private final ControllerHelper controllerHelper;
    private final UserService userService;

    public UserController(ControllerHelper controllerHelper, UserService userService) {
        this.controllerHelper = controllerHelper;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String userPage(Model model) {
        setDataForJustLoggedUser(model);
        return URL.USER_HOME;
    }

    private void setDataForJustLoggedUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute(Common.BALANCE, user.getBalance());
        model.addAttribute(Common.USER_ID, user.getId());
        model.addAttribute(Common.EMAIL, user.getUsername());
    }

    @PostMapping("/topup")
    public String topUpBalance(@AuthenticationPrincipal CustomUserDetails user,
                               @RequestParam("amount") String amount, HttpSession session) {
        try {
            containsOnlyDigits(amount);
        } catch (UserException e) {
            session.setAttribute("infMsg", e.getMessage());
            return URL.USER_REDIRECT_HOME_PAGE;
        }
        topUpBalance(user, new BigDecimal(amount));
        return URL.USER_REDIRECT_HOME_PAGE;
    }

    public boolean containsOnlyDigits(String str) {
        return Optional.ofNullable(str)
                .map(value -> Pattern.compile(ONLY_DIGITS).matcher(value).matches())
                .filter(value -> value)
                .orElseThrow(() -> new UserException("err.incorrect_amount"));
    }

    public User topUpBalance(CustomUserDetails user, BigDecimal amount) {
        BigDecimal balance = userService.topUpBalance(user.getUser(), amount).getBalance();
        user.getUser().setBalance(balance);
        return user.getUser();
    }


    @PostMapping("/expos/buy/{id}")
    public String buyExpo(@AuthenticationPrincipal CustomUserDetails user,
                          @PathVariable(ID) Long id, Model model) {
        log.debug("User  with email {} buying exposition {}", user, id);
        try {
            buyExpo(user.getUser(), id);
        } catch (UserException e) {
            log.warn("Cannot buy expo. User balance issue.");
            model.addAttribute(ERR_MSG, e.getMessage());
            return URL.USER_HOME;
        }
        return URL.USER_REDIRECT_HOME_PAGE;
    }

    public boolean buyExpo(User user, Long id) {
        Exposition exposition = userService.getExpoById(id);
        return userService.buyExpo(user, exposition);
    }

    @GetMapping("/myExpos")
    public String getUserExpos(@AuthenticationPrincipal CustomUserDetails user,
                               @RequestParam(defaultValue = "active") String status,
                               @PageableDefault(size = 25) Pageable pageable,
                               Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute(Common.DATE_FORMAT, controllerHelper.setDateFormat(locale));
        model.addAttribute(Common.TIME_FORMAT, controllerHelper.setTimeFormat(locale));
        model.addAttribute(Common.MY_EXPOS, getUserExpos(user.getUser(), status, pageable));
        return URL.USER_HOME;
    }

    public Page<ExpoDto> getUserExpos(User user, String status, Pageable pageable) {
        log.debug("The status was entered {}", status);
        Integer statusId = controllerHelper.defineStatusId(status);
        return userService.getUserExpos(userService
                .getAllExposByStatusIdAndUser(statusId, user, pageable));
    }

}
