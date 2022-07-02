package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.config.userdetails.CustomUserDetails;
import com.myproject.expo.expositions.controller.util.ControllerUtils;
import com.myproject.expo.expositions.controller.util.UserUtilController;
import com.myproject.expo.expositions.entity.User;
import com.myproject.expo.expositions.exception.custom.UserException;
import com.myproject.expo.expositions.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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

/**
 * The UserController class receives requests from the client and send response to required endpoints.
 * Get requests related to user actions
 */
@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('USER')")
public class UserController implements ControllerUtils {
    private static final Logger log = LogManager.getLogger(UserController.class);
    private final UserUtilController userUtilController;

    @Autowired
    public UserController(UserUtilController userUtilController) {
        this.userUtilController = userUtilController;
    }

    @GetMapping("/home")
    public String userPage(Model model) {
        setDataForJustLoggedUser(model);
        return Constant.URL.USER_HOME;
    }

    private void setDataForJustLoggedUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute(Constant.Common.BALANCE, user.getBalance());
        model.addAttribute(Constant.Common.USER_ID, user.getId());
        model.addAttribute(Constant.Common.EMAIL, user.getUsername());
    }

    @PostMapping("/topup")
    public String topUpBalance(@AuthenticationPrincipal CustomUserDetails user,
                               @RequestParam("amount") String amount,HttpSession session) {
        try{
            userUtilController.containsOnlyDigits(amount);
        }catch (UserException e){
            session.setAttribute("infMsg",e.getMessage());
            return Constant.URL.USER_REDIRECT_HOME_PAGE;
        }
        userUtilController.topUpBalance(user, new BigDecimal(amount));
        return Constant.URL.USER_REDIRECT_HOME_PAGE;
    }

    @PostMapping("/expos/buy/{id}")
    public String buyExpo(@AuthenticationPrincipal User user,
                          @PathVariable("id") Long id, Model model, HttpSession session) {
        try{
            userUtilController.buyExpo(user,id);
        }catch (UserException e){
            log.warn("Cannot buy expo");
            model.addAttribute(Constant.ERR_MSG,e.getMessage());
            return Constant.URL.USER_HOME;
        }
        return Constant.URL.USER_REDIRECT_HOME_PAGE;
    }

    @GetMapping("/myExpos")
    public String getUserExpos(@AuthenticationPrincipal User user,
                               @RequestParam(defaultValue = "active") String status,
                               Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute(Constant.Common.DATE_FORMAT, setDateFormat(locale));
        model.addAttribute(Constant.Common.TIME_FORMAT, setTimeFormat(locale));
        model.addAttribute(Constant.Common.MY_EXPOS,userUtilController.getUserExpos(user,status));
        return Constant.URL.USER_HOME;
    }

}
