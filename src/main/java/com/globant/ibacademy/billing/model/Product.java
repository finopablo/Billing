package com.globant.ibacademy.billing.model;


public record Product(
    Integer id,
    String name,
    String description,
    double price)
{

    public Product(String name, String description, double price) {
      this(null, name, description, price);
    }
}
