package com.myproject.expo.expositions.service.impl;

import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.entity.Statistic;
import com.myproject.expo.expositions.entity.Theme;
import com.myproject.expo.expositions.generator.TestEntity;
import com.myproject.expo.expositions.repository.ExpoRepo;
import com.myproject.expo.expositions.service.ThemeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.text.html.parser.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExpoServiceLogicTest {
    private static final Pageable PAGEABLE = PageRequest.of(0, 4, Sort.by("idExpo"));
    @Autowired
    private ExpoServiceLogic expoService;
    @Autowired
    @Qualifier("expoBuild")
    private Build<ExpoDto, Exposition> buildExpo;
    @MockBean
    private ExpoRepo expoRepo;
    @MockBean
    private ThemeService themeService;
    @MockBean
    private Statistic statistic;
    @MockBean
    private Exposition expo;
    @MockBean
    private ExpoDto expoDto;

    @MockBean
    @Qualifier("expoBuild")
    private Build<ExpoDto, Exposition> build;
    private final List<Exposition> exposList = new ArrayList<>();
    private final ExpoDto expoDto1 = ExpoDto.builder()
            .id(17L)
            .name("sky-champions")
            .expoDate(LocalDate.of(2022, Month.AUGUST, 12))
            .expoTime(LocalTime.of(13, 30))
            .price(new BigDecimal(300))
            .statusId(1)
            .statistic(new Statistic(1L, 20L, 450L))
            .halls(Collections.singleton(TestEntity.HallTest.hall1))
            .theme(TestEntity.ThemeTest.theme1)
            .build();

    private final ExpoDto expoDto2 = ExpoDto.builder()
            .id(26L)
            .name("junit-testing")
            .expoDate(LocalDate.of(2022, Month.DECEMBER, 25))
            .expoTime(LocalTime.of(15, 30))
            .price(new BigDecimal(400))
            .statusId(1)
            .statistic(new Statistic(2L, 300L, 700L))
            .halls(Collections.singleton(TestEntity.HallTest.hall2))
            .theme(TestEntity.ThemeTest.theme2)
            .build();

      private Exposition exposition = new Exposition();

    @Before
    public void init() {
        exposList.add(buildExpo.toModel(expoDto1));
        exposList.add(buildExpo.toModel(expoDto2));

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
    public void testGetAllExposWithPageable() {
        when(expoRepo.findAll(PAGEABLE)).thenReturn(new PageImpl<>(exposList));
        Assertions.assertEquals(2, expoService.getAll(PAGEABLE).getSize());
    }

    @Test
    public void testGetAllExposWithoutPageable() {
        when(expoRepo.findAll()).thenReturn(exposList);
        Assertions.assertEquals(2,expoService.getAll().size());
    }

    @Test
    public void testGetExpositionById() {
        when(build.toDto(exposList.get(0))).thenReturn(expoDto1);
        when(expoRepo.getById(17L)).thenReturn(exposList.get(0));

    }

    @Test
    public void testUpdateExpo() {
        when(build.toModel(expoDto1)).thenReturn(exposition);
        when(themeService.getById(anyLong())).thenReturn(TestEntity.ThemeTest.theme1);
        when(expoRepo.save(exposition)).thenReturn(exposition);
        Assertions.assertTrue(expoService.update(17L,exposition));
    }

    @Test
    public void testsChangeExpoStatus() {
        when(expoRepo.changeStatus(1,26L)).thenReturn(1);
        Assertions.assertTrue(expoService.changeStatus(26L,1));
    }

    @Test
    public void testAddExposition() {
        //when(themeService.getById(anyLong())).thenReturn(TestEntity.ThemeTest.theme1);
        //when(build.toModel(expoDto1)).thenReturn(exposList.get(0));
        when(expoRepo.save(exposList.get(0))).thenReturn(exposList.get(0));
        verify(expoRepo).save(exposList.get(0));
        Exposition exposition = expoService.addExpo(expoDto1, List.of(1L, 2L));
        System.out.println(exposition);

        //Assertions.assertEquals("sky-champions",);

    }
}