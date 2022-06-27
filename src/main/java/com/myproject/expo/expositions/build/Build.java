package com.myproject.expo.expositions.build;

public interface Build<T, U> {
    T toDto(U u);

    U toModel(T t);
}
