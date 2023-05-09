package com.globant.ibacademy.billing.service;

import com.globant.ibacademy.billing.dao.ProductDao;
import com.globant.ibacademy.billing.exceptions.DataAccessException;
import com.globant.ibacademy.billing.exceptions.EntityNotFoundException;
import com.globant.ibacademy.billing.model.Product;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
public class ProductService {
    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> bulkSaveProducts(List<Product> productList) {
        return
                productList
                        .stream()
                        .map(p ->{
                                try {
                                   return productDao.save(p);
                                } catch(DataAccessException e) {
                                     return null;
                                }
                        })
                        .filter(Objects::nonNull)
                        .toList();

    }

    public Product saveProduct(Product product) {
        if (Objects.isNull(product)) {
            throw new IllegalArgumentException("Product can't be null");
        }
        return productDao.save(product);
    }

    public Product findProductById(Integer id) throws EntityNotFoundException {
        return productDao.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Product ID %s does not exists", id)));
    }

    public void deleteProduct(Integer id) {
        this.productDao.delete(id);
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public Product findByName(String name) throws EntityNotFoundException {
        return productDao.
                findAll().
                stream().
                filter(o -> name.equals(o.name())).
                findFirst().
                orElseThrow(() -> new EntityNotFoundException(String.format("Product name %s does not exists", name)));
    }
}
