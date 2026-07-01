package org.northernarc.productrental.controller;

import jakarta.validation.Valid;
import org.northernarc.productrental.exception.ProductNotFoundException;
import org.northernarc.productrental.exception.RentalRecordNotFoundException;
import org.northernarc.productrental.model.Product;
import org.northernarc.productrental.model.RentalRecord;
import org.northernarc.productrental.model.RentalStatus;
import org.northernarc.productrental.service.ProductRentalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductRentalController {

    private final ProductRentalService productRentalService;

    public ProductRentalController(ProductRentalService productRentalService) {
        this.productRentalService = productRentalService;
    }

    // USER can view products and rental history
    @GetMapping("/products")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<Page<Product>> getAllProducts(
            @PageableDefault(size = 10, sort = "rentPerDay", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(productRentalService.getAllProducts(pageable));
    }

    @GetMapping("/rentals/status/{status}")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<List<RentalRecord>> getRentalsByStatus(@PathVariable RentalStatus status) {
        return ResponseEntity.ok(productRentalService.getRentalsByStatus(status));
    }

    @GetMapping("/products/{id}")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productRentalService.getProduct(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id)));
    }

    @GetMapping("/rentals/{id}")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<RentalRecord> getRental(@PathVariable Long id) {
        return ResponseEntity.ok(productRentalService.getRental(id)
                .orElseThrow(() -> new RentalRecordNotFoundException("Rental record not found with id: " + id)));
    }

    // MANAGER can update rent rates
    @PutMapping("/products/category/{category}/rent")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<String> increaseRentPerDay(@PathVariable String category,
                                                     @RequestParam Double amount) {
        int updated = productRentalService.increaseRentPerDay(category, amount);
        return ResponseEntity.ok("Updated " + updated + " products");
    }

    @PutMapping("/products/{id}/rent")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<Product> increaseRentPerDayByProductId(@PathVariable Long id,
                                                                  @RequestParam Double amount) {
        return ResponseEntity.ok(productRentalService.increaseRentPerDayByProductId(id, amount));
    }

    // ADMIN can delete products
    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productRentalService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/products")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        return ResponseEntity.ok(productRentalService.createProduct(product));
    }
}

