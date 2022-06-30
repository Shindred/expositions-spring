package com.myproject.expo.expositions.util;

import com.myproject.expo.expositions.controller.util.ControllerUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

public class DateConvert implements Converter<String, LocalDate>, ControllerUtils {
    @Override
    public LocalDate convert(String source) {
        return parseStrToLocalDate(source);
    }
}
