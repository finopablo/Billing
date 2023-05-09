package com.globant.ibacademy.billing.service;

import com.globant.ibacademy.billing.TestParams;
import com.globant.ibacademy.billing.dao.ProductDao;
import com.globant.ibacademy.billing.exceptions.DataAccessException;
import com.globant.ibacademy.billing.exceptions.EntityNotFoundException;
import com.globant.ibacademy.billing.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private  ProductDao productDao;
    @InjectMocks  private  ProductService productService;
    private List<Product> productList = TestParams.validProducts().toList();




    @ParameterizedTest
    @MethodSource("com.globant.ibacademy.billing.TestParams#products")
     void givenValidProduct_whenSaveProduct_thenSucceed(Product product) {
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        when(productDao.save(captor.capture())).thenReturn(new Product(new Random().nextInt(), product.name(), product.description(), product.price()));
        Product returnedProduct = productService.saveProduct(product);
        assertNotNull(returnedProduct);
        assertNotNull( returnedProduct.id() );
        assertEquals(product.name(), returnedProduct.name());
        assertEquals(product.description(), returnedProduct.description());
        verify(productDao, only()).save(captor.getValue());
    }

    @Test
    void givenNullProduct_whenSaveProduct_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> productService.saveProduct(null));
    }


    @Test
    void givenValidID_whenFindAProduct_thenSucceed() throws EntityNotFoundException {
        Product product = new Product(1,"Product1","decription1", 2.33);
        when(productDao.findById(1)).thenReturn(Optional.of(product));
        Product returnProduct = productService.findProductById(1);
        assertNotNull(returnProduct);
        assertEquals(returnProduct, product);
        verify(productDao, only()).findById(1);
    }

    @Test
    void givenValidID_whenFindAProduct_thenThrowsEntityNotFound()   {
        when(productDao.findById(1)).thenReturn(Optional.ofNullable(null));
        assertThrows(EntityNotFoundException.class, () -> productService.findProductById(1));
        verify(productDao, only()).findById(1);
    }

    @ParameterizedTest
    @MethodSource("com.globant.ibacademy.billing.TestParams#productIds")
    void givenValidID_whenDeleteProduct_thenSuccess(Integer productId) {
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        lenient().doNothing().when(productDao).delete(captor.capture());
        productService.deleteProduct(productId);
        verify(productDao, only()).delete(captor.getValue());
    }

    @Test
    void given_whenFindAllProducts_thenSuccess()  {
        when(productDao.findAll()).thenReturn(productList);
        List<Product> returnProductList = productService.findAll();
        assertNotNull(returnProductList);
        assertEquals(productList.size(), returnProductList.size());
        verify(productDao, only()).findAll();
    }


    @Test
    void givenValidName_whenFindProductByName_thenSuccess() throws EntityNotFoundException {
        when(productDao.findAll()).thenReturn(productList);
        Product returnProduct = productService.findByName(productList.get(0).name());
        assertNotNull(returnProduct);
        assertEquals(productList.get(0), returnProduct);
        verify(productDao, only()).findAll();
    }


    @Test
    void givenNotExistingValidName_whenFindProductByName_thenThrowsAnException() {
        when(productDao.findAll()).thenReturn(productList);
        assertThrows(EntityNotFoundException.class, () -> productService.findByName("Invalid Product"));
    }

    @Test
    void givenAListOfProducts_whenSaveBulk_thenSuceess() {
        when(productDao.save(any(Product.class))).thenAnswer(returnsFirstArg());
        productService.bulkSaveProducts(productList);
        verify(productDao, times(productList.size())).save(any(Product.class));
    }

    @Test
    void givenAListOfProducts_whenSaveBulk_thenReturnOnlySavedProducts() {
        when(productDao.save(productList.get(0))).thenAnswer(returnsFirstArg());
        when(productDao.save(productList.get(1))).thenThrow(DataAccessException.class);
        when(productDao.save(productList.get(2))).thenAnswer(returnsFirstArg());
        List<Product> savedProducts = productService.bulkSaveProducts(productList);
        assertEquals(productList.size() - 1 , savedProducts.size() );
        verify(productDao, times(productList.size())).save(any(Product.class));
    }

}