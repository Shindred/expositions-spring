package com.myproject.expo.expositions.repository;

import com.myproject.expo.expositions.entity.Statistic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticRepo extends CrudRepository<Statistic,Long> {
}
