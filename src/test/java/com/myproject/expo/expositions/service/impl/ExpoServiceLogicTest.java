package com.myproject.expo.expositions.service.impl;

import com.myproject.expo.expositions.TestRunner;
import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.entity.Statistic;
import com.myproject.expo.expositions.repository.ExpoRepo;
import com.myproject.expo.expositions.service.ThemeService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.myproject.expo.expositions.generator.EntityStorage.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

public class ExpoServiceLogicTest extends TestRunner {
    private static final Pageable PAGEABLE = PageRequest.of(0, 4, Sort.by("idExpo"));
    private final List<Exposition> exposList = new ArrayList<>();
    private final Exposition exposition = new Exposition();
    @Autowired
    private ExpoServiceImpl expoService;
    @Autowired
    @Qualifier("expoBuild")
    private Build<ExpoDto, Exposition> buildExpo;
    @MockBean
    private ExpoRepo expoRepo;
    @MockBean
    private ThemeService themeService;
    @MockBean
    @Qualifier("expoBuild")
    private Build<ExpoDto, Exposition> build;

    @Before
    public void init() {
        exposList.add(buildExpo.toModel(ExpoTest.expoDto1));
        exposList.add(buildExpo.toModel(ExpoTest.expoDto2));
        initializeExpo();

    }

    private void initializeExpo() {
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
    public void testGetAllExposWithPageable() {
        when(expoRepo.findAll(PAGEABLE)).thenReturn(new PageImpl<>(exposList));

        assertThat(expoService.getAll(PAGEABLE)).hasSize(2);
    }

    @Test
    public void testGetAllExposWithoutPageable() {
        when(expoRepo.findAll()).thenReturn(exposList);

        assertThat(expoService.getAll()).hasSize(2);
    }

    @Test
    public void testGetExpositionById() {
        when(build.toDto(exposList.get(0))).thenReturn(ExpoTest.expoDto1);
        when(expoRepo.getById(17L)).thenReturn(exposList.get(0));

        assertThat(expoService.getById(17L)).isNotNull();
    }

    @Test
    public void testUpdateExpo() {
        when(build.toModel(ExpoTest.expoDto1)).thenReturn(exposition);
        when(themeService.getById(anyLong())).thenReturn(ThemeTest.theme1);
        when(expoRepo.save(exposition)).thenReturn(exposition);

        assertThat(expoService.update(17L, exposition)).isTrue();
    }

    @Test
    public void testsChangeExpoStatus() {
        when(expoRepo.changeStatus(1, 26L)).thenReturn(1);

        assertThat(expoService.changeStatus(26L, 1)).isTrue();
    }

    @Test
    public void testAddExposition() {
        when(build.toModel(ExpoTest.expoDto1)).thenReturn(exposition);
        when(expoRepo.save(exposition)).thenReturn(exposition);

        assertThat(expoService.addExpo(ExpoTest.expoDto1, new ArrayList<>()).getIdExpo()).isNotEqualTo(0);

    }
}