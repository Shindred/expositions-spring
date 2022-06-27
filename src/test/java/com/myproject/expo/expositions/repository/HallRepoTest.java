package com.myproject.expo.expositions.repository;

import com.myproject.expo.expositions.entity.Hall;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class HallRepoTest {
     @Autowired
    private HallRepo hallRepo;

    @Test
    public void findAll() {
        Page<Hall> all = hallRepo.findAll(PageRequest.of(1, 5));
        System.out.println(all);

    }
}