package com.myproject.expo.expositions.service.impl;

import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.dto.UserDto;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.entity.Status;
import com.myproject.expo.expositions.entity.User;
import com.myproject.expo.expositions.exception.custom.ExpoException;
import com.myproject.expo.expositions.exception.custom.UserException;
import com.myproject.expo.expositions.repository.ExpoRepo;
import com.myproject.expo.expositions.repository.UserRepo;
import com.myproject.expo.expositions.service.UserService;
import com.myproject.expo.expositions.service.facade.UserServiceFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
@Slf4j
public class UserServiceLogic implements UserService {
    private final UserRepo userRepo;
    private final ExpoRepo expoRepo;
    private final Build<ExpoDto, Exposition> buildExpos;
    private final UserServiceFacade userServiceFacade;

    @Autowired
    public UserServiceLogic(UserRepo userRepo, ExpoRepo expoRepo,
                            @Qualifier("expoBuild") Build<ExpoDto, Exposition> buildExpos,
                            UserServiceFacade userServiceFacade) {
        this.userRepo = userRepo;
        this.expoRepo = expoRepo;
        this.buildExpos = buildExpos;
        this.userServiceFacade = userServiceFacade;
    }

    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User save(UserDto userDto) {
        return userServiceFacade.save(userDto);
    }

    @Override
    public Page<UserDto> getAll(Pageable pageable) {
        return userServiceFacade.getAll(pageable);
    }

    @Override
    public boolean blockUnblock(Long id, String status) {
        Long statusId = Arrays.stream(Status.values())
                .filter(elem -> elem.getName().equals(status))
                .mapToLong(Status::getStatusId)
                .findFirst()
                .orElse(1);
        int res = userRepo.changeStatus(statusId, id);
        return res > 0;
    }

    @Override
    public User topUpBalance(User user, BigDecimal amount) {
        return userServiceFacade.topUpBalance(user, amount);
    }

    @Transactional
    @Override
    public boolean buyExpo(User user, Long id) {
        Exposition expo = expoRepo.getById(id);
        checkCaseExpoStatusCanceledUserBalanceNotOk(user, expo);
        try{
            userServiceFacade.buyExposition(expo, user);
        }catch (Exception e){
            throw new ExpoException("err.buy_expo");
        }
        return true;
    }

    private void checkCaseExpoStatusCanceledUserBalanceNotOk(User user, Exposition expo) {
        if (Status.CANCELED.getStatusId() == expo.getStatusId()) {
            throw new UserException("err.canceled_expo");
        }
        if (user.getBalance().compareTo(expo.getPrice()) < 0) {
            throw new UserException("err.not_enough_balance");
        }
    }

    @Override
    public Page<ExpoDto> getUserExpos(int offset, int size, Integer statusId, User user) {
        Pageable pageable = PageRequest.of(offset, size);
        Page<Exposition> resList = expoRepo.getAllByStatusIdAndUsers(statusId, user, pageable);
        if (resList.getSize() == 0) {
            throw new UserException("err.user_dont_have_expos");
        }
        return resList.map(buildExpos::toDto);
    }

    @Override
    public int changeEmail(String oldEmail, String newEmail) {
        int res = userRepo.changeEmail(oldEmail, newEmail);
        if (res <= 0) {
            throw new UserException("err.change_user_email");
        }
        return res;
    }

    @Override
    public User getByEmail(String email) {
        User userByEmail = userRepo.getByEmail(email);
        if (userByEmail == null) {
            throw new UserException("err.no_such_user");
        }
        return userByEmail;

    }
}
