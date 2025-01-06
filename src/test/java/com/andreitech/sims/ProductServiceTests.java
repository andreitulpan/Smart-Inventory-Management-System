package com.andreitech.sims;

import com.andreitech.sims.entity.Product;
import com.andreitech.sims.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @Test
    public void testAddProduct() {
        Product product = new Product();
        product.setName("Product #1");
        product.setCategory("test-category");
        product.setQuantity(10);
        product.setPrice(100.0);

        Product savedProduct = productService.saveProduct(product);
        assertNotNull(savedProduct.getId());
        assertEquals(product.getName(), savedProduct.getName());
    }

    // Other tests...
}