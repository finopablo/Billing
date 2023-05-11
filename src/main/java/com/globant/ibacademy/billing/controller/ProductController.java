package com.globant.ibacademy.billing.controller;


import com.globant.ibacademy.billing.dto.ProductDto;
import com.globant.ibacademy.billing.model.Product;
import com.globant.ibacademy.billing.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/products" )
@Validated
public class ProductController  extends Controller<ProductDto> {

    final ProductService productService;
    final ModelMapper mapper;

    @Autowired
    public ProductController(ModelMapper mapper, ProductService productService) {
        this.productService = productService;
        this.mapper = mapper;
    }

    @Operation(
            summary = "Retrieve a Product",
            description = "Retrieve a Product based on the ID",
            tags = {"Product", "find"})

    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Product.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = Product.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())})
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@Min(value = 0, message = "Invalid Product ID") @PathVariable("id") Integer id) {
        return ResponseEntity.ok(toDto(productService.findProductById(id)));
    }


    @Operation(
            summary = "Retrieve a Product By Name",
            description = "Retrieve a Product based on Product Name",
            tags = {"Product", "find"})
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Product.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = Product.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())})

    @GetMapping(params = {"name"})
    @ResponseBody
    public ResponseEntity<ProductDto> getProductByName(@NotEmpty(message = "Name is required") String name) {
        return ResponseEntity.ok(toDto(productService.findByName(name)));
    }

    @Operation(
            summary = "Retrieve All Products",
            description = "Retrieve a list of all existing products",
            tags = {"Product", "find"})
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Product[].class), mediaType = "application/json")})
    @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema(implementation = Product[].class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())})
    @GetMapping()
    @ResponseBody
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<Product> products = productService.findAll();
        return response.apply(products.stream().map(this::toDto).toList());
    }

    @Operation(
            summary = "Add a new Product",
            description = "Retrieve a list of all existing products",
            tags = { "Product","add" })
    @ApiResponse(responseCode = "201", content = { @Content( mediaType = "application/json") })
    @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
    @PostMapping()
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductDto product) {
       Product savedProduct = this.productService.saveProduct(fromDto(product));
       return ResponseEntity.created(location.apply(savedProduct.getId())).body(toDto(savedProduct));

    }
    @Operation(
            summary = "Update an existing product",
            description = "Update an existing product",
            tags = { "Product","update" })

    @ApiResponse(responseCode = "201", content = { @Content( mediaType = "application/json") })
    @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductDto productDto, @Min(value = 0, message = "Invalid Product ID") @PathVariable("id") Integer productId) {
        final Product savedProduct = this.productService.saveProduct(fromDto(productDto));
        return ResponseEntity.accepted().body(toDto(savedProduct));
    }


    public Product fromDto(ProductDto productDto) {
        return mapper.map(productDto, Product.class);
    }

    public ProductDto toDto(Product product) {
        return mapper.map(product, ProductDto.class);
    }
}
