package com.myproject.expo.expositions.repository;

import com.myproject.expo.expositions.entity.Theme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeRepo extends JpaRepository<Theme,Long> {

    @Override
    Page<Theme> findAll(Pageable pageable);

}
