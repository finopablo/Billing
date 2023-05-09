package com.globant.ibacademy.billing.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


public record Bill (
    Integer id,
    Integer number,
    LocalDateTime date,
    String customer,
    Set<Item> items)
{
    public Bill(Integer number, LocalDateTime date, String customer ) {
        this(null, number, date, customer, new HashSet<>());
    }
    public double total () {
        return items.stream().map( Item::total).reduce(Double.MIN_VALUE, Double::sum);
    }
    public void add(Item item) {
        items.add(item);
    }
}

