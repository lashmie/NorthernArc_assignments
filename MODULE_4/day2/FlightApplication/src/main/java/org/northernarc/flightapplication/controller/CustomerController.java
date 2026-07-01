package org.northernarc.flightapplication.controller;

import org.northernarc.flightapplication.dto.CustomerRequestDTO;
import org.northernarc.flightapplication.dto.CustomerResponseDTO;
import org.northernarc.flightapplication.service.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    private CustomerServiceImpl customerService;

    @GetMapping("/getall")
    public ResponseEntity<List<CustomerResponseDTO>> getAll() {
        List<CustomerResponseDTO> customers = customerService.findAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody CustomerRequestDTO customerRequestDTO) {
        CustomerResponseDTO createdCustomer = customerService.saveCustomer(customerRequestDTO);
        return ResponseEntity.ok(createdCustomer);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomer(@PathVariable Long id) {
        CustomerResponseDTO customer = customerService.findCustomerById(id);
        return ResponseEntity.ok(customer);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerRequestDTO customerRequestDTO) {
        CustomerResponseDTO updatedCustomer = customerService.updateCustomerById(id, customerRequestDTO);
        return ResponseEntity.ok(updatedCustomer);
    }
}
