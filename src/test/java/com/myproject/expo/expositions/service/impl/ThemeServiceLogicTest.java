package com.myproject.expo.expositions.service.impl;

import com.myproject.expo.expositions.TestRunner;
import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.ThemeDto;
import com.myproject.expo.expositions.entity.Theme;
import com.myproject.expo.expositions.repository.ThemeRepo;
import org.junit.Before;
import org.junit.Test;
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

import static com.myproject.expo.expositions.generator.EntityStorage.ThemeTest;
import static org.assertj.core.api.Assertions.assertThat;
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
    private ThemeServiceImpl themeService;

    @Before
    public void init() {
        themeList.add(ThemeTest.theme1);
        themeList.add(ThemeTest.theme1);
    }

    @Test
    public void testGetAllThemesWithPageable() {
        when(themeRepo.findAll(PAGEABLE)).thenReturn(new PageImpl<>(themeList));

        assertThat(themeService.getAll(PAGEABLE).getSize()).isEqualTo(2);
    }

    @Test
    public void testGetAllThemesWithoutPageable() {
        when(themeRepo.findAll()).thenReturn(themeList);

        assertThat(themeService.getAll()).hasSize(2);
    }

    @Test
    public void testSaveTheme() {
        when(build.toModel(ThemeTest.themeDto)).thenReturn(ThemeTest.theme1);
        when(themeRepo.save(ThemeTest.theme1)).thenReturn(ThemeTest.theme1);

        assertThat(themeService.save(ThemeTest.themeDto)).isNotNull();
    }

    @Test
    public void testUpdateTheme() {
        when(build.toModel(ThemeTest.themeDto)).thenReturn(ThemeTest.theme2);
        when(themeRepo.save(ThemeTest.theme2)).thenReturn(ThemeTest.theme2);

        assertThat(themeService.update(ThemeTest.themeDto)).isEqualTo(1);
    }

    @Test
    public void testDeleteTheme() {
        doNothing().when(themeRepo).deleteById(1L);

        assertThat(themeService.delete(1L)).isEqualTo(1);
    }

    @Test
    public void testGetThemeById() {
        when(themeRepo.findById(2L)).thenReturn(Optional.ofNullable(ThemeTest.theme2));

        assertThat(themeService.getById(2L).getIdTheme()).isEqualTo(2L);

    }
}