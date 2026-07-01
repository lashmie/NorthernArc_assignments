package org.northernarc.productrental.service;

import org.northernarc.productrental.dto.CustomerRentalSummaryDTO;
import org.northernarc.productrental.model.Customer;
import org.northernarc.productrental.model.Product;
import org.northernarc.productrental.model.RentalRecord;
import org.northernarc.productrental.model.RentalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRentalService {

    Page<Product> getAllProducts(Pageable pageable);

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByRentGreaterThan(Double amount);

    List<Product> getAvailableProducts();

    List<Product> getProductsWithNoOverdueRentals();

    List<Product> getProductsCurrentlyOverdue();

    int increaseRentPerDay(String category, Double amount);

    Product increaseRentPerDayByProductId(Long productId, Double amount);

    Product createProduct(Product product);

    Optional<Product> getProduct(Long productId);

    Product updateProduct(Long productId, Product product);

    void deleteProduct(Long productId);

    List<Customer> getCustomersByCity(String city);

    List<Customer> findFrequentCustomers(Long minRentals);

    List<Object[]> getTotalRentCollectedPerCity();

    List<Customer> getCustomersRentingMultipleCategories();

    Optional<CustomerRentalSummaryDTO> getCustomerRentalSummary(Long customerId);

    Customer createCustomer(Customer customer);

    Optional<Customer> getCustomer(Long customerId);

    Customer updateCustomer(Long customerId, Customer customer);

    void deleteCustomer(Long customerId);

    List<RentalRecord> getRentalsByStatus(RentalStatus status);

    RentalRecord createRental(RentalRecord rental);

    Optional<RentalRecord> getRental(Long rentalId);

    RentalRecord updateRental(Long rentalId, RentalRecord rental);

    void deleteRental(Long rentalId);
}

