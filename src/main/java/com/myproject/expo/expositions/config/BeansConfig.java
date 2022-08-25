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
    public MainUtilController getMainUtilController(@Qualifier("userServiceImpl") UserService userService,ControllerHelper controllerHelper) {
        return new MainUtilController(userService,controllerHelper);
    }

    @Bean
    public UserServiceFacade getUserServiceFacade(UserRepo userRepo, ExpoRepo expoRepo){
        return new UserServiceFacade(userRepo,expoRepo,getPasswordEncoder(),getUserBuild());
    }

    @Bean
    public AdminControllerUtil getAdminUtilController(@Qualifier("userServiceImpl") UserService userService, ControllerHelper controllerHelper) {
        return new AdminControllerUtil(userService,controllerHelper);
    }

    @Bean
    public ExpoControllerUtil getExpoUtilController(ExpoService expoService, HallService hallService,
                                                    ThemeService themeService, Validate validate,
                                                    @Qualifier("expoBuild") Build<ExpoDto,Exposition> build,
                                                    ControllerHelper controllerHelper) {
        return new ExpoControllerUtil(expoService, hallService, themeService,validate,build,controllerHelper);
    }

    @Bean
    public HallControllerUtil getHallUtilController(HallService hallService,ControllerHelper controllerHelper) {
        return new HallControllerUtil(hallService,controllerHelper);
    }

    @Bean
    public ThemeControllerUtil getThemeUtilController(ThemeService themeService, ControllerHelper controllerHelper) {
        return new ThemeControllerUtil(themeService,new ArrayList<>(),controllerHelper);
    }

    @Bean
    public UserControllerUtil getUserUtilController(UserService userService,ControllerHelper controllerHelper){
        return new UserControllerUtil(userService,controllerHelper);
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
