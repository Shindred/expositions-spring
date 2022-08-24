package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.config.userdetails.CustomUserDetails;
import com.myproject.expo.expositions.controller.util.ControllerUtil;
import com.myproject.expo.expositions.controller.util.UserUtilController;
import com.myproject.expo.expositions.exception.custom.UserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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

import static com.myproject.expo.expositions.util.Constant.*;

/**
 * The UserController class receives requests from the client and send response to required endpoints.
 * Get requests related to user actions
 */
@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('USER')")
public class UserController implements ControllerUtil {
    private static final Logger log = LogManager.getLogger(UserController.class);
    private final UserUtilController userUtilController;

    @Autowired
    public UserController(UserUtilController userUtilController) {
        this.userUtilController = userUtilController;
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
            userUtilController.containsOnlyDigits(amount);
        } catch (UserException e) {
            session.setAttribute("infMsg", e.getMessage());
            return URL.USER_REDIRECT_HOME_PAGE;
        }
        userUtilController.topUpBalance(user, new BigDecimal(amount));
        return URL.USER_REDIRECT_HOME_PAGE;
    }

    @PostMapping("/expos/buy/{id}")
    public String buyExpo(@AuthenticationPrincipal CustomUserDetails user,
                          @PathVariable(ID) Long id, Model model) {
        log.info("user {} buy expo {}",user,id);
        try {
            userUtilController.buyExpo(user.getUser(), id);
        } catch (UserException e) {
            log.warn("Cannot buy expo");
            model.addAttribute(ERR_MSG, e.getMessage());
            return URL.USER_HOME;
        }
        return URL.USER_REDIRECT_HOME_PAGE;
    }

    @GetMapping("/myExpos")
    public String getUserExpos(@AuthenticationPrincipal CustomUserDetails user,
                               @RequestParam(defaultValue = "active") String status,
                               @PageableDefault(size = 25) Pageable pageable,
                               Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute(Common.DATE_FORMAT, setDateFormat(locale));
        model.addAttribute(Common.TIME_FORMAT, setTimeFormat(locale));
        model.addAttribute(Common.MY_EXPOS, userUtilController.getUserExpos(user.getUser(), status,pageable));
        return URL.USER_HOME;
    }

}
