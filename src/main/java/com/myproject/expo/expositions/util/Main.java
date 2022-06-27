package com.myproject.expo.expositions.util;

import org.springframework.context.support.ResourceBundleMessageSource;

import java.time.LocalDate;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("resources");
       // System.out.println(messageSource.getMessage("Lang", null, new Locale("uk","UA")));
        System.out.println(LocalDate.now());
    }

}
