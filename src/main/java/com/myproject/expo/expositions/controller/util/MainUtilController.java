package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.dto.UserDto;
import com.myproject.expo.expositions.exception.custom.UserException;
import com.myproject.expo.expositions.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import java.math.BigDecimal;

@Component
@Slf4j
public class MainUtilController implements ControllerUtils {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MainUtilController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(@ModelAttribute("user") @Valid UserDto user,
                           Model model, BindingResult bindingResult) {
        String pathToClientIfErr = checkClientInput(bindingResult, "/register");
        if (isInputHasErrors(pathToClientIfErr)) return pathToClientIfErr;
        String pathBack = registerUser(user, model);
        return isInputHasErrors(pathBack) ? pathBack : "redirect:/login";
    }

    private String registerUser(UserDto user, Model model) {
        try {
            user.setBalance(BigDecimal.ZERO);
            userService.save(user);
            log.info("register post " + user.getEmail() + " " + user.getPassword());
        } catch (UserException e) {
            model.addAttribute("errMsg", e.getMessage());
            return "/register";
        }
        return "";
    }

//    public String changePassword(String email, String password, Model model) {
//        try {
//            userService.changePassword(email, passwordEncoder.encode(password));
//        } catch (Exception e) {
//            model.addAttribute("errMsg", "Cannot change the password. Such user doesnt exist!");
//            return "/change_password";
//        }
//        return "redirect:/login";
//    }

    public int changeEmail(String oldEmail, String newEmail) {
        return userService.changeEmail(oldEmail, newEmail);
    }
}
