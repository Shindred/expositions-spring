package com.myproject.expo.expositions.validator;

import com.myproject.expo.expositions.dto.ExpoDto;

import java.time.LocalDate;
import java.time.LocalTime;

public interface Validate {
    boolean isProperDateTimeInput(LocalDate date, LocalTime time);

    boolean isTimeInReqDiapason(LocalTime time);

    boolean isDateValid(LocalDate date);

    boolean validateProperDate(LocalDate date);

    boolean validateProperTime(LocalTime time);

    boolean validateThemeHasIdFromInput(ExpoDto expoDto);

    boolean validateHallNotEmpty(ExpoDto expoDto);

}
