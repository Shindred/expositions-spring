package com.myproject.expo.expositions.service;

import com.myproject.expo.expositions.dto.HallDto;
import com.myproject.expo.expositions.entity.Hall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HallService extends GeneralService<Hall, HallDto> {

    List<Hall> getAll();

    Page<Hall> getHalls(Pageable pageable);

    int delete(Long id);
}
