package com.myproject.expo.expositions.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetAll<T, U> {
    Page<U> getAll(Pageable pageable);
}
