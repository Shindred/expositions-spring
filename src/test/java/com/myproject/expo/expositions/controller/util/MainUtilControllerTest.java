package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.exception.custom.UserException;
import com.myproject.expo.expositions.generator.TestEntity;
import com.myproject.expo.expositions.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainUtilControllerTest {
    private static final String OLD_EMAIL = "some@gmail.com";
    private static final String NEW_EMAIL = "new@gmail.com";
    @Autowired
    private MainUtilController mainUtilController;
    @Mock
    private UserService userService;
    @Mock
    private Model model;
    @Mock
    private BindingResult bindingResult;

    @Test
    public void register() {
        when(userService.save(TestEntity.UserTest.userDto)).thenReturn(TestEntity.UserTest.user);
        assertEquals("/register",mainUtilController.register(TestEntity.UserTest.userDto,model,bindingResult));
    }

    @Test(expected = UserException.class)
    public void changeEmail() {
        when(userService.changeEmail(OLD_EMAIL,NEW_EMAIL)).thenReturn(1);
        mainUtilController.changeEmail(OLD_EMAIL,NEW_EMAIL);
    }
}