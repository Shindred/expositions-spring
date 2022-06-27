package com.myproject.expo.expositions.generator;

import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.dto.HallDto;
import com.myproject.expo.expositions.dto.ThemeDto;
import com.myproject.expo.expositions.dto.UserDto;
import com.myproject.expo.expositions.entity.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Collections;

public  class TestEntity {
    public static class UserTest{
        public static BigDecimal NEW_BALANCE = new BigDecimal(1100);
        public static User user = User.builder()
                .id(10L)
                .email("some@gmail.com")
                .balance(NEW_BALANCE)
                .status(1L)
                .expos(Collections.singletonList(new Exposition(
                        10L,"sky-champions",LocalDate.of(2022, Month.AUGUST, 12),
                        LocalTime.of(13, 30),new BigDecimal(300),1,
                        Collections.singleton(HallTest.hall1),TestEntity.ThemeTest.theme1,
                        null,new Statistic(1L, 20L, 450L)
                )))
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
        public Exposition exposition = new Exposition();

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
    public static class ExpoTest{
        public static final  ExpoDto expoDto1 = ExpoDto.builder()
                .id(17L)
                .name("sky-champions")
                .expoDate(LocalDate.of(2022, Month.AUGUST, 12))
                .expoTime(LocalTime.of(13, 30))
                .price(new BigDecimal(300))
                .statusId(1)
                .statistic(new Statistic(1L, 20L, 450L))
                .halls(Collections.singleton(TestEntity.HallTest.hall1))
                .theme(TestEntity.ThemeTest.theme1)
                .build();

        public static final ExpoDto expoDto2 = ExpoDto.builder()
                .id(26L)
                .name("junit-testing")
                .expoDate(LocalDate.of(2022, Month.DECEMBER, 25))
                .expoTime(LocalTime.of(15, 30))
                .price(new BigDecimal(400))
                .statusId(1)
                .statistic(new Statistic(2L, 300L, 700L))
                .halls(Collections.singleton(TestEntity.HallTest.hall2))
                .theme(TestEntity.ThemeTest.theme2)
                .build();

    }
}
