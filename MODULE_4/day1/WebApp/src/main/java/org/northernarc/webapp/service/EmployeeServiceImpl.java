package org.northernarc.webapp.service;

import jakarta.transaction.Transactional;
import org.northernarc.webapp.dto.EmployeeRequestDto;
import org.northernarc.webapp.dto.EmployeeResponseDto;
import org.northernarc.webapp.expection.ResourceNotFoundException;
import org.northernarc.webapp.model.Employee;
import org.northernarc.webapp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeServiceDao {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponseDto saveEmployee(EmployeeRequestDto employeeRequestDto) {
        Employee employee = mapToEntity(employeeRequestDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return entityToMap(savedEmployee);
    }
//  public List<EmployeeResponseDto> getAllPage(int page,int size){
//        return employeeRepository.findAll(PageRequest.of(page,size,Sort.by("name").ascending()))
//                .stream()
//                .map(this::entityToMap)
//                .toList();
//  }

      public List<EmployeeResponseDto> getAllPage(int page,int size){
        return employeeRepository.findAll(PageRequest.of(page,size,Sort.by("name").ascending()))
                .stream()
                .map(this::entityToMap)
                .toList();
  }
    @Override
    public List<EmployeeResponseDto> getAllEmployees() {
        return employeeRepository.findAll(Sort.by("name").ascending())
                .stream()
                .map(this::entityToMap)
                .toList();
    }
    @Override
    public EmployeeResponseDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        return entityToMap(employee);
    }

    @Override
    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto employeeRequestDto) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        Employee mappedEmployee = mapToEntity(employeeRequestDto);
        existingEmployee.setName(mappedEmployee.getName());
        existingEmployee.setEmail(mappedEmployee.getEmail());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return entityToMap(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    private Employee mapToEntity(EmployeeRequestDto employeeRequestDto) {
        Employee employee = new Employee();
        employee.setName(employeeRequestDto.getName());
        employee.setEmail(employeeRequestDto.getEmail());
        employee.setProjects(new ArrayList<>());
        return employee;
    }

    private EmployeeResponseDto entityToMap(Employee employee) {
        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto();
        employeeResponseDto.setId(employee.getId());
        employeeResponseDto.setName(employee.getName());
        return employeeResponseDto;
    }

    @Transactional
    public int updateempEmailByName(String name,String email){
        return employeeRepository.updateEmployeeEmailByNames(name,email);
    }
}