package com.globant.ibacademy.billing.dao;

import com.globant.ibacademy.billing.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public  interface ProductDao extends CrudRepository<Product, Integer> {

        public List<Product> findAll();
}
