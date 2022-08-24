package com.myproject.expo.expositions.service.impl;

import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.ThemeDto;
import com.myproject.expo.expositions.entity.Theme;
import com.myproject.expo.expositions.exception.custom.ThemeException;
import com.myproject.expo.expositions.repository.ThemeRepo;
import com.myproject.expo.expositions.service.ThemeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The ThemeServiceLogic class do transfer operations for Theme entity and logic. Transfer data to repository layer
 */
@Service
public class ThemeServiceImpl implements ThemeService {
    private static final Logger log = LogManager.getLogger(ThemeServiceImpl.class);
    private final ThemeRepo themeRepo;
    private final Build<ThemeDto,Theme> build;

    public ThemeServiceImpl(ThemeRepo themeRepo, @Qualifier("themeBuild") Build<ThemeDto,Theme> build) {
        this.themeRepo = themeRepo;
        this.build = build;
    }

    @Override
    public Page<Theme> getAll(Pageable pageable) {
        Page<Theme> themes;
        try{
           themes = themeRepo.findAll(pageable);
        }catch (Exception e){
            log.warn("Cannot get all themes");
            throw new ThemeException("err.cant_get_expos");
        }
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
             log.warn("The theme with such name already exists in the system");
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
            log.warn("The theme with id {} is in usage.Cannot delete it",id);
            throw new ThemeException("err.delete_theme");
        }
        return 1;
    }

    @Override
    public Theme getById(Long id) {
        return themeRepo.findById(id).orElseGet(Theme::new);
    }
}
