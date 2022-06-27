package com.myproject.expo.expositions.dto;

import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.entity.Hall;
import com.myproject.expo.expositions.entity.Statistic;
import com.myproject.expo.expositions.entity.Theme;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    private static final String CHECK_DATE_TIME_INPUT = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
    private final String TIME_CHECK = "^(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$";
    private final String DATE_CHECK = "^([0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]))$";

    private Long id;
    @NotEmpty(message = "Please enter the expo name")
    private String name;
    @NotNull(message = "{err.date_input}")
    private LocalDate expoDate;
    //@Pattern(regexp = "^(\\d{1,2}/\\d{1,2}/\\d{2,4})|(\\d{1,2}.\\d{1,2}.\\d{2,4})$",message = "{err.date_input}")
    @NotNull(message = "{err.time_input}")
    private LocalTime expoTime;
  //  @Pattern(regexp = "^(\\d{1,2}:\\d{2}\\W[A-Z]{2})|(\\d{2}:\\d{2})$",message = "{err.time_input}")
   // @Pattern(regexp = "\\d+\\.\\d{2}",message = "{err.price_input}")
    @NotNull(message = "{err.price_input}")
    @Digits(integer = 6,fraction = 2)
    private BigDecimal price;
    @NotNull
    private Statistic statistic;
    @NotNull
    private Theme theme;
    private int statusId;
    @NotNull(message = "{err.enter_hall_for_expo}")
    private Set<Hall> halls;
    private Long sold;
    private Long tickets;
    private Long idTheme;
    private String themeName;
    private ExpoDto expoDto;

    @Override
    public String toString() {
        return "ExpoDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", expoDate=" + expoDate +
                ", expoTime=" + expoTime +
                ", price=" + price +
                ", statistic=" + statistic +
                ", theme=" + theme +
                ", statusId=" + statusId +
                ", halls=" + halls +
                '}';
    }
}
