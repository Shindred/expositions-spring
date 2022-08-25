package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.config.userdetails.CustomUserDetails;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.entity.User;
import com.myproject.expo.expositions.exception.custom.UserException;
import com.myproject.expo.expositions.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * The UserUtilController class validates the income data and transmits the data to the service layer
 */
@Component
public class UserControllerUtil {
    private static final Logger log = LogManager.getLogger(UserControllerUtil.class);
    private static final String ONLY_DIGITS = "^\\d+$";
    private final UserService userService;
    private final ControllerHelper controllerHelper;

    public UserControllerUtil(UserService userService, ControllerHelper controllerHelper) {
        this.userService = userService;
        this.controllerHelper = controllerHelper;
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

    public boolean buyExpo(User user, Long id) {
        Exposition exposition = userService.getExpoById(id);
        return userService.buyExpo(user, exposition);
    }

    public Page<ExpoDto> getUserExpos(User user, String status, Pageable pageable) {
        log.debug("The status was entered {}", status);
        Integer statusId = controllerHelper.defineStatusId(status);
        return userService.getUserExpos(userService
                .getAllExposByStatusIdAndUser(statusId, user, pageable));
    }
}
