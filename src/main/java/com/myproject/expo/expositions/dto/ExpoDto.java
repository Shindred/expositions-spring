package com.myproject.expo.expositions.dto;

import com.myproject.expo.expositions.entity.Hall;
import com.myproject.expo.expositions.entity.Statistic;
import com.myproject.expo.expositions.entity.Theme;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ExpoDto implements DTO {
    private static final String TIME_CHECK = "^(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$";
    private static final String DATE_CHECK = "^([0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]))$";
    private static final String PRICE_REGEX = "\\d+\\.\\d{2}";
    private static final String ONLY_DIGITS = "^\\d+$";
    private static final String REGEX_ONLY_WORDS = "^(\\p{L}+){3,17}$";

    private Long id;
    @Pattern(regexp = REGEX_ONLY_WORDS, message = "{err.expo_name}")
    private String name;
    private LocalDate expoDate;
    @Pattern(regexp = DATE_CHECK, message = "{err.date_input}")
    private String expoDateStr;
    private LocalTime expoTime;
    @Pattern(regexp = TIME_CHECK, message = "{err.time_input}")
    private String expoTimeStr;
    @NotNull(message = "{err.price_input}")
    @Digits(integer = 6, fraction = 2, message = "{err.price_input}")
    private BigDecimal price;
    @Valid
    private Statistic statistic;
    @NotNull
    private Theme theme;
    private int statusId;
    @NotNull(message = "{err.enter_hall_for_expo}")
    private Set<Hall> halls;
    private Long sold;
    private Long tickets;

    @Override
    public String toString() {
        return "ExpoDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", expoDate=" + expoDate +
                ", expoDateStr='" + expoDateStr + '\'' +
                ", expoTime=" + expoTime +
                ", expoTimeStr='" + expoTimeStr + '\'' +
                ", price=" + price +
                ", statistic=" + statistic +
                ", theme=" + theme +
                ", statusId=" + statusId +
                ", halls=" + halls +
                ", sold=" + sold +
                ", tickets=" + tickets +
                '}';
    }
}
