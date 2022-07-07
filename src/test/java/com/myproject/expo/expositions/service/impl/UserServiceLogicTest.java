package com.myproject.expo.expositions.service.impl;

import com.myproject.expo.expositions.TestRunner;
import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.dto.UserDto;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.entity.Statistic;
import com.myproject.expo.expositions.entity.User;
import com.myproject.expo.expositions.repository.ExpoRepo;
import com.myproject.expo.expositions.repository.UserRepo;
import com.myproject.expo.expositions.service.UserService;
import com.myproject.expo.expositions.service.facade.UserServiceFacade;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Collections;

import static com.myproject.expo.expositions.generator.TestEntity.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class UserServiceLogicTest extends TestRunner {
    private static final BigDecimal TOP_UP_BALANCE = new BigDecimal(100);
    private final Exposition exposition = new Exposition();
    @MockBean
    private UserRepo userRepo;
    @MockBean
    private ExpoRepo expoRepo;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    @Qualifier("userBuild")
    private Build<UserDto, User> build;
    @MockBean
    @Qualifier("expoBuild")
    private Build<ExpoDto, Exposition> buildExpo;
    @MockBean
    private Pageable pageable;
    @MockBean
    @Qualifier("userServiceFacade")
    private UserServiceFacade userServiceFacade;
    @Autowired
    private UserService userService;

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
    public void testFindUserByEmail() {
        when(userRepo.findByEmail("some@gmail.com")).thenReturn(UserTest.user);
        Assert.assertEquals(10L, userService.findByEmail("some@gmail.com").getId(), 0.00001);
    }

    @Test
    public void testSaveUser() {
        User user = UserTest.user;
        when(build.toModel(UserTest.userDto)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn(anyString());
        when(userRepo.save(user)).thenReturn(user);
        when(userServiceFacade.save(UserTest.userDto)).thenReturn(UserTest.user);
        Assertions.assertNotNull(userService.save(UserTest.userDto));
    }

    @Test
    public void testFindAllUsers() {
        when(userRepo.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(UserTest.user)));
        when(build.toDto(any())).thenReturn(UserTest.userDto);
        Assert.assertEquals(1, userService.getAll(pageable).getSize());
    }

    @Test
    public void testBlockUnblockUser() {
        when(userRepo.changeStatus(1L, 12L)).thenReturn(1);
        Assert.assertTrue(userService.blockUnblock(12L, "active"));
    }

    @Test
    public void testUserTopUpBalance() {
        User user = UserTest.user;
        when(userServiceFacade.topUpBalance(user, TOP_UP_BALANCE)).thenReturn(user);
        when(userRepo.changeBalance(17L, TOP_UP_BALANCE)).thenReturn(1);
        when(build.toDto(user)).thenReturn(UserTest.userDto);
        when(userRepo.getBalance(anyLong())).thenReturn(new BigDecimal(600));
        when(build.toModel(UserTest.userDto)).thenReturn(user);
        Assertions.assertNotNull(userService.topUpBalance(user, TOP_UP_BALANCE));
        Assertions.assertNotEquals(BigDecimal.ZERO, userService.topUpBalance(user, TOP_UP_BALANCE).getBalance());
    }

    @Test
    public void testUserBuyExpo() {
        when(expoRepo.save(exposition)).thenReturn(exposition);
        when(userRepo.save(UserTest.user)).thenReturn(UserTest.user);
        when(userServiceFacade.buyExposition(exposition, UserTest.user)).thenReturn(true);
        Assertions.assertTrue(userService.buyExpo(UserTest.user, exposition));
    }

    @Test
    public void testGetUserExpos() {
        when((buildExpo.toDto(any()))).thenReturn(ExpoTest.expoDto1);
        Page<ExpoDto> userExpos = userService.getUserExpos(new PageImpl<>(UserTest.user.getExpos()));
        Assertions.assertNotNull(userExpos);
        Assertions.assertEquals(1, userExpos.getSize());

    }

    @Test
    public void testGetAllExposByStatusIdAndUser() {
        when(expoRepo.findExpositionsByUsersAndStatusId(UserTest.user, 1, PageRequest.of(0, 3)))
                .thenReturn(new PageImpl<>(UserTest.user.getExpos()));
        Page<Exposition> result = userService.getAllExposByStatusIdAndUser(1, UserTest.user, PageRequest.of(0, 3));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getSize());

    }

}