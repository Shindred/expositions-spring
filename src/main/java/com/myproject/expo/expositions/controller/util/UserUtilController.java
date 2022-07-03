package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.config.userdetails.CustomUserDetails;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.User;
import com.myproject.expo.expositions.exception.custom.UserException;
import com.myproject.expo.expositions.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * The UserUtilController class validates the income data and transmits the data to the service layer
 */
@Component
public class UserUtilController implements ControllerUtils {
    private static final Logger log = LogManager.getLogger(UserUtilController.class);
    private final UserService userService;

    @Autowired
    public UserUtilController(UserService userService) {
        this.userService = userService;
    }

    public boolean containsOnlyDigits(String str) {
        String ONLY_DIGITS = "^\\d+$";
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
        return userService.buyExpo(user, userService.getExpoById(id));
    }

    public Page<ExpoDto> getUserExpos(User user, String status) {
        log.info("input  status {}", status);
        Integer statusId = defineStatusId(status);
        return userService.getUserExpos(userService
                .getAllExposByStatusIdAndUser(statusId, user, PageRequest.of(0, 5)));
    }
}
