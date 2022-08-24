package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.TestRunner;
import com.myproject.expo.expositions.config.userdetails.CustomUserDetails;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.entity.Statistic;
import com.myproject.expo.expositions.exception.custom.UserException;
import com.myproject.expo.expositions.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static com.myproject.expo.expositions.generator.EntityStorage.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserControllerUtilTest extends TestRunner {
    @Mock
    private Exposition exposition;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserUtilController userUtilController;

    @Before
    public void init() {
        exposition.setIdExpo(17L);
        exposition.setName("sky-champions");
        exposition.setExpoDate(LocalDate.of(2022, Month.AUGUST, 12));
        exposition.setExpoTime(LocalTime.of(13, 30));
        exposition.setPrice(new BigDecimal(300));
        exposition.setStatusId(1);
        exposition.setStatistic(new Statistic(1L, 20L, 450L));
        exposition.setHalls(Collections.singleton(HallTest.hall1));
        exposition.setTheme(ThemeTest.theme1);
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
    public void testTopUpBalance() {
        CustomUserDetails user = new CustomUserDetails(UserTest.user);

        when(userService.topUpBalance(UserTest.user, new BigDecimal(100))).thenReturn(UserTest.user);

        assertThat(userUtilController.topUpBalance(user, new BigDecimal(100))).isNotNull();
    }

    @Test
    public void testBuyExpo() {
        when(userService.getExpoById(17L)).thenReturn(exposition);
        when(exposition.getIdExpo()).thenReturn(17L);
        when(userService.buyExpo(UserTest.user, exposition)).thenReturn(true);

        assertThat(userUtilController.buyExpo(UserTest.user, exposition.getIdExpo())).isTrue();

        verify(userService).getExpoById(17L);
        verify(userService).buyExpo(UserTest.user, exposition);
    }

    @Test
    public void testGetUserExpos() {
        when(userService.getAllExposByStatusIdAndUser(1,
                UserTest.user, PageRequest.of(0, 2)))
                .thenReturn(new PageImpl<>(List.of(exposition, exposition)));
        when(userService.getUserExpos(new PageImpl<>(List.of(exposition, exposition))))
                .thenReturn(new PageImpl<>(List.of(ExpoTest.expoDto1, ExpoTest.expoDto2)));

        assertThat(userUtilController.getUserExpos(UserTest.user, "active",PageRequest.of(0,5))).isNull();

    }
}