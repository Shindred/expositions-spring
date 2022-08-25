package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.dto.UserDto;
import com.myproject.expo.expositions.exception.custom.UserException;
import com.myproject.expo.expositions.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import java.math.BigDecimal;

import static com.myproject.expo.expositions.util.Constant.*;

/**
 * The MainUtilController class do transfer operations with data, transfer data to the service layer if data ok
 */
@Component
public class MainUtilController {
    private static final Logger log = LogManager.getLogger(MainUtilController.class);
    private final UserService userService;
    private final ControllerHelper controllerHelper;

    public MainUtilController(UserService userService,ControllerHelper controllerHelper) {
        this.userService = userService;
        this.controllerHelper = controllerHelper;
    }

    public String register(@ModelAttribute(USER) @Valid UserDto user,
                           Model model, BindingResult bindingResult) {
        String pathToClientIfErr = controllerHelper.checkClientInput(bindingResult, URL.REGISTER);
        if (controllerHelper.isInputHasErrors(pathToClientIfErr)) return pathToClientIfErr;
        String pathBack = registerUser(user, model);
        return controllerHelper.isInputHasErrors(pathBack) ? pathBack : URL.REDIRECT_LOGIN;
    }

    private String registerUser(UserDto user, Model model) {
        try {
            user.setBalance(BigDecimal.ZERO);
            userService.save(user);
            log.debug("Registered a new user {}",user.getEmail());
        } catch (UserException e) {
            model.addAttribute(ERR_MSG, e.getMessage());
            return URL.REGISTER;
        }
        return "";
    }

    public int changeEmail(String oldEmail, String newEmail) {
        return userService.changeEmail(oldEmail, newEmail);
    }
}
