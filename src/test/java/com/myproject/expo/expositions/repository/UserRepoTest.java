package com.myproject.expo.expositions.repository;

import com.myproject.expo.expositions.ExpositionsSpringApplication;
import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.dto.UserDto;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.entity.Role;
import com.myproject.expo.expositions.entity.Status;
import com.myproject.expo.expositions.entity.User;
import com.myproject.expo.expositions.generator.TestEntity;
import org.apache.catalina.UserDatabase;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(SpringRunner.class)
//@DataJpaTest
//@Import(UserRepo.class)
//@EnableJpaRepositories(basePackageClasses = UserRepo.class)
//@SpringBootTest
@RunWith(SpringRunner.class)
@DataJpaTest
//@SpringBootTest(classes = ExpositionsSpringApplication.class)
@ComponentScan(basePackages = "com.myproject.expo.expositions.")
public class UserRepoTest {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    @Qualifier("getUserBuild")
    private Build<UserDto, User> build;
    @Autowired
    @Qualifier("expoBuild")
    private Build<ExpoDto, Exposition> buildExpo;

    @Test
    public void findByEmail() {
        User user = User.builder()
                .name("name")
                .surname("surname")
                .email("some@gmail.com")
                .phone("+390564578213")
                .balance(new BigDecimal(100))
                .roles(Collections.singleton(Role.USER))
               .expos(Collections.singletonList(buildExpo.toModel(TestEntity.ExpoTest.expoDto1)))
                .status(Status.ACTIVE.getStatusId())
                .build();
         this.userRepo.save(user);
        User byEmail = this.userRepo.findByEmail("some@gmail.com");
        System.out.println(byEmail);
//        assertThat(user.getId()).isGreaterThan(0);

       // assertThat(userRepo.findByEmail("some@gmail.com")).isNull();


    }

    @Test
    public void changeStatus() {
    }

    @Test
    public void getBalance() {
    }

    @Test
    public void changeBalance() {
    }

    @Test
    public void changeEmail() {
    }

    @Test
    public void changePassword() {
    }

    @Test
    public void getByEmail() {
    }
}