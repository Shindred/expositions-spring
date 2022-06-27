package com.myproject.expo.expositions.build;

import com.myproject.expo.expositions.dto.HallDto;
import com.myproject.expo.expositions.entity.Hall;
import org.springframework.stereotype.Component;

@Component
public class HallBuild implements Build<HallDto, Hall> {
    @Override
    public HallDto toDto(Hall hall) {
        return HallDto.builder()
                .id(hall.getIdHall())
                .name(hall.getName())
                .build();
    }

    @Override
    public Hall toModel(HallDto hallDto) {
        Hall hall = new Hall();
        hall.setIdHall(hallDto.getId());
        hall.setName(hallDto.getName());
        return hall;
    }
}
