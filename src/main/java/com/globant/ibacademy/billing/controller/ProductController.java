package com.globant.ibacademy.billing.controller;


import com.globant.ibacademy.billing.model.Product;
import com.globant.ibacademy.billing.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/products")
@Validated

public class ProductController extends Controller<Product> {

    final ProductService productService;


    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @Operation(
            summary = "Retrieve a Product",
            description = "Retrieve a Product based on the ID",
            tags = {"Product", "find"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Product.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = Product.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@Min(value = 0, message = "Invalid Product ID") @PathVariable("id") Integer id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }


    @Operation(
            summary = "Retrieve a Product By Name",
            description = "Retrieve a Product based on Product Name",
            tags = {"Product", "find"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Product.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = Product.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())})
    })
    @GetMapping(params = {"name"})
    @ResponseBody
    public ResponseEntity<Product> getProductByName(@NotEmpty(message = "Name is required") String name) {
        return ResponseEntity.ok(productService.findByName(name));
    }

    @Operation(
            summary = "Retrieve All Products",
            description = "Retrieve a list of all existing products",
            tags = {"Product", "find"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Product[].class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema(implementation = Product[].class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())})
    })
    @GetMapping()
    @ResponseBody
    public ResponseEntity<List<Product>> getProducts() {
        return response.apply(productService.findAll());
    }


    @Operation(
            summary = "Add a new Product",
            description = "Retrieve a list of all existing products",
            tags = { "Product","add" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content( mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
    })
    @PostMapping()
    public ResponseEntity<Product> addProduct(@NotNull @RequestBody Product product) {
            Product savedProduct = this.productService.saveProduct(product);
            return ResponseEntity.created(location.apply(savedProduct.id())).body(product);

    }

    @Operation(
            summary = "Update an existing product",
            description = "Update an existing product",
            tags = { "Product","update" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content( mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> addProduct(@NotNull @RequestBody Product product, @Min(value = 0, message = "Invalid Product ID") @PathVariable("id") Integer productId) {
        this.productService.saveProduct(new Product(productId, product.name(), product.description(),product.price()));
        return ResponseEntity.accepted().build();
    }
}
