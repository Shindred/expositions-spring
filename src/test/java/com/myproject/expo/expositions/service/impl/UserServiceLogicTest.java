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
import org.junit.Before;
import org.junit.Test;
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

import static com.myproject.expo.expositions.generator.EntityStorage.*;
import static org.assertj.core.api.Assertions.assertThat;
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

        assertThat(userService.findByEmail("some@gmail.com").getId()).isEqualTo(10L);
    }

    @Test
    public void testSaveUser() {
        User user = UserTest.user;

        when(build.toModel(UserTest.userDto)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn(anyString());
        when(userRepo.save(user)).thenReturn(user);
        when(userServiceFacade.save(UserTest.userDto)).thenReturn(UserTest.user);

        assertThat(userService.save(UserTest.userDto)).isNotNull();
    }

    @Test
    public void testFindAllUsers() {
        when(userRepo.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(UserTest.user)));
        when(build.toDto(any())).thenReturn(UserTest.userDto);

        assertThat(userService.getAll(pageable).getSize()).isEqualTo(1);
    }

    @Test
    public void testBlockUnblockUser() {
        when(userRepo.changeStatus(1L, 12L)).thenReturn(1);

        assertThat(userService.blockUnblock(12L, "active")).isTrue();
    }

    @Test
    public void testUserTopUpBalance() {
        User user = UserTest.user;

        when(userServiceFacade.topUpBalance(user, TOP_UP_BALANCE)).thenReturn(user);
        when(userRepo.changeBalance(17L, TOP_UP_BALANCE)).thenReturn(1);
        when(build.toDto(user)).thenReturn(UserTest.userDto);
        when(userRepo.getBalance(anyLong())).thenReturn(new BigDecimal(600));
        when(build.toModel(UserTest.userDto)).thenReturn(user);

        assertThat(userService.topUpBalance(user, TOP_UP_BALANCE)).isNotNull();
        assertThat(userService.topUpBalance(user, TOP_UP_BALANCE).getBalance()).isNotEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void testUserBuyExpo() {
        when(expoRepo.save(exposition)).thenReturn(exposition);
        when(userRepo.save(UserTest.user)).thenReturn(UserTest.user);
        when(userServiceFacade.buyExposition(exposition, UserTest.user)).thenReturn(true);

        assertThat(userService.buyExpo(UserTest.user, exposition)).isTrue();
    }

    @Test
    public void testGetUserExpos() {
        when((buildExpo.toDto(any()))).thenReturn(ExpoTest.expoDto1);

        Page<ExpoDto> userExpos = userService.getUserExpos(new PageImpl<>(UserTest.user.getExpos()));

        assertThat(userExpos).isNotNull();
        assertThat(userExpos.getSize()).isEqualTo(1);
    }

    @Test
    public void testGetAllExposByStatusIdAndUser() {
        when(expoRepo.findExpositionsByUsersAndStatusId(UserTest.user, 1, PageRequest.of(0, 3)))
                .thenReturn(new PageImpl<>(UserTest.user.getExpos()));

        Page<Exposition> result = userService.getAllExposByStatusIdAndUser(1, UserTest.user, PageRequest.of(0, 3));

        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(1);
    }

}