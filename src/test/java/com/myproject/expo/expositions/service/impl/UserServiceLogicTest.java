package com.myproject.expo.expositions.service.impl;

import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.dto.UserDto;
import com.myproject.expo.expositions.entity.*;
import com.myproject.expo.expositions.generator.TestEntity;
import com.myproject.expo.expositions.repository.ExpoRepo;
import com.myproject.expo.expositions.repository.UserRepo;
import com.myproject.expo.expositions.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceLogicTest {
    private static final BigDecimal TOP_UP_BALANCE = new BigDecimal(100);
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
    private Build<ExpoDto,Exposition> buildExpo;
    @MockBean
    private Pageable pageable;
    @MockBean
    @Qualifier("expoBuild")
    private Build<ExpoDto, Exposition> buildExpos;
    @Autowired
    private UserService userService;
    private final Exposition expo = new Exposition();


    @Test
    public void findByEmail() {
        when(userRepo.findByEmail("some@gmail.com")).thenReturn(TestEntity.UserTest.user);
        Assert.assertEquals(10L, userService.findByEmail("some@gmail.com").getId(), 0.00001);
    }

    @Test
    public void save() {
        when(build.toModel(TestEntity.UserTest.userDto)).thenReturn(TestEntity.UserTest.saveUser);
        when(passwordEncoder.encode(anyString())).thenReturn(anyString());
        when(userRepo.save(TestEntity.UserTest.saveUser)).thenReturn(TestEntity.UserTest.saveUser);
        Assert.assertEquals(12L, userService.save(TestEntity.UserTest.userDto).getId(), 0.00001);
    }

    @Test
    public void getAll() {
        when(userRepo.findAll(pageable)).thenReturn(Page.empty(pageable));
        Assert.assertEquals(0, userService.getAll(pageable).getSize());
    }

    @Test
    public void blockUnblock() {
        when(userRepo.changeStatus(1L, 12L)).thenReturn(1);
        Assert.assertTrue(userService.blockUnblock(12L, "active"));
    }

    @Test
    public void topUpBalance() {
        when(userRepo.changeBalance(10L, TOP_UP_BALANCE)).thenReturn(1);
        when(build.toDto(TestEntity.UserTest.user)).thenReturn(TestEntity.UserTest.userDto);
        when(userRepo.getBalance(10L)).thenReturn(TestEntity.UserTest.NEW_BALANCE);
        when(build.toModel(TestEntity.UserTest.userDto)).thenReturn(TestEntity.UserTest.user);

        Assert.assertEquals(TestEntity.UserTest.NEW_BALANCE, userService.topUpBalance(TestEntity.UserTest.user, TOP_UP_BALANCE).getBalance());

    }

    @Test
    public void buyExpo() {
        expo.setIdExpo(1L);
        expo.setPrice(new BigDecimal(300));
        Statistic statistic = new Statistic();
        statistic.setId(1L);
        statistic.setSold(10L);
        statistic.setTickets(300L);
        expo.setStatistic(statistic);
        expo.setStatusId(1);


        when(expoRepo.getById(1L)).thenReturn(expo);
        TestEntity.UserTest.user.setExposForUser(expo);
        when(expoRepo.save(expo)).thenReturn(expo);
        when(userRepo.save(TestEntity.UserTest.user)).thenReturn(TestEntity.UserTest.user);

        Assert.assertTrue(userService.buyExpo(TestEntity.UserTest.user,1L));
    }

    @Test
    public void getUserExpos() {
        List<Exposition> list = new ArrayList<>();
        List<ExpoDto> list2 = new ArrayList<>();
        list.add(expo);
        list2.add(buildExpo.toDto(expo));
        Page<Exposition> page = new PageImpl<>(list);
        Page<ExpoDto> page2 = new PageImpl<>(list2);
        when(expoRepo.getAllByStatusIdAndUsers(1,TestEntity.UserTest.user,pageable)).thenReturn(page);
        when(page.map(buildExpo::toDto)).thenReturn(page2);
        System.out.println(userService.getUserExpos(1,2,1,TestEntity.UserTest.user));
    }
}