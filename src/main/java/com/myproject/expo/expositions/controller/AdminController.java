package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.controller.util.AdminControllerUtil;
import com.myproject.expo.expositions.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The AdminUtilController class do transient operations and transport data to the service layer
 */
@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final AdminControllerUtil adminUtilController;

    public AdminController(AdminControllerUtil adminUtilController) {
        this.adminUtilController = adminUtilController;
    }

    @GetMapping("/admin/home")
    public String adminPage() {
        return Constant.URL.ADMIN_HOME_NO_1_SLASH;
    }

    @GetMapping("/admin/users")
    public String users(Model model) {
        model.addAttribute("users", adminUtilController.getAllUsers( 1, 5));
        return Constant.URL.ADMIN_USERS;
    }

    @PostMapping("/admin/users/{id}")
    public String changeStatus(@PathVariable("id") Long id,
                               @RequestParam("status") String status) {
        return adminUtilController.changeStatus(id, status);
    }

}
