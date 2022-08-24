package com.myproject.expo.expositions.validator;

import com.myproject.expo.expositions.TestRunner;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.Theme;
import com.myproject.expo.expositions.generator.EntityStorage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidateInputTest extends TestRunner {
    private static final LocalDate LOCAL_DATE = LocalDate.of(2022, Month.AUGUST, 27);
    private static final LocalTime LOCAL_TIME = LocalTime.of(13, 30);
    @Autowired
    private Validate validate;

    @Test
    public void isProperDateTimeInput() {
        assertThat(validate.isProperDateTimeInput(LOCAL_DATE, LOCAL_TIME)).isTrue();
    }

    @Test
    public void isTimeInReqDiapason() {
        assertThat(validate.isTimeInReqDiapason(LOCAL_TIME)).isTrue();
    }

    @Test
    public void isDateValid() {
        assertThat(validate.isDateValid(LOCAL_DATE)).isTrue();
    }

    @Test
    public void validateProperDate() {
        assertThat(validate.validateProperDate(LOCAL_DATE)).isTrue();
    }

    @Test
    public void validateProperTime() {
        assertThat(validate.validateProperTime(LOCAL_TIME)).isTrue();
    }

    @Test
    public void validateThemeHasIdFromInput() {
        ExpoDto expoDto = ExpoDto.builder().theme(new Theme(12L, "name")).build();

        assertThat(validate.validateThemeHasIdFromInput(expoDto)).isTrue();
    }

    @Test
    public void validateHallNotEmpty() {
        assertThat(validate.validateHallNotEmpty(EntityStorage.ExpoTest.expoDto1)).isFalse();
    }
}