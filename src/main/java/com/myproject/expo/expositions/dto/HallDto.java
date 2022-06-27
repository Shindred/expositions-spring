package com.myproject.expo.expositions.dto;

import lombok.*;

import javax.validation.constraints.Pattern;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HallDto implements DTO{
    private Long id;
    @Pattern(regexp = "^[a-zA-Z]{1,3}[0-9]{1,3}$",message = "{err.input}")
    private String name;

}
