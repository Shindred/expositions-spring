package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.controller.util.AdminControllerUtil;
import com.myproject.expo.expositions.controller.util.ControllerHelper;
import com.myproject.expo.expositions.service.UserService;
import com.myproject.expo.expositions.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.myproject.expo.expositions.util.Constant.*;

/**
 * The AdminUtilController class do transient operations and transport data to the service layer
 */
@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
     private final UserService userService;
     private final ControllerHelper controllerHelper;

    public AdminController(UserService userService,ControllerHelper controllerHelper) {
        this.userService = userService;
        this.controllerHelper = controllerHelper;
    }

    @GetMapping("/admin/home")
    public String adminPage() {
        return URL.ADMIN_HOME_NO_1_SLASH;
    }

    @GetMapping("/admin/users")
    public String users(Model model) {
        model.addAttribute("users",userService.getAll(controllerHelper.getResPageable(PageRequest.of(1,5), ID)));
        return URL.ADMIN_USERS;
    }

    @PostMapping("/admin/users/{id}")
    public String changeStatus(@PathVariable("id") Long id,
                               @RequestParam("status") String status) {
        userService.blockUnblock(id, status);
        return URL.REDIRECT_ADMIN_HOME;
    }

}
