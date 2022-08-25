package com.myproject.expo.expositions.dto;

import lombok.*;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ThemeDto {
    private Long id;
    @Pattern(regexp = "^[a-zA-Z]+$",message = "{err.input}")
    private String name;

}
