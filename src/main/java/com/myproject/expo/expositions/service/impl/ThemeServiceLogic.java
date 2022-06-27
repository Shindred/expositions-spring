package com.myproject.expo.expositions.service.impl;

import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.DTO;
import com.myproject.expo.expositions.dto.ThemeDto;
import com.myproject.expo.expositions.entity.Theme;
import com.myproject.expo.expositions.exception.custom.ExpoException;
import com.myproject.expo.expositions.exception.custom.ThemeException;
import com.myproject.expo.expositions.repository.ThemeRepo;
import com.myproject.expo.expositions.service.ThemeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ThemeServiceLogic implements ThemeService {
    private final ThemeRepo themeRepo;
    private final Build<ThemeDto,Theme> build;

    @Autowired
    public ThemeServiceLogic(ThemeRepo themeRepo,
                             @Qualifier("themeBuild") Build<ThemeDto,Theme> build) {
        this.themeRepo = themeRepo;
        this.build = build;
    }

    @Override
    public Page<Theme> getAll(Pageable pageable) {
        Page<Theme> themes;
        try{
           themes = themeRepo.findAll(pageable);
        }catch (Exception e){
            throw new ThemeException("err.cant_get_expos");
        }
        themes.forEach(System.out::println);
        return themes;
    }

    @Override
    public List<Theme> getAll() {
        return themeRepo.findAll();
    }

    @Transactional
    @Override
    public Theme save(ThemeDto themeDto) {
        Theme theme;
         try{
            theme = themeRepo.save(build.toModel(themeDto));
         }catch (Exception e){
             throw new ThemeException("err.add_new_theme");
         }
        return theme;
    }

    @Transactional
    @Override
    public int update(ThemeDto themeDto){
        try{
            themeRepo.save(build.toModel(themeDto));
        }catch (Throwable e){
            log.warn("Updating theme by name {} has failed",themeDto.getName());
            throw new ThemeException("err.update_theme");
        }
        return 1;
    }

    @Transactional
    @Override
    public int delete(Long id) {
        try{
            themeRepo.deleteById(id);
        }catch (Exception e){
            throw new ThemeException("err.delete_theme");
        }
        return 1;
    }

    @Override
    public Theme getById(Long id) {
        return themeRepo.findById(id)
                .orElseGet(Theme::new);
    }
}
