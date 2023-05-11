package com.globant.ibacademy.billing;

import com.globant.ibacademy.billing.model.Product;

import java.util.List;
import java.util.stream.Stream;

public class TestParams {

    public static List<Product> validProductsWithIds() {
        return List.of(
                new Product( 1, "Product1", "Description1" , 1.50) ,
                new Product( 2, "Product2", "Description2", 2.50),
                new Product( 3, "Product3","Description3", 5.50)
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
