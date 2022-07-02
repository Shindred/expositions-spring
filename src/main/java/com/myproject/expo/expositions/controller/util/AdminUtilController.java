package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.dto.UserDto;
import com.myproject.expo.expositions.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static com.myproject.expo.expositions.util.Constant.*;

@Component
public class AdminUtilController implements ControllerUtils {
    private static final Logger log = LogManager.getLogger(AdminUtilController.class);
    private final UserService userService;

    @Autowired
    public AdminUtilController(UserService userService) {
        this.userService = userService;
    }

    public Page<UserDto> getAllUsers(Integer offset, Integer size) {
        return userService.getAll(getResPageable(PageRequest.of(offset, size), ID));
    }

    public String changeStatus(@PathVariable(ID) Long id,
                               @RequestParam(STATUS) String status) {
        log.info("Id to change status " + id + " todo status " + status);
        userService.blockUnblock(id, status);
        return URL.REDIRECT_ADMIN_HOME;
    }

}
