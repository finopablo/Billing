package com.globant.ibacademy.billing.service;

import com.globant.ibacademy.billing.dao.ProductDao;
import com.globant.ibacademy.billing.exceptions.DataAccessException;
import com.globant.ibacademy.billing.exceptions.EntityAlreadyExistsException;
import com.globant.ibacademy.billing.exceptions.EntityNotFoundException;
import com.globant.ibacademy.billing.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ProductService {
    private final ProductDao productDao;

    @Autowired
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> bulkSaveProducts(List<Product> productList) {
        return
                productList
                        .stream()
                        .map(p -> {
                            try {
                                return productDao.save(p);
                            } catch (DataIntegrityViolationException e) {
                                log.info("Bulk Save Product {} cannot be saved ", p.getName(), e );
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
        try
        {
            return productDao.save(product);
        } catch(DataIntegrityViolationException e) {
            log.error("Error saving product", e.getCause());
            throw new EntityAlreadyExistsException("Product already exists", e);
        }
    }

    public Product findProductById(Integer id) throws EntityNotFoundException {
        return productDao.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Product ID %s does not exists", id)));
    }

   public void deleteProduct(Integer id) {
        this.productDao.deleteById(id);
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public Product findByName(String name) throws EntityNotFoundException {
        return productDao.
                findAll().
                stream().
                filter(o -> name.equals(o.getName())).
                findFirst().
                orElseThrow(() -> new EntityNotFoundException(String.format("Product name %s does not exists", name)));
    }
}
