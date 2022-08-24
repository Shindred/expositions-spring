package com.myproject.expo.expositions.config;

import com.myproject.expo.expositions.build.*;
import com.myproject.expo.expositions.controller.filter.CleanCacheFilter;
import com.myproject.expo.expositions.controller.util.*;
import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.dto.HallDto;
import com.myproject.expo.expositions.dto.ThemeDto;
import com.myproject.expo.expositions.dto.UserDto;
import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.entity.Hall;
import com.myproject.expo.expositions.entity.Theme;
import com.myproject.expo.expositions.entity.User;
import com.myproject.expo.expositions.repository.ExpoRepo;
import com.myproject.expo.expositions.repository.UserRepo;
import com.myproject.expo.expositions.service.ExpoService;
import com.myproject.expo.expositions.service.HallService;
import com.myproject.expo.expositions.service.ThemeService;
import com.myproject.expo.expositions.service.UserService;
import com.myproject.expo.expositions.service.facade.UserServiceFacade;
import com.myproject.expo.expositions.validator.Validate;
import com.myproject.expo.expositions.validator.ValidateInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.ArrayList;

@Configuration
public class BeansConfig {
    @Autowired
    private MessageSource messageSource;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public Validate getValidate() {
        return new ValidateInput();
    }

    @Bean
    public Build<ExpoDto, Exposition> getExpoBuild() {
        return new ExpoBuild();
    }

    @Bean
    public Build<ThemeDto, Theme> getThemeBuild() {
        return new ThemeBuild();
    }

    @Bean
    public Build<HallDto, Hall> getHallBuild() {
        return new HallBuild();
    }

    @Bean
    public Build<UserDto, User> getUserBuild() {
        return new UserBuild();
    }

    @Bean
    public MainUtilController getMainUtilController(@Qualifier("userServiceImpl") UserService userService) {
        return new MainUtilController(userService);
    }

    @Bean
    public UserServiceFacade getUserServiceFacade(UserRepo userRepo, ExpoRepo expoRepo){
        return new UserServiceFacade(userRepo,expoRepo,getPasswordEncoder(),getUserBuild());
    }

    @Bean
    public AdminUtilController getAdminUtilController(@Qualifier("userServiceImpl") UserService userService,ControllerHelper controllerHelper) {
        return new AdminUtilController(userService,controllerHelper);
    }

    @Bean
    public ExpoUtilController getExpoUtilController(ExpoService expoService, HallService hallService,
                                                    ThemeService themeService,Validate validate,
                                                    @Qualifier("expoBuild") Build<ExpoDto,Exposition> build,
                                                    ControllerHelper controllerHelper) {
        return new ExpoUtilController(expoService, hallService, themeService,validate,build,controllerHelper);
    }

    @Bean
    public HallUtilController getHallUtilController(HallService hallService) {
        return new HallUtilController(hallService);
    }

    @Bean
    public ThemeUtilController getThemeUtilController(ThemeService themeService) {
        return new ThemeUtilController(themeService,new ArrayList<>());
    }

    @Bean
    public UserUtilController getUserUtilController(UserService userService){
        return new UserUtilController(userService);
    }

    @Bean
    public FilterRegistrationBean<CleanCacheFilter> loggingFilter(){
        FilterRegistrationBean<CleanCacheFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new CleanCacheFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);

        return registrationBean;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}
