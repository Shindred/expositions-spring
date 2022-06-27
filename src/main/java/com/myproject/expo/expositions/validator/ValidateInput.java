package com.myproject.expo.expositions.validator;

import com.myproject.expo.expositions.dto.ExpoDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateInput implements Validate {
    private final String TIME_CHECK = "^(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$";
    private final String DATE_CHECK = "^([0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]))$";

    @Override
    public boolean isProperDateTimeInput(LocalDate date, LocalTime time) {
        Pattern patternDate = Pattern.compile(DATE_CHECK);
        Matcher matcherDate = patternDate.matcher(date.toString());
        Pattern patternTime = Pattern.compile(TIME_CHECK);
        Matcher matcherTime = patternTime.matcher(time.toString());
        return matcherDate.matches() && matcherTime.matches();
    }

    @Override
    public boolean isTimeInReqDiapason(LocalTime time) {
        LocalTime fromTime = LocalTime.of(9, 00);
        LocalTime toTime = LocalTime.of(21, 30);
        return (time.isAfter(fromTime) && time.isBefore(toTime));
    }

    @Override
    public boolean isDateValid(LocalDate date) {
        return date.isEqual(LocalDate.now()) || date.isAfter(LocalDate.now());
    }

    @Override
    public boolean validateProperDate(LocalDate date)  {
        if (date.isEqual(LocalDate.now()) || date.isAfter(LocalDate.now())) {
            return true;
        }
        throw new RuntimeException("{err.date_input}");
    }

    @Override
    public boolean validateProperTime(LocalTime time)  {
        LocalTime fromTime = LocalTime.of(9, 0);
        LocalTime toTime = LocalTime.of(21, 30);
        if (time.isAfter(fromTime) || time.isBefore(toTime)) {
            return true;
        }
        throw new RuntimeException("{err.time_input}");
    }

    @Override
    public boolean validateThemeHasIdFromInput(ExpoDto expoDto){
        return expoDto.getTheme().getIdTheme() !=0;
    }

    @Override
    public boolean validateHallNotEmpty(ExpoDto expoDto) {
        return expoDto.getHalls().size() == 0;
    }
}
