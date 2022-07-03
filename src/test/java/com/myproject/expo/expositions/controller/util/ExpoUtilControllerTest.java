package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.TestRunner;
import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.build.ExpoBuild;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.generator.TestEntity;
import com.myproject.expo.expositions.service.ExpoService;
import com.myproject.expo.expositions.service.HallService;
import com.myproject.expo.expositions.service.ThemeService;
import com.myproject.expo.expositions.validator.Validate;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.myproject.expo.expositions.generator.TestEntity.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ExpoUtilControllerTest extends TestRunner {
    @MockBean
    private ExpoService expoService;
    @Mock
    private HallService hallService;
    @Mock
    private ThemeService themeService;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private Model model;
    @Autowired
    @Qualifier("expoBuild")
    private Build<ExpoDto, Exposition> build;
    @InjectMocks
    private ExpoUtilController expoUtilController;

    @Test
    public void getPageToAddExpo() {
        when(hallService.getAll()).thenReturn(List.of(HallTest.hall1, HallTest.hall2));
        when(themeService.getAll()).thenReturn(List.of(ThemeTest.theme1, ThemeTest.theme2));
        assertThat(expoUtilController.getPageToAddExpo(model)).isNotEmpty();
    }

    @Test
    public void addExpo() {
        //TODO FIX ME
        ExpoDto expoDto1 = ExpoTest.expoDto1;
      //  Build<ExpoDto, Exposition> build = new ExpoBuild();
//        when(expoService.getAll()).thenReturn(List.of(build.toModel(expoDto1)));
        when(expoService.addExpo(expoDto1, List.of(1L, 2L))).thenReturn(build.toModel(expoDto1));
   //     when(build.toModel(expoDto1)).thenReturn(build.toModel(expoDto1));
        assertThat(expoUtilController.addExpo(expoDto1, bindingResult, model)).isNotEmpty();
    }

    @Test
    public void getAllHalls() {
        when(hallService.getAll()).thenReturn(List.of(HallTest.hall1, HallTest.hall2));
        assertThat(expoUtilController.getAllHalls()).hasSizeGreaterThan(0);

    }

    @Test
    public void getAllThemes() {
        when(themeService.getAll()).thenReturn(List.of(ThemeTest.theme1, ThemeTest.theme2));
        assertThat(expoUtilController.getAllThemes()).hasSizeGreaterThan(0);
    }

}