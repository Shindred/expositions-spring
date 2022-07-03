package com.myproject.expo.expositions;

import com.myproject.expo.expositions.generator.TestEntity;
import com.myproject.expo.expositions.validator.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRunner {
    @Autowired
    private Validate validate;

    @Test
    public void validateHallNotEmpty() {
        assertFalse(validate.validateHallNotEmpty(TestEntity.ExpoTest.expoDto1));
    }
}
