package com.globant.ibacademy.billing.dao;

import com.globant.ibacademy.billing.model.Bill;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BillDao  extends CrudRepository<Bill, Integer> {
    public Optional<Bill> findByNumber(Integer number);

    public List<Bill> findByCustomer(String client);

    public List<Bill> findAll();
}
