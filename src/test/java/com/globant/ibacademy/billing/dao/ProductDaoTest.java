package com.globant.ibacademy.billing.dao;

import com.globant.ibacademy.billing.exceptions.DataAccessException;
import com.globant.ibacademy.billing.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductDaoTest {

    @Autowired
    private  ProductDao dao;

    @Test
    void givenValidNotExistingProduct_whenSaveProduct_thenSuccess() throws DataAccessException {
            Product saved = dao.save(new Product("Product3", "Product3", 3.20));
            assertNotNull(saved.getId());
            assertEquals(dao.findById(saved.getId()).orElse(null), saved);
    }


    @Test
    void givenValidExistingProduct_whenSaveProduct_thenThrowsException()  {
        assertThrows(DataIntegrityViolationException.class, () -> {
            dao.save(new Product("Product1", "Updated product 1", 3.20));
        });
    }

    @Test
    void givenValidExistingProduct_whenSaveProduct_thenUpdate() {
            dao.save(new Product(1,"Product3", "Product3", 10.20));
            Product p = dao.findById(1).orElse(null);
            assertEquals("Product3", p.getName());
            assertEquals( "Product3", p.getDescription());
            assertEquals( 10.20, p.getPrice());
    }

    @Test
    void givenValidData_whenFindAllProducts_thenSuccess()   {
        List<Product> products = dao.findAll();
        assertEquals(2, products.size());
    }

    @Test
    void givenAValidProductId_WhenFindById_ThenSuccess()   {
        Optional<Product> product = dao.findById(1);
        assertEquals(1, product.get().getId(), 1);
        assertEquals("Product1", product.get().getName());

    }

    @Test
    void givenAValidProductId_WhenFindById_ThenReturnEmptyOptional()   {
        Optional<Product> product = dao.findById(15);
        assertEquals(product, Optional.empty() );
    }





    @Test
    void testDeleteShouldDeleteProduct()   {
            dao.deleteById(1);
            assertEquals(Optional.empty(), dao.findById(1));
            assertEquals( 1, dao.findAll().size());
    }
}