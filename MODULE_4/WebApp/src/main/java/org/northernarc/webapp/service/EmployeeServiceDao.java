package org.northernarc.webapp.service;

import org.northernarc.webapp.model.Employee;

import java.util.List;

public interface EmployeeServiceDao {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    Employee updateEmployee(Long id, Employee employee);
    void deleteEmployee(Long id);
}
