package com.myproject.expo.expositions.service.facade;

import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.UserDto;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.entity.User;
import com.myproject.expo.expositions.exception.custom.UserException;
import com.myproject.expo.expositions.repository.ExpoRepo;
import com.myproject.expo.expositions.repository.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UserServiceFacade {
    private static final Logger log = LogManager.getLogger(UserServiceFacade.class);
    private final UserRepo userRepo;
    private final ExpoRepo expoRepo;
    private final PasswordEncoder passwordEncoder;
    private final Build<UserDto, User> build;

    @Autowired
    public UserServiceFacade(UserRepo userRepo, ExpoRepo expoRepo,
                             PasswordEncoder passwordEncoder, @Qualifier("userBuild") Build<UserDto, User> build) {
        this.userRepo = userRepo;
        this.expoRepo = expoRepo;
        this.passwordEncoder = passwordEncoder;
        this.build = build;
    }

    public boolean buyExposition(Exposition expo, User user) {
        expo.getStatistic().setSold(expo.getStatistic().getSold() + 1);
        expo.getStatistic().setTickets(expo.getStatistic().getTickets() - 1);
        user.setExposForUser(expo);
        expoRepo.save(expo);
        user.setBalance(user.getBalance().subtract(expo.getPrice()));
        userRepo.save(user);
        return true;
    }

    public User save(UserDto userDto) {
        User user = build.toModel(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepo.save(user);
        } catch (RuntimeException e) {
            log.warn("User duplicate entry email");
            throw new UserException("err.user_exists_already");
        }
        return user;
    }

    public User topUpBalance(User user, BigDecimal amount) {
        BigDecimal resUserBalance = user.getBalance().add(amount);
        userRepo.changeBalance(user.getId(), resUserBalance);
        UserDto userDto = build.toDto(user);
        BigDecimal balance = userRepo.getBalance(user.getId());
        userDto.setBalance(balance);
        return build.toModel(userDto);
    }

    public Page<UserDto> getAll(Pageable pageable) {
        Page<User> all = userRepo.findAll(pageable);
        return all.map(build::toDto);
    }
}
