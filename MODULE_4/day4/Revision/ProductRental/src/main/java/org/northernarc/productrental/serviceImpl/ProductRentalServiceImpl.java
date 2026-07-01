package org.northernarc.productrental.serviceImpl;

import org.northernarc.productrental.dto.CustomerRentalSummaryDTO;
import org.northernarc.productrental.exception.CustomerNotFoundException;
import org.northernarc.productrental.exception.ProductNotFoundException;
import org.northernarc.productrental.exception.RentalRecordNotFoundException;
import org.northernarc.productrental.model.Customer;
import org.northernarc.productrental.model.Product;
import org.northernarc.productrental.model.RentalRecord;
import org.northernarc.productrental.model.RentalStatus;
import org.northernarc.productrental.repository.CustomerRepository;
import org.northernarc.productrental.repository.ProductRepository;
import org.northernarc.productrental.repository.RentalRecordRepository;
import org.northernarc.productrental.service.ProductRentalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductRentalServiceImpl implements ProductRentalService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final RentalRecordRepository rentalRecordRepository;

    public ProductRentalServiceImpl(ProductRepository productRepository,
                                    CustomerRepository customerRepository,
                                    RentalRecordRepository rentalRecordRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.rentalRecordRepository = rentalRecordRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAllProducts(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByRentGreaterThan(Double amount) {
        return productRepository.findByRentPerDayGreaterThan(amount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAvailableProducts() {
        return productRepository.findByAvailable(true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsWithNoOverdueRentals() {
        return productRepository.findProductsWithNoOverdueRentals();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsCurrentlyOverdue() {
        return productRepository.findProductsCurrentlyOverdue();
    }

    @Override
    public int increaseRentPerDay(String category, Double amount) {
        return productRepository.increaseRentPerDay(category, amount);
    }

    @Override
    public Product increaseRentPerDayByProductId(Long productId, Double amount) {
        return productRepository.findById(productId)
                .map(product -> {
                    product.setRentPerDay(product.getRentPerDay() + amount);
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProduct(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Product updateProduct(Long productId, Product productDetails) {
        return productRepository.findById(productId)
                .map(product -> {
                    product.setProductName(productDetails.getProductName());
                    product.setCategory(productDetails.getCategory());
                    product.setRentPerDay(productDetails.getRentPerDay());
                    product.setAvailable(productDetails.getAvailable());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getCustomersByCity(String city) {
        return customerRepository.findByCity(city);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> findFrequentCustomers(Long minRentals) {
        return customerRepository.findFrequentCustomers(minRentals);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getTotalRentCollectedPerCity() {
        return customerRepository.findTotalRentCollectedPerCity();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getCustomersRentingMultipleCategories() {
        return customerRepository.findCustomersRentingMultipleCategories();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerRentalSummaryDTO> getCustomerRentalSummary(Long customerId) {
        return customerRepository.findCustomerRentalSummary(customerId);
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> getCustomer(Long customerId) {
        return customerRepository.findById(customerId);
    }

    @Override
    public Customer updateCustomer(Long customerId, Customer customerDetails) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    customer.setCustomerName(customerDetails.getCustomerName());
                    customer.setEmail(customerDetails.getEmail());
                    customer.setCity(customerDetails.getCity());
                    customer.setPassword(customerDetails.getPassword());
                    return customerRepository.save(customer);
                })
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
    }

    @Override
    public void deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
        customerRepository.delete(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalRecord> getRentalsByStatus(RentalStatus status) {
        return rentalRecordRepository.findByStatus(status);
    }

    @Override
    public RentalRecord createRental(RentalRecord rental) {
        return rentalRecordRepository.save(rental);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RentalRecord> getRental(Long rentalId) {
        return rentalRecordRepository.findById(rentalId);
    }

    @Override
    public RentalRecord updateRental(Long rentalId, RentalRecord rentalDetails) {
        return rentalRecordRepository.findById(rentalId)
                .map(rental -> {
                    rental.setRentDate(rentalDetails.getRentDate());
                    rental.setExpectedReturnDate(rentalDetails.getExpectedReturnDate());
                    rental.setActualReturnDate(rentalDetails.getActualReturnDate());
                    rental.setStatus(rentalDetails.getStatus());
                    return rentalRecordRepository.save(rental);
                })
                .orElseThrow(() -> new RentalRecordNotFoundException("Rental record not found with id: " + rentalId));
    }

    @Override
    public void deleteRental(Long rentalId) {
        RentalRecord rental = rentalRecordRepository.findById(rentalId)
                .orElseThrow(() -> new RentalRecordNotFoundException("Rental record not found with id: " + rentalId));
        rentalRecordRepository.delete(rental);
    }
}

