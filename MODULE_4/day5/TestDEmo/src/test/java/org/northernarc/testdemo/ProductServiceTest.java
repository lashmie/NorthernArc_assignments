package org.northernarc.testdemo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.northernarc.testdemo.model.Product;
import org.northernarc.testdemo.repository.ProductRepo;
import org.northernarc.testdemo.service.ProductService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private ProductService productService;

    @Test
    void saveAndGetProduct() {
        Product product = new Product("1", "Phone");
        Mockito.when(productRepo.saveProduct(product)).thenReturn(product);

        Product saved = productService.saveProduct(product);

        assertEquals("1", saved.getId());
        assertEquals("Phone", saved.getName());
        Mockito.verify(productRepo).saveProduct(product);
    }

    @Test
    void updateProduct() {
        Product updatedDetails = new Product("9", "Laptop");
        Product updated = new Product("1", "Laptop");
        when(productRepo.updateProduct("1", updatedDetails)).thenReturn(Optional.of(updated));

        Product result = productService.updateProduct("1", updatedDetails).orElseThrow();

        assertEquals("1", result.getId());
        assertEquals("Laptop", result.getName());
        verify(productRepo).updateProduct("1", updatedDetails);
    }

    @Test
    void deleteProduct() {
        when(productRepo.deleteProduct("1")).thenReturn(true);

        boolean deleted = productService.deleteProduct("1");

        assertTrue(deleted);
        verify(productRepo).deleteProduct("1");
    }

}
