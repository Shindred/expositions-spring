package com.myproject.expo.expositions.service.impl;

import com.myproject.expo.expositions.TestRunner;
import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.ThemeDto;
import com.myproject.expo.expositions.entity.Theme;
import com.myproject.expo.expositions.repository.ThemeRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.myproject.expo.expositions.generator.TestEntity.ThemeTest;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class ThemeServiceLogicTest extends TestRunner {
    private static final Pageable PAGEABLE = PageRequest.of(0, 3, Sort.by("idTheme"));
    private final List<Theme> themeList = new ArrayList<>();
    @MockBean
    private ThemeRepo themeRepo;
    @MockBean
    @Qualifier("themeBuild")
    private Build<ThemeDto, Theme> build;
    @Autowired
    private ThemeServiceLogic themeService;

    @Before
    public void init() {
        themeList.add(ThemeTest.theme1);
        themeList.add(ThemeTest.theme1);
    }

    @Test
    public void testGetAllThemesWithPageable() {
        when(themeRepo.findAll(PAGEABLE)).thenReturn(new PageImpl<>(themeList));
        Assertions.assertEquals(2, themeService.getAll(PAGEABLE).getSize());
    }

    @Test
    public void testGetAllThemesWithoutPageable() {
        when(themeRepo.findAll()).thenReturn(themeList);
        Assertions.assertEquals(2, themeService.getAll().size());
    }

    @Test
    public void testSaveTheme() {
        when(build.toModel(ThemeTest.themeDto)).thenReturn(ThemeTest.theme1);
        when(themeRepo.save(ThemeTest.theme1)).thenReturn(ThemeTest.theme1);
        Assertions.assertNotNull(themeService.save(ThemeTest.themeDto));
    }

    @Test
    public void testUpdateTheme() {
        when(build.toModel(ThemeTest.themeDto)).thenReturn(ThemeTest.theme2);
        when(themeRepo.save(ThemeTest.theme2)).thenReturn(ThemeTest.theme2);
        Assertions.assertEquals(1, themeService.update(ThemeTest.themeDto));
    }

    @Test
    public void testDeleteTheme() {
        doNothing().when(themeRepo).deleteById(1L);
        Assertions.assertEquals(1, themeService.delete(1L));
    }

    @Test
    public void testGetThemeById() {
        when(themeRepo.findById(2L)).thenReturn(Optional.ofNullable(ThemeTest.theme2));
        assertEquals(2L, themeService.getById(2L).getIdTheme(), 0.00001);

    }
}