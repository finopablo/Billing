package com.globant.ibacademy.billing.dao;

import com.globant.ibacademy.billing.model.Bill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillDao  extends CrudRepository<Bill, Integer> {
    public Optional<Bill> findByNumber(Integer number);

    public List<Bill> findByCustomer(String client);

    public List<Bill> findAll();
}
