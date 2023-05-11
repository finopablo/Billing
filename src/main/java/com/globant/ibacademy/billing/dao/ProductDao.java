package com.globant.ibacademy.billing.dao;

import com.globant.ibacademy.billing.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public  interface ProductDao extends CrudRepository<Product, Integer> {

        public List<Product> findAll();
}
