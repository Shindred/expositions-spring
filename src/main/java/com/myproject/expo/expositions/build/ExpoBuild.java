package com.myproject.expo.expositions.build;

import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.Exposition;
import org.springframework.stereotype.Component;

@Component
public class ExpoBuild implements Build<ExpoDto, Exposition> {
    @Override
    public ExpoDto toDto(Exposition exposition) {
        return ExpoDto.builder()
                .id(exposition.getIdExpo())
                .name(exposition.getName())
                .expoDate(exposition.getExpoDate())
                .expoTime(exposition.getExpoTime())
                .price(exposition.getPrice())
                .statistic(exposition.getStatistic())
                .theme(exposition.getTheme())
                .halls(exposition.getHalls())
                .statusId(exposition.getStatusId())
                .build();
    }

    @Override
    public Exposition toModel(ExpoDto expoDto) {
        Exposition expo = new Exposition();
        expo.setIdExpo(expoDto.getId());
        expo.setName(expoDto.getName());
        expo.setExpoDate(expoDto.getExpoDate());
        expo.setExpoTime(expoDto.getExpoTime());
        expo.setPrice(expoDto.getPrice());
        expo.setStatistic(expoDto.getStatistic());
        expo.setTheme(expoDto.getTheme());
        expo.setHalls(expoDto.getHalls());
        expo.setStatusId(defineStatus(expoDto));

        return expo;
    }

    private int defineStatus(ExpoDto dto){
        return dto.getStatusId() != 0 ? dto.getStatusId() : 1;
    }
}
