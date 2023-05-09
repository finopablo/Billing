package com.globant.ibacademy.billing.dao.mysql;

import com.globant.ibacademy.billing.dao.ProductDao;
import com.globant.ibacademy.billing.exceptions.DataAccessException;
import com.globant.ibacademy.billing.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductDaoTest {

    @Autowired
    private  Connection conn;
    @Autowired
    private  ProductDao dao;







    @Test

    void givenValidNotExistingProduct_whenSaveProduct_thenSuccess() throws DataAccessException {
            Product saved = dao.save(new Product("Product3", "Product3", 3.20));
            assertNotNull(saved.id());
            assertEquals(dao.findById(saved.id()).orElse(null), saved);
    }


    @Test
    void givenValidExistingProduct_whenSaveProduct_thenThrowsException()  {
        assertThrows(DataAccessException.class, () -> {
            dao.save(new Product("Product1", "Updated product 1", 3.20));
        });
    }

    @Test
    void givenValidExistingProduct_whenSaveProduct_thenUpdate() {
            dao.save(new Product(1,"Product3", "Product3", 10.20));
            Product p = dao.findById(1).orElse(null);
            assertEquals("Product3", p.name());
            assertEquals( "Product3", p.description());
            assertEquals( 10.20, p.price());
    }

    @Test
    void givenValidData_whenFindAllProducts_thenSuccess()   {
        List<Product> products = dao.findAll();
        assertEquals(2, products.size());
    }

    @Test
    void givenAValidProductId_WhenFindById_ThenSuccess()   {
        Optional<Product> product = dao.findById(1);
        assertEquals(1, product.get().id(), 1);
        assertEquals("Product1", product.get().name());

    }

    @Test
    void givenAValidProductId_WhenFindById_ThenReturnEmptyOptional()   {
        Optional<Product> product = dao.findById(15);
        assertEquals(product, Optional.empty() );
    }





    @Test
    void testDeleteShouldDeleteProduct()   {
            dao.delete(1);
            assertEquals(Optional.empty(), dao.findById(1));
            assertEquals( 1, dao.findAll().size());
    }

    @Test
    void testSaveNullProductShouldReturnsAnException() {
        assertThrows(IllegalArgumentException.class, () -> {
            dao.save(null);
        });
    }

}