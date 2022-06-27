package com.myproject.expo.expositions.service;

import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.dto.UserDto;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface UserService extends GetAll<User, UserDto>, Savable<User, UserDto> {

    User findByEmail(String email);

    boolean blockUnblock(Long id, String status);

    User topUpBalance(User user, BigDecimal amount);

    boolean buyExpo(User user, Exposition expo);

    Page<ExpoDto> getUserExpos(Page<Exposition> resList);

    int changeEmail(String oldEmail, String newEmail);

    User getByEmail(String email);

    Page<Exposition> getAllExposByStatusIdAndUser(Integer statusId, User user, Pageable pageable);

    Exposition getExpoById(Long id);
}
