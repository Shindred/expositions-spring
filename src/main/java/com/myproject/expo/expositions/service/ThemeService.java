package com.myproject.expo.expositions.service;

import com.myproject.expo.expositions.dto.ThemeDto;
import com.myproject.expo.expositions.entity.Theme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ThemeService extends GeneralService<Theme, ThemeDto> {
    List<Theme> getAll();

    Page<Theme> getAll(Pageable pageable);

    int delete(Long id);

    Theme getById(Long id);
}
