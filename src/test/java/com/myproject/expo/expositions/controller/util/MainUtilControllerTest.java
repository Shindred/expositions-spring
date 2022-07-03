package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.TestRunner;
import com.myproject.expo.expositions.exception.custom.UserException;
import com.myproject.expo.expositions.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static com.myproject.expo.expositions.generator.TestEntity.UserTest;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class MainUtilControllerTest extends TestRunner {
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

    @Before
    public void init(){
        when(userService.save(UserTest.userDto)).thenReturn(UserTest.user);
        when(userService.changeEmail(OLD_EMAIL, NEW_EMAIL)).thenReturn(1);

    }
    @Test
    public void register() {
        assertEquals("/register", mainUtilController.register(UserTest.userDto, model, bindingResult));
    }

    @Test(expected = UserException.class)
    public void changeEmail() {
        mainUtilController.changeEmail(OLD_EMAIL, NEW_EMAIL);
    }
}