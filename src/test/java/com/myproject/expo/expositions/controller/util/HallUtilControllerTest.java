package com.myproject.expo.expositions.controller.util;

import com.myproject.expo.expositions.generator.TestEntity;
import com.myproject.expo.expositions.service.HallService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HallUtilControllerTest {
    private static final int PAGE = 0;
    private static final int SIZE = 1;
    @Autowired
    private HallUtilController hallUtilController;
    @Mock
    private HallService hallService;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private Model model;

    @Test
    public void getHalls() {
        when(hallService.getHalls(PageRequest.of(PAGE, SIZE)))
                .thenReturn(new PageImpl<>(Collections.singletonList(TestEntity.HallTest.hall1)));
        assertThat(hallUtilController.getHalls(PageRequest.of(PAGE, SIZE))).isNotNull();
        assertThat(hallUtilController.getHalls(PageRequest.of(PAGE, SIZE))).hasSizeGreaterThan(0);
    }

    @Test
    public void getAllHalls() {
        when(hallService.getAll()).thenReturn(Collections.singletonList(TestEntity.HallTest.hall2));
        assertThat(hallUtilController.getAllHalls()).hasSizeGreaterThan(1);
    }

    @Test
    public void countTotalNoOfRequiredPages() {
        assertThat(hallUtilController.countTotalNoOfRequiredPages(
                List.of(TestEntity.HallTest.hall1, TestEntity.HallTest.hall2), PageRequest.of(PAGE, 2)
        )).isEqualTo(1);
    }

    @Test
    public void saveHall() {
        when(hallService.save(TestEntity.HallTest.hallDto)).thenReturn(TestEntity.HallTest.hall1);
        assertThat(hallUtilController.saveHall(TestEntity.HallTest.hallDto, bindingResult,
                model, PageRequest.of(PAGE, SIZE))).isNotNull();
    }

    @Test
    public void updateTheHall() {
        when(hallService.update(TestEntity.HallTest.hallDto)).thenReturn(1);
        assertThat(hallUtilController.updateTheHall(17L, TestEntity.HallTest.hallDto,
                bindingResult, model, PageRequest.of(PAGE, SIZE))).isNotEmpty();
    }

    @Test
    public void deleteTheHall() {
        when(hallService.delete(17L)).thenReturn(1);
        assertThat(hallUtilController.deleteTheHall(17L, model, PageRequest.of(PAGE, SIZE))).isNotEmpty();
    }
}