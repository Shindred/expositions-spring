package com.myproject.expo.expositions.service.impl;

import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.HallDto;
import com.myproject.expo.expositions.entity.Hall;
import com.myproject.expo.expositions.generator.TestEntity;
import com.myproject.expo.expositions.repository.HallRepo;
import com.myproject.expo.expositions.service.HallService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HallServiceLogicTest {
    private static Pageable PAGEABLE = PageRequest.of(0, 5, Sort.by("idHall"));
    @MockBean
    private HallRepo hallRepo;
    @MockBean
    @Qualifier("hallBuild")
    private Build<HallDto,Hall> build;
    @Autowired
    private HallService hallservice;
    private final List<Hall> hallList = new ArrayList<>();

    @Before
    public void init(){
        hallList.add(TestEntity.HallTest.hall1);
        hallList.add(TestEntity.HallTest.hall2);
    }

    @Test
    public void testGetHallsWithPageable() {
        when(hallRepo.findAll(PAGEABLE)).thenReturn(new PageImpl<>(hallList));
        Assertions.assertEquals(2,hallservice.getHalls(PAGEABLE).getSize());
    }

    @Test
    public void testGetAllHallsWithoutPageable() {
        when(hallRepo.findAll()).thenReturn(hallList);
        Assertions.assertEquals(2,hallservice.getAll().size());
    }

    @Test
    public void testSaveHall() {
        when(build.toModel(TestEntity.HallTest.hallDto)).thenReturn(TestEntity.HallTest.hall1);
        when(hallRepo.save(TestEntity.HallTest.hall1)).thenReturn(TestEntity.HallTest.hall1);
        Assertions.assertEquals("AA1",hallservice.save(TestEntity.HallTest.hallDto).getName());
    }

    @Test
    public void testUpdateHall() {
        TestEntity.HallTest.hall1.setName("newName");
        when(hallRepo.save(TestEntity.HallTest.hall1)).thenReturn(TestEntity.HallTest.hall1);
        Assertions.assertEquals(1,hallservice.update(build.toDto(TestEntity.HallTest.hall1)));
    }

    @Test
    public void testDeleteHall() {
        doNothing().when(hallRepo).deleteById(1L);
        Assertions.assertEquals(1,hallservice.delete(1L));
    }
}