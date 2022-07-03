package com.myproject.expo.expositions.util;

import com.myproject.expo.expositions.controller.util.ControllerUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;

/**
 * The TimeConvert additional class for time converting
 */
public class TimeConvert implements Converter<String, LocalTime>, ControllerUtils {
    @Override
    public LocalTime convert(String source) {
        return parseStrToLocalTime(source);
    }
}
