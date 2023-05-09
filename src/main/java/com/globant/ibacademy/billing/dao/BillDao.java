package com.globant.ibacademy.billing.dao;

import com.globant.ibacademy.billing.exceptions.DataAccessException;
import com.globant.ibacademy.billing.model.Bill;

import java.util.List;
import java.util.Optional;

public interface BillDao  extends Dao<Bill> {
    public abstract Optional<Bill> findByNumber(Integer number) throws DataAccessException;
    public abstract List<Bill> findByClient(String client) throws DataAccessException;
}
