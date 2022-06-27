package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.controller.util.ControllerUtils;
import com.myproject.expo.expositions.controller.util.UserUtilController;
import com.myproject.expo.expositions.entity.User;
import com.myproject.expo.expositions.exception.custom.UserException;
import lombok.extern.slf4j.Slf4j;
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

@Controller
@RequestMapping("/user")
@Slf4j
@PreAuthorize("hasAuthority('USER')")
public class UserController implements ControllerUtils {
    private final UserUtilController userUtilController;

    @Autowired
    public UserController(UserUtilController userUtilController) {
        this.userUtilController = userUtilController;
    }

    @GetMapping("/home")
    public String userPage(Model model) {
        setDataForJustLoggedUser(model);
        return "/user/home";
    }

    private void setDataForJustLoggedUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("balance", user.getBalance());
        model.addAttribute("userId", user.getId());
        model.addAttribute("email", user.getEmail());
    }

    @PostMapping("/topup")
    public String topUpBalance(@AuthenticationPrincipal User user,
                               @RequestParam("amount") String amount,HttpSession session) {
        try{
            userUtilController.containsOnlyDigits(amount);
        }catch (UserException e){
            session.setAttribute("infMsg",e.getMessage());
            return "redirect:/user/home";
        }
        userUtilController.topUpBalance(user, new BigDecimal(amount));
        return "redirect:/user/home";
    }

    @PostMapping("/expos/buy/{id}")
    public String buyExpo(@AuthenticationPrincipal User user,
                          @PathVariable("id") Long id, Model model, HttpSession session) {
        try{
            userUtilController.buyExpo(user,id);
        }catch (UserException e){
            log.warn("Cannot buy expo");
            session.setAttribute("errMsg",e.getMessage());
            return "redirect:/user/home";
        }
        session.setAttribute("infMsg", "inf.thanks_for_purchase");
        return "redirect:/user/home";
    }

    @GetMapping("/myExpos")
    public String getUserExpos(@AuthenticationPrincipal User user,
                               @RequestParam(defaultValue = "active") String status,
                               Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute("dateFormat", setDateFormat(locale));
        model.addAttribute("timeFormat", setTimeFormat(locale));
        model.addAttribute("myExpos",userUtilController.getUserExpos(user,status));
        return "/user/home";
    }

}
