package com.globant.ibacademy.billing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globant.ibacademy.billing.config.BillAppConfiguration;
import com.globant.ibacademy.billing.exceptions.EntityAlreadyExistsException;
import com.globant.ibacademy.billing.exceptions.EntityNotFoundException;
import com.globant.ibacademy.billing.model.Product;
import com.globant.ibacademy.billing.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.globant.ibacademy.billing.TestParams.validProductsWithIds;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(BillAppConfiguration.class)
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;


    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void givenAProduct_whenCallAddProductUrl_thenReturn201() throws Exception {

        Product savedProduct = new Product(1, "Product name", "Product description", 3.5);
        when(productService.saveProduct(any(Product.class))).thenReturn(savedProduct);

        MvcResult result = mockMvc.perform(post("/api/v1/products").
                contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(savedProduct))).
                andExpect(status().isCreated()).
                andExpect(header().exists("location")).andReturn();

        assertThat( result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(savedProduct));

    }

    @Test
    void givenAnExistingProductName_whenCallAddProductUrl_thenReturn409() throws Exception {
        Product product = new Product(1, "product1", "Description1", 2.2);
        when(productService.saveProduct(product)).thenThrow(EntityAlreadyExistsException.class);
        mockMvc.perform(post("/api/v1/products").
                contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(product))).
                andExpect(status().isConflict());

    }
    @Test
    void givenAProduct_whenCallUpdateProductUrl_thenReturn202() throws Exception {

        Product savedProduct = new Product(1, "Product name", "Product description", 3.5);
        when(productService.saveProduct(any(Product.class))).thenReturn(savedProduct);

        MvcResult result = mockMvc.perform(put("/api/v1/products/{id}", 1).
                     contentType(MediaType.APPLICATION_JSON).
                     content(objectMapper.writeValueAsString(savedProduct))).
                     andExpect(status().isAccepted()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualToIgnoringNewLines(objectMapper.writeValueAsString(savedProduct));
    }

    @Test
    void givenAnProductWithoutName_whenCallAddProductUrl_thenReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/products").
                        contentType(MediaType.APPLICATION_JSON).
                        content("{\"description\": \"Product 1 Description\",\n" +
                                "\"price\": 2.5}")).
                andExpect(status().isBadRequest());
    }

    @Test
    void whenCallAllProducts_thenReturns200() throws Exception {
        when(productService.findAll()).thenReturn(validProductsWithIds());
        MvcResult result = mockMvc.perform(get("/api/v1/products"))
            .andExpect(status().isOk())
            .andReturn();
        String response = result.getResponse().getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(validProductsWithIds());
        assertThat(response).isEqualToIgnoringNewLines(expectedResponse);
    }

    @Test
    void whenCallFindByName_thenReturns200() throws Exception {
        Product product = new Product(1, "product1", "Description1", 2.2);
        when(productService.findByName("test")).thenReturn(product);
        MvcResult result = mockMvc.perform(get("/api/v1/products").param("name", "test"))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isEqualToIgnoringNewLines(objectMapper.writeValueAsString(product));
    }

    @Test
    void whenCallFindByName_thenReturns404() throws Exception {

        when(productService.findByName("test")).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(get("/api/v1/products").param("name", "test"))
                .andExpect(status().isNotFound());
    }



    @Test
    void whenCallFindById_thenReturns200() throws Exception {
        Product product = new Product(1, "product1", "Description1", 2.2);
        when(productService.findProductById(1)).thenReturn(product);
        MvcResult result = mockMvc.perform(get("/api/v1/products/{id}", 1))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isEqualToIgnoringNewLines(objectMapper.writeValueAsString(product));
    }

    @Test
    void whenCallFindById_thenReturns404() throws Exception {
        when(productService.findProductById(1)).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(get("/api/v1/products/{id}", 1))
                                    .andExpect(status().isNotFound());
    }

}