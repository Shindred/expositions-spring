package com.myproject.expo.expositions.service;


public interface GeneralService<T, U> extends  Savable<T, U> {
    int update(U dto);
}
