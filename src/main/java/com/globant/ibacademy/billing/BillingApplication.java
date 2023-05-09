package com.globant.ibacademy.billing;

import com.globant.ibacademy.billing.dao.ProductDao;
import com.globant.ibacademy.billing.dao.mysql.ProductSqlDao;
import com.globant.ibacademy.billing.model.Product;
import com.globant.ibacademy.billing.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Stream;

@SpringBootApplication
@Slf4j
public class BillingApplication implements CommandLineRunner
{

    public static void main(String[] args) {
        SpringApplication.run(BillingApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));

        Class.forName(properties.getProperty("db.driver"));
        Connection conn = DriverManager.getConnection(properties.getProperty("db.url"), properties.getProperty("db.user"), properties.getProperty("db.pwd"));

        ProductDao productDao = new ProductSqlDao(conn);
        ProductService productService = new ProductService(productDao);
        productService.findAll().stream().forEach(o -> log.info(o.toString()));


        List<Product> products = List.of(new Product("Product 3", "Desceription 3", 6.33), new Product("Product 4", "description 4", 9));

        productService.bulkSaveProducts(products);


    }
}
