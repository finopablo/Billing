package com.globant.ibacademy.billing.dao.mysql;

import com.globant.ibacademy.billing.TestUtil;
import com.globant.ibacademy.billing.dao.ProductDao;
import com.globant.ibacademy.billing.exceptions.DataAccessException;
import com.globant.ibacademy.billing.model.Product;
import org.h2.tools.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductDaoTest {

    private  Connection conn;
    private  ProductDao dao;

    @BeforeAll
    static void beforeAll() throws SQLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }


    @BeforeEach
    void init() throws SQLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        this.conn = TestUtil.createConnection();
        dao = new ProductSqlDao(this.conn);
    }

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
    void testFindByIdThrowsDatabaseException() throws SQLException {
        conn.close();
        assertThrows(DataAccessException.class, () ->  dao.findById(1));
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