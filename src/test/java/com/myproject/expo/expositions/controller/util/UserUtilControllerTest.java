package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.entity.Statistic;
import com.myproject.expo.expositions.entity.Status;
import com.myproject.expo.expositions.exception.custom.UserException;
import com.myproject.expo.expositions.generator.TestEntity;
import com.myproject.expo.expositions.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserUtilControllerTest {
    private final Exposition exposition = new Exposition();
    @Autowired
    private UserUtilController userUtilController;
    @Autowired
    @Qualifier("expoBuild")
    private Build<ExpoDto, Exposition> build;
    @Mock
    private UserService userService;

    @Before
    public void init(){
        exposition.setIdExpo(17L);
        exposition.setName("sky-champions");
        exposition.setExpoDate(LocalDate.of(2022, Month.AUGUST, 12));
        exposition.setExpoTime(LocalTime.of(13, 30));
        exposition.setPrice(new BigDecimal(300));
        exposition.setStatusId(1);
        exposition.setStatistic(new Statistic(1L, 20L, 450L));
        exposition.setHalls(Collections.singleton(TestEntity.HallTest.hall1));
        exposition.setTheme(TestEntity.ThemeTest.theme1);
    }

    @Test
    public void containsOnlyDigitsPositiveScenario() {
        assertThat(userUtilController.containsOnlyDigits("123")).isTrue();
    }

    @Test(expected = UserException.class)
    public void containsOnlyDigitsNegativeScenario() {
        userUtilController.containsOnlyDigits("asdf$");
    }

    @Test
    public void topUpBalance() {
        when(userService.topUpBalance(TestEntity.UserTest.user,new BigDecimal(100))).thenReturn(TestEntity.UserTest.user);
       //TODO REDO
        // assertThat(userUtilController.topUpBalance(TestEntity.UserTest.user,new BigDecimal(100))).isNotNull();
    }

    @Test
    public void buyExpo() {
        //TODO FIX ME
        when(userService.getExpoById(17L)).thenReturn(exposition);
        assertThat(userUtilController.buyExpo(TestEntity.UserTest.user,17L)).isTrue();
    }

    @Test
    public void getUserExpos() {
        when(userService.getAllExposByStatusIdAndUser(1,
                TestEntity.UserTest.user, PageRequest.of(0,2)))
                .thenReturn(new PageImpl<>(List.of(exposition,exposition)));
        when(userService.getUserExpos(new PageImpl<>(List.of(exposition,exposition))))
                .thenReturn(new PageImpl<>(List.of(build.toDto(exposition),build.toDto(exposition))));
        assertThat(userUtilController.getUserExpos(TestEntity.UserTest.user,"active"))
                .isNotNull();

    }
}