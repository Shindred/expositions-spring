package com.myproject.expo.expositions.service;

public interface Savable<T,U> {
    T save(U dto);
}
