package com.globant.ibacademy.billing.controller;


import com.globant.ibacademy.billing.model.Product;
import com.globant.ibacademy.billing.service.ProductService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping(value = "/api/v1/products")
@Validated

public class ProductController {

    final ProductService productService;

    Function<List<Product>, ResponseEntity<List<Product>>> response = (list) ->  ( list.isEmpty()  ? ResponseEntity.notFound().build() : ResponseEntity.ok(list));
    Function<Integer, URI> location = (id) -> ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

    @Autowired
    public ProductController(ProductService productService) {
            this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById( @Min(value = 0, message = "Invalid Product ID") @PathVariable("id")  Integer id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @GetMapping(params = {"name"})
    @ResponseBody
    public ResponseEntity<Product> getProductByName( @NotEmpty(message = "Name is required") String name) {
        return ResponseEntity.ok(productService.findByName(name));
    }

    @GetMapping()
    @ResponseBody
    public ResponseEntity<List<Product>> getProducts() {
        return response.apply(productService.findAll());
    }


    @PostMapping()
    public ResponseEntity<Product> addProduct(@NotNull @RequestBody Product product) {
            Product savedProduct = this.productService.saveProduct(product);
            return ResponseEntity.created(location.apply(savedProduct.id())).body(product);

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> addProduct(@NotNull @RequestBody Product product, @Min(value = 0, message = "Invalid Product ID") @PathVariable("id") Integer productId) {
        this.productService.saveProduct(new Product(productId, product.name(), product.description(),product.price()));
        return ResponseEntity.accepted().build();
    }
}
