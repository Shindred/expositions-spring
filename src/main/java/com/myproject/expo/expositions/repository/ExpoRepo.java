package com.myproject.expo.expositions.repository;

import com.myproject.expo.expositions.entity.Exposition;
import com.myproject.expo.expositions.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpoRepo extends JpaRepository<Exposition, Long> {
    Page<Exposition> findExpositionsByUsersAndStatusId(User user, Integer statusId, Pageable pageable);

    @Modifying
    @Query("update Exposition expo set expo.statusId= ?1 where expo.idExpo= ?2")
    int changeStatus(@Param("statusId") Integer statusId, @Param("idExpo") Long idExpo);

    List<Exposition> findByThemeName(@Param("theme_name") String theme_name);

    List<Exposition> findByExpoDate(@Param("expoDate") LocalDate expoDate);
}
