package com.myproject.expo.expositions.service;

import com.myproject.expo.expositions.dto.ExpoDto;
import com.myproject.expo.expositions.entity.Exposition;

import java.util.List;

public interface ExpoService extends GetAll<Exposition,ExpoDto> {

    Exposition addExpo(ExpoDto expoDto, List<Long> hallsIds);

    ExpoDto getById(Long id);

    boolean update(Long id, Exposition expo);

    boolean changeStatus(Long id,Integer statusId);

    List<Exposition> getAll();

    List<ExpoDto> getSearchedExpos(String search,String selected);
}
