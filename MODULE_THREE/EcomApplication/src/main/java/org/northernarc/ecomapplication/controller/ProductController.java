package org.northernarc.ecomapplication.controller;

import jakarta.validation.Valid;
import org.northernarc.ecomapplication.model.Product;
import org.northernarc.ecomapplication.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public Product saveProduct(@Valid @RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand) {
        return productService.searchProducts(name, category, brand);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @GetMapping("/search/by-name")
    public List<Product> searchByName(@RequestParam String name) {
        return productService.getProductsByName(name);
    }

    @GetMapping("/search/by-category")
    public List<Product> searchByCategory(@RequestParam String category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/search/by-brand")
    public List<Product> searchByBrand(@RequestParam String brand) {
        return productService.getProductsByBrand(brand);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}

