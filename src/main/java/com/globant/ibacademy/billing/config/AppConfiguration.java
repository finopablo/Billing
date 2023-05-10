package com.globant.ibacademy.billing.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@Slf4j
public class AppConfiguration {


    @Bean
    public Connection getConnection(@Value ("${db.url}") String dbUrl,
                                    @Value ("${db.driver}") String driver,
                                    @Value ("${db.user}") String user,
                                    @Value ("${db.pwd}") String pwd) throws SQLException, ClassNotFoundException {
        log.info("Creating connection");
        Class.forName(driver);
        return DriverManager.getConnection(dbUrl, user, pwd);
    }



}
