package com.globant.ibacademy.billing.dao;

import com.globant.ibacademy.billing.exceptions.DataAccessException;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    T save(T data) throws DataAccessException;
    void delete(Integer id) throws DataAccessException;
    Optional<T> findById(Integer id) throws DataAccessException;
    List<T> findAll() throws DataAccessException;
}
