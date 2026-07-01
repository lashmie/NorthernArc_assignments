package org.northernarc.webapp.service;

import org.northernarc.webapp.dto.EmployeeRequestDto;
import org.northernarc.webapp.dto.EmployeeResponseDto;

import java.util.List;

public interface EmployeeServiceDao {
    EmployeeResponseDto saveEmployee(EmployeeRequestDto employeeRequestDto);
    List<EmployeeResponseDto> getAllEmployees();
    EmployeeResponseDto getEmployeeById(Long id);
    EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto employeeRequestDto);
    void deleteEmployee(Long id);
}
