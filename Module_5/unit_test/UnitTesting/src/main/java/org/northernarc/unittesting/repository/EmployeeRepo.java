package org.northernarc.unittesting.repository;

import org.northernarc.unittesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {
}
