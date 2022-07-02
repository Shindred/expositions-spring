package com.myproject.expo.expositions.build;

import com.myproject.expo.expositions.dto.UserDto;
import com.myproject.expo.expositions.entity.Role;
import com.myproject.expo.expositions.entity.Status;
import com.myproject.expo.expositions.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * The UserBuild class converts User DTO to entity and vice versa
 */
@Component
public class UserBuild implements Build<UserDto, User> {
    @Override
    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .balance(user.getBalance())
                .phone(user.getPhone())
                .roles(user.getRoles())
                .status(user.getStatus())
                .build();
    }

    @Override
    public User toModel(UserDto userDto) {
       return User.builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .phone(userDto.getPhone())
                .balance(userDto.getBalance())
                .roles(Collections.singleton(Role.USER))
                .status(Status.ACTIVE.getStatusId())
                .build();
    }
}
