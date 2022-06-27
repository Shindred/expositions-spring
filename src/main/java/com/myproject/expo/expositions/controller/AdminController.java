package com.myproject.expo.expositions.controller;

import com.myproject.expo.expositions.controller.util.AdminUtilController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final AdminUtilController adminUtilController;

    @Autowired
    public AdminController(AdminUtilController adminUtilController) {
        this.adminUtilController = adminUtilController;
    }

    @GetMapping("/admin/home")
    public String adminPage() {
        return "admin/home";
    }

    @GetMapping("/admin/users")
    public String users(Model model) {
        model.addAttribute("users", adminUtilController.getAllUsers( 1, 5));
        return "admin/users";
    }

    @PostMapping("/admin/users/{id}")
    public String changeStatus(@PathVariable("id") Long id,
                               @RequestParam("status") String status) {
        return adminUtilController.changeStatus(id, status);
    }


}
