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

@SpringBootApplication
@Slf4j
public class BillingApplication
{

    public static void main(String[] args) {
        SpringApplication.run(BillingApplication.class, args);
    }

}
