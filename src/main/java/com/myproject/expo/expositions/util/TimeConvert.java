package com.myproject.expo.expositions.util;

import com.myproject.expo.expositions.controller.util.ControllerUtil;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;

/**
 * The TimeConvert additional class for time converting
 */
public class TimeConvert implements Converter<String, LocalTime>, ControllerUtil {
    @Override
    public LocalTime convert(String source) {
        return parseStrToLocalTime(source);
    }
}
