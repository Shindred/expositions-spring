package com.myproject.expo.expositions.repository;

import com.myproject.expo.expositions.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Modifying
    @Transactional
    @Query("update User u set u.status = :status where u.id = :id")
    int changeStatus(@Param("status") Long status, @Param("id") Long id);

    @Query("select balance from User where id= :id")
    BigDecimal getBalance(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("update User u set u.balance = :amount where u.id= :id")
    int changeBalance(Long id, BigDecimal amount);

    @Modifying
    @Transactional
    @Query("update User u set u.email = :newEmail where u.email=:oldEmail")
    int changeEmail(String oldEmail,String newEmail);

    @Modifying
    @Transactional
    @Query("update User u set u.password=:password where u.email=:email")
    int changePassword(String email,String password);

    User getByEmail(String email);

}
