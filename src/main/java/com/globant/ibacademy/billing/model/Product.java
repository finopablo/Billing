package com.globant.ibacademy.billing.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="products", uniqueConstraints = { @UniqueConstraint(name="unq_products", columnNames = {"name"})} )
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "name", unique = true)
    @NotNull(message = "Name is required")
    String name;
    @Column(name = "description", nullable = true)
    String description;
    @Column(name = "price")
    double price;

    public Product(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
