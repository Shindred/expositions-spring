package com.myproject.expo.expositions.service.impl;

import com.myproject.expo.expositions.build.Build;
import com.myproject.expo.expositions.dto.HallDto;
import com.myproject.expo.expositions.entity.Hall;
import com.myproject.expo.expositions.exception.custom.HallException;
import com.myproject.expo.expositions.repository.HallRepo;
import com.myproject.expo.expositions.service.HallService;
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
 * The HallServiceLogic class do transfer operations with Hall entity. Do logic. Transfer data to the repository layer
 */
@Service
public class HallServiceImpl implements HallService {
    private static final Logger log = LogManager.getLogger(HallServiceImpl.class);
    private final HallRepo hallRepo;
    private final Build<HallDto,Hall> build;

    @Autowired
    public HallServiceImpl(HallRepo hallRepo, @Qualifier("hallBuild") Build<HallDto,Hall> build) {
        this.hallRepo = hallRepo;
        this.build = build;
    }

    @Override
    public Page<Hall> getHalls(Pageable pageable) {
        Page<Hall> halls;
        try{
            halls = hallRepo.findAll(pageable);
        }catch (Exception e){
            log.warn("Cannot get all halls");
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
            log.warn("Cannot add the new hall {}. Already exists",hallDto.getName());
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
