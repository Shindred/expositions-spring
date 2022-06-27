package com.myproject.expo.expositions.service.impl;

import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.HallDto;
import com.myproject.expo.expositions.entity.Hall;
import com.myproject.expo.expositions.exception.custom.HallException;
import com.myproject.expo.expositions.repository.HallRepo;
import com.myproject.expo.expositions.service.HallService;
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
public class HallServiceLogic implements HallService {
    private final HallRepo hallRepo;
    private final Build<HallDto,Hall> build;

    @Autowired
    public HallServiceLogic(HallRepo hallRepo, @Qualifier("hallBuild") Build<HallDto,Hall> build) {
        this.hallRepo = hallRepo;
        this.build = build;
    }

    @Override
    public Page<Hall> getHalls(Pageable pageable) {
        Page<Hall> halls;
        try{
            halls = hallRepo.findAll(pageable);
        }catch (Exception e){
            throw new HallException("cant_get_halls");
        }
        return halls;
    }

    @Override
    public List<Hall> getAll() {
        List<Hall> allHalls;
        allHalls = hallRepo.findAll();
        return allHalls;
    }

    @Transactional
    @Override
    public Hall save(HallDto hallDto) {
        Hall save;
        Hall hall = build.toModel(hallDto);
        try{
            save = hallRepo.save(hall);
        }catch (Exception e){
            throw new HallException("err.hall_exist");
        }
        return save;
    }

    @Transactional
    @Override
    public int update(HallDto hallDto) {
        hallRepo.save(build.toModel(hallDto));
        return 1;
    }

    @Transactional
    @Override
    public int delete(Long id) {
        hallRepo.deleteById(id);
        return 1;
    }
}
