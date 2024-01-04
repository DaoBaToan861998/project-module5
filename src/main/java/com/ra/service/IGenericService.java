package com.ra.service;

import com.ra.advice.CustomException;

import java.util.Optional;

public interface IGenericService<T> {
    Iterable<T> findAll();
    Optional<T> findById(Long id);
    T save(T t) throws CustomException;
    void deleteById(Long id);
}
