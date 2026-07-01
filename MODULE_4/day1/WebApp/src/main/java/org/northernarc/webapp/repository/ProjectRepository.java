package org.northernarc.webapp.repository;

import org.northernarc.webapp.model.Employee;
import org.northernarc.webapp.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
Project findByName(String name);
//List<Project> findByEmployeesContaining(Employee employee); //// it created automatically
}
