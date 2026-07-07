package org.northernarc.unittesting.service;

import lombok.RequiredArgsConstructor;
import org.northernarc.unittesting.dto.EmployeeRequestDTO;
import org.northernarc.unittesting.dto.EmployeeResponseDTO;
import org.northernarc.unittesting.exception.EmployeeNotFoundException;
import org.northernarc.unittesting.model.Employee;
import org.northernarc.unittesting.repository.EmployeeRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepo employeeRepo;

    public EmployeeResponseDTO create(EmployeeRequestDTO requestDTO) {
        Employee employee = Employee.builder()
                .name(requestDTO.getName())
                .salary(requestDTO.getSalary())
                .build();

        return toResponse(employeeRepo.save(employee));
    }

    public List<EmployeeResponseDTO> getAll() {
        return employeeRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public EmployeeResponseDTO getById(Long id) {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        return toResponse(employee);
    }

    public EmployeeResponseDTO update(Long id, EmployeeRequestDTO requestDTO) {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        employee.setName(requestDTO.getName());
        employee.setSalary(requestDTO.getSalary());

        return toResponse(employeeRepo.save(employee));
    }

    public void delete(Long id) {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        employeeRepo.delete(employee);
    }

    private EmployeeResponseDTO toResponse(Employee employee) {
        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .salary(employee.getSalary())
                .build();
    }
}

