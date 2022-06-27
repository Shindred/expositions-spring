package com.myproject.expo.expositions.dto;

import lombok.*;

import javax.validation.constraints.Pattern;

@Data
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ThemeDto implements DTO{
    private Long id;
    @Pattern(regexp = "^[a-zA-Z]+$",message = "{err.input}")
    private String name;


}
