package com.myproject.expo.expositions.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidateInputTest {
    private static final LocalDate LOCAL_DATE = LocalDate.of(2022, Month.AUGUST, 27);
    private static final LocalTime LOCAL_TIME = LocalTime.of(13, 30);
    @Autowired
    private Validate validate;

    @Test
    public void isProperDateTimeInput() {
        assertTrue(validate.isProperDateTimeInput(LOCAL_DATE, LOCAL_TIME));
    }

    @Test
    public void isTimeInReqDiapason() {
        assertTrue(validate.isTimeInReqDiapason(LOCAL_TIME));
    }

    @Test
    public void isDateValid() {
        assertTrue(validate.isDateValid(LOCAL_DATE));
    }

    @Test
    public void validateProperDate() {
        assertTrue(validate.validateProperDate(LOCAL_DATE));
    }

    @Test
    public void validateProperTime() {
        assertTrue(validate.validateProperTime(LOCAL_TIME));
    }
}