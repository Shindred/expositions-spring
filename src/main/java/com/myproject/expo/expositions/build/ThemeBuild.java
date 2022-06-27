package com.myproject.expo.expositions.build;

import com.myproject.expo.expositions.dto.ThemeDto;
import com.myproject.expo.expositions.entity.Theme;
import org.springframework.stereotype.Component;

@Component
public class ThemeBuild implements Build<ThemeDto, Theme>{
    @Override
    public ThemeDto toDto(Theme theme) {
        return ThemeDto.builder()
                .id(theme.getIdTheme())
                .name(theme.getName())
                .build();
    }

    @Override
    public Theme toModel(ThemeDto themeDto) {
        return new Theme(themeDto.getId(), themeDto.getName());
    }
}
