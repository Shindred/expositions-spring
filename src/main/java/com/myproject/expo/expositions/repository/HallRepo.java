package com.myproject.expo.expositions.repository;

import com.myproject.expo.expositions.entity.Hall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HallRepo extends JpaRepository<Hall, Long> {
    @Override
    Page<Hall> findAll(Pageable pageable);


}
