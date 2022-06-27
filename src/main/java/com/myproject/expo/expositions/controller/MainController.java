package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.controller.util.MainUtilController;
import com.myproject.expo.expositions.dto.UserDto;
import com.myproject.expo.expositions.exception.custom.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@Slf4j
public class MainController {
    private final MainUtilController mainUtilController;

    @Autowired
    public MainController(MainUtilController mainUtilController) {
        this.mainUtilController = mainUtilController;
    }

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @GetMapping("/international")
    public String locale(@RequestParam(value = "localeData", required = false) String localeData,
                         HttpServletRequest req) {
        log.info("locale " + localeData + " url " + req.getServletPath());
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
        log.info("get login page");
        model.addAttribute("user", new UserDto());
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getRegisterPage(@ModelAttribute("user") UserDto user) {
        log.info("get register page");
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute("user") @Valid UserDto user, Model model,
                           BindingResult bindingResult) {
        return mainUtilController.register(user, model, bindingResult);
    }

    @RequestMapping(value = "/change_email", method = RequestMethod.GET)
    public String getChangeEmailPage() {
        return "change_email";
    }

    @RequestMapping(value = "/change_pass", method = RequestMethod.GET)
    public String getChangePassPage() {
        return "change_pass";
    }


    @RequestMapping(value = "/change_email", method = RequestMethod.POST)
    public String changeEmailForUser(@RequestParam("oldEmail") String oldEmail,
            @RequestParam("newEmail") String newEmail, Model model) {
        try{
            mainUtilController.changeEmail(oldEmail, newEmail);
        }catch (UserException e){
            model.addAttribute("errMsg",e.getMessage());
            return "/change_email";
        }
        return "redirect:/login";
    }

//    @RequestMapping(value = "/change_pass", method = RequestMethod.POST)
//    public String changePassForUser(@RequestParam("email")String email,
//             @RequestParam("password") String password, Model model) {
//        return mainUtilController.changePassword(email, password, model);
//    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getIndexPage() {
        return "/index";
    }

    @GetMapping("*/home")
    public String getBackPage(HttpServletRequest req){
        if (req.getServletPath().contains("admin")){
            return "/admin/home";
        }else if (req.getServletPath().contains("user")){
            return "/user/home";
        }else {
            return "/index";
        }
    }

}
