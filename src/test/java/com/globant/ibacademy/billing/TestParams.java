package com.globant.ibacademy.billing;

import com.globant.ibacademy.billing.model.Product;

import java.util.stream.Stream;

public class TestParams {

    public static Stream<Product> products() {
        return Stream.of(
                new Product( "Product1", "Desciption1" , 1.50) ,
                new Product( null, "Description2", 2.50),
                new Product( "Product3",null, 5.50)
        );
    }



    public static Stream<Product> validProducts() {
        return Stream.of(
                new Product( "Product1", "Desciption1" , 1.50) ,
                new Product( "Product2", "Description2", 2.50),
                new Product( "Product3","Product3", 5.50)
        );
    }

    public static Stream<String> productNames() {
        return Stream.of("Product1", "Product2", "Product3");
    }


    public static Stream<Integer> productIds() {
        return Stream.of(1,2 );
    }

}
