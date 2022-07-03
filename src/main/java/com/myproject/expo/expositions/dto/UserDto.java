package com.myproject.expo.expositions.dto;

import com.myproject.expo.expositions.entity.Role;
import com.myproject.expo.expositions.entity.Status;
import com.myproject.expo.expositions.entity.User;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
@Validated
public class UserDto implements DTO {
    private Long id;
    @NotEmpty(message = "The input name is invalid")
    @Size(min = 3, max = 25)
    private String name;
    @NotEmpty(message = "The input surname is invalid")
    @Size(min = 2, max = 40)
    private String surname;
    @NotEmpty()
    @Email(message = "Please enter the proper email address")
    private String email;
    @NotEmpty()
    @Size(min = 3, max = 16, message = "Password has to be at least 3 to 25 signs")
    private String password;
    @NotEmpty()
    @Pattern(regexp = "^[+]?[\\d]{7,14}$", message = "Phone input has to be ar least 3 to 25 signs")
    private String phone;
    private BigDecimal balance;
    private Set<Role> roles;
    private Long status;

}
