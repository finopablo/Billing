package com.globant.ibacademy.billing.model;

import java.util.Objects;

public record Item (
    Product product,
    long qty,
    double price) {

    double total() {
        return price * qty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return product.equals(item.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }
}

