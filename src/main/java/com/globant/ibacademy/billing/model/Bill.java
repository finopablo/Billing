package com.globant.ibacademy.billing.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;



@Data
@Entity
@Table(name ="bills")
@AllArgsConstructor
@NoArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "number")
    Integer number;
    @Column(name = "date")
    LocalDateTime date;
    @Column(name = "customer")
    String customer;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_bill")
    Set<Item> items;

    public Bill(Integer number, LocalDateTime date, String customer ) {
        this.number = number;
        this.date = date;
        this.customer = customer;
        this.items = new HashSet<>();
    }
    public double total () {
        return items.stream().map( Item::total).reduce(Double.MIN_VALUE, Double::sum);
    }
    public void add(Item item) {
        items.add(item);
    }

}

