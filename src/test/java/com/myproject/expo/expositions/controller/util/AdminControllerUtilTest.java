package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.TestRunner;
import com.myproject.expo.expositions.dto.UserDto;
import com.myproject.expo.expositions.service.UserService;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static com.myproject.expo.expositions.generator.EntityStorage.UserTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class AdminControllerUtilTest extends TestRunner {
    private static final String BLOCKED = "blocked";
    @Mock
    private UserService userService;
    @Autowired
    private AdminControllerUtil adminUtilController;

    @Test
    public void getAllUsers() {
        when(userService.getAll(PageRequest.of(1, 1)))
                .thenReturn(new PageImpl<>(Collections.singletonList(UserTest.userDto)));

        Page<UserDto> allUsers = adminUtilController.getAllUsers(1, 1);

        assertThat(allUsers).isNotNull();
        assertThat(allUsers.getSize()).isEqualTo(1);
    }

    @Test
    public void changeStatus() {
        when(userService.blockUnblock(12L, BLOCKED)).thenReturn(true);

        assertThat(adminUtilController.changeStatus(12L, BLOCKED)).isEqualTo("redirect:/admin/home");
    }
}