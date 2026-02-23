package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    Product product;

    @BeforeEach
    void Setup() {
        product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    @Test
    void testFindByIdIfFound() {
        when(productRepository.findById(product.getProductId())).thenReturn(product);

        Product found = productService.findById(product.getProductId());

        assertNotNull(found);
        assertEquals(product.getProductId(), found.getProductId());
        verify(productRepository, times(1)).findById(product.getProductId());
    }

    @Test
    void testFindByIdIfNotFound() {
        when(productRepository.findById("non-existent-id")).thenReturn(null);

        Product found = productService.findById("non-existent-id");

        assertNull(found);
        verify(productRepository, times(1)).findById("non-existent-id");
    }

    @Test
    void testEditProduct() {
        Product updatedProduct = new Product();
        updatedProduct.setProductId(product.getProductId());
        updatedProduct.setProductName("Sampo Cap Bambang Edited");
        updatedProduct.setProductQuantity(200);

        when(productRepository.update(updatedProduct)).thenReturn(updatedProduct);

        Product result = productService.update(updatedProduct);

        assertNotNull(result);
        assertEquals("Sampo Cap Bambang Edited", result.getProductName());
        assertEquals(200, result.getProductQuantity());
        verify(productRepository, times(1)).update(updatedProduct);
    }

    @Test
    void testEditProductIfNotFound() {
        Product updatedProduct = new Product();
        updatedProduct.setProductId("non-existent-id");
        updatedProduct.setProductName("Ghost Product");
        updatedProduct.setProductQuantity(999);

        when(productRepository.update(updatedProduct)).thenReturn(null);

        Product result = productService.update(updatedProduct);

        assertNull(result);
        verify(productRepository, times(1)).update(updatedProduct);
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.delete(product.getProductId())).thenReturn(true);

        boolean result = productService.delete(product.getProductId());

        assertTrue(result);
        verify(productRepository, times(1)).delete(product.getProductId());
    }

    @Test
    void testDeleteProductIfNotFound() {
        when(productRepository.delete("non-existent-id")).thenReturn(false);

        boolean result = productService.delete("non-existent-id");

        assertFalse(result);
        verify(productRepository, times(1)).delete("non-existent-id");
    }
}
