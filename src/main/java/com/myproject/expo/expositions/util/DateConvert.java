package com.myproject.expo.expositions.util;

import com.myproject.expo.expositions.controller.util.ControllerUtil;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

/**
 * The DateConvert additional class for converting date
 */
public class DateConvert implements Converter<String, LocalDate>, ControllerUtil {
    @Override
    public LocalDate convert(String source) {
        return parseStrToLocalDate(source);
    }
}
