package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.dto.UserDto;
import com.myproject.expo.expositions.generator.TestEntity;
import com.myproject.expo.expositions.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminUtilControllerTest {
    @Mock
    private UserService userService;
    @Autowired
    private AdminUtilController adminUtilController;

    @Test
    public void getAllUsers() {
        when(userService.getAll(PageRequest.of(1, 1)))
                .thenReturn(new PageImpl<>(Collections.singletonList(TestEntity.UserTest.userDto)));
        Page<UserDto> allUsers = adminUtilController.getAllUsers(1, 1);
        assertThat(allUsers).isNotNull();
        assertThat(allUsers.getSize()).isEqualTo(1);
    }

    @Test
    public void changeStatus() {
        when(userService.blockUnblock(anyLong(), anyString())).thenReturn(true);
        assertEquals("redirect:/admin/home", adminUtilController.changeStatus(anyLong(), anyString()));
    }
}