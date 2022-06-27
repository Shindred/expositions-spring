package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.dto.UserDto;
import com.myproject.expo.expositions.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@Slf4j
public class AdminUtilController implements ControllerUtils {
    private final UserService userService;

    @Autowired
    public AdminUtilController(UserService userService) {
        this.userService = userService;
    }

    public Page<UserDto> getAllUsers(Integer offset,Integer size) {
        return userService.getAll(getResPageable(PageRequest.of(offset,size),"id"));
    }

    public String changeStatus(@PathVariable("id") Long id,
                               @RequestParam("status") String status) {
        log.info("Id to change status " + id + " todo status " + status);
        userService.blockUnblock(id, status);
        return "redirect:/admin/home";
    }

}
