package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    ProductService service;

    @Mock
    Model model;

    @InjectMocks
    ProductController productController;

    Product product;

    @BeforeEach
    void Setup() {
        product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    @Test
    void testEditProductPage() {
        when(service.findById(product.getProductId())).thenReturn(product);

        String viewName = productController.editProductPage(product.getProductId(), model);

        assertEquals("editProduct", viewName);
        verify(model, times(1)).addAttribute("product", product);
        verify(service, times(1)).findById(product.getProductId());
    }

    @Test
    void testEditProductPost() {
        Product updatedProduct = new Product();
        updatedProduct.setProductId(product.getProductId());
        updatedProduct.setProductName("Sampo Cap Bambang Edited");
        updatedProduct.setProductQuantity(200);

        when(service.update(updatedProduct)).thenReturn(updatedProduct);

        String viewName = productController.editProductPost(updatedProduct, model);

        assertEquals("redirect:list", viewName);
        verify(service, times(1)).update(updatedProduct);
    }

    @Test
    void testDeleteProduct() {
        when(service.delete(product.getProductId())).thenReturn(true);

        String viewName = productController.deleteProduct(product.getProductId());

        assertEquals("redirect:/product/list", viewName);
        verify(service, times(1)).delete(product.getProductId());
    }
}
