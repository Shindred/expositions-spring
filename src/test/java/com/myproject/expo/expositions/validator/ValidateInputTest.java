package com.myproject.expo.expositions.validator;

import com.myproject.expo.expositions.TestRunner;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.Theme;
import com.myproject.expo.expositions.generator.TestEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidateInputTest extends TestRunner {
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

    @Test
    public void validateThemeHasIdFromInput() {
        ExpoDto expoDto = ExpoDto.builder().theme(new Theme(12L, "name")).build();
        assertTrue(validate.validateThemeHasIdFromInput(expoDto));
    }

    @Test
    public void validateHallNotEmpty() {
        assertFalse(validate.validateHallNotEmpty(TestEntity.ExpoTest.expoDto1));
    }
}