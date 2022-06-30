package com.myproject.expo.expositions.util;

import org.springframework.context.support.ResourceBundleMessageSource;

import java.time.LocalDate;
import java.util.Locale;

public class Main {
    private static final String TIME_CHECK = "^(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$";
    private static final String DATE_CHECK = "^([0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]))$";

    public static void main(String[] args) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("resources");
       // System.out.println(messageSource.getMessage("Lang", null, new Locale("uk","UA")));
        System.out.println(LocalDate.now());
        String date = "2022-05-13";
        String time ="13:00";

        System.out.println(date.matches(DATE_CHECK));
        System.out.println(time.matches(TIME_CHECK));
    }

}
