package com.myproject.expo.expositions.generator;

import com.myproject.expo.expositions.dto.HallDto;
import com.myproject.expo.expositions.dto.ThemeDto;
import com.myproject.expo.expositions.dto.UserDto;
import com.myproject.expo.expositions.entity.*;

import java.math.BigDecimal;
import java.util.Collections;

public  class TestEntity {
    public static class UserTest{
        public static BigDecimal NEW_BALANCE = new BigDecimal(1100);
        public static User user = User.builder()
                .id(10L)
                .email("some@gmail.com")
                .balance(NEW_BALANCE)
                .status(1L)
                .build();
        public static User saveUser = User.builder()
                .id(12L)
                .email("some@gmail.com")
                .password("1234")
                .balance(new BigDecimal(1000))
                .roles(Collections.singleton(Role.USER))
                .status(Status.ACTIVE.getStatusId())
                .build();
        public static UserDto userDto = UserDto.builder()
                .email("some@gmail.com")
                .password("1234")
                .balance(NEW_BALANCE)
                .roles(Collections.singleton(Role.USER))
                .status(Status.ACTIVE.getStatusId())
                .build();
    }

    public static class HallTest{
        public static Hall hall1 = Hall.builder().idHall(1L).name("AA1").build();
        public static Hall hall2 = Hall.builder().idHall(2L).name("GH3").build();
        public static HallDto hallDto = new HallDto(1L,"AA1");
    }

    public static class ThemeTest{
        public static Theme theme1 = new Theme(1L, "weather");
        public static Theme theme2 = new Theme(2L, "books");
        public static ThemeDto themeDto = new ThemeDto(1L, "weather");
    }
}
