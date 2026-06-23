package org.northernarc.webapp.service;

import org.northernarc.webapp.expection.ResourceNotFoundException;
import org.northernarc.webapp.model.Employee;
import org.northernarc.webapp.model.Project;
import org.northernarc.webapp.repository.EmployeeRepository;
import org.northernarc.webapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectServiceDao {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    @Override
    public Project getProjectByName(String name) {
        Project project = projectRepository.findByName(name);
        if (project == null) {
            throw new ResourceNotFoundException("Project not found with name: " + name);
        }
        return project;
    }
    @Override
    public Project updateProject(Long id, Project project) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        project.setId(id);
        return projectRepository.save(project);
    }
    @Override
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }
    @Override
    public String assignProjectToClient(Long pid, Long cid) {
        return addEmployeeToProject(pid, cid);
    }

    @Override
    public String addEmployeeToProject(Long pid, Long eid) {
        Project project = projectRepository.findById(pid)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + pid));
        Employee employee = employeeRepository.findById(eid)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + eid));

        if (project.getEmployees() == null) {
            project.setEmployees(new ArrayList<>());
        }

        boolean alreadyAssigned = project.getEmployees().stream()
                .anyMatch(existing -> existing.getId().equals(employee.getId()));
        if (alreadyAssigned) {
            return "Employee already assigned to project";
        }

        project.getEmployees().add(employee);
        projectRepository.save(project);
        return "Employee assigned to project successfully";
    }
    @Override
    public String reassignProjectToClient(Long pid, Long cid) {
        Project project = projectRepository.findById(pid)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + pid));
        Employee client = employeeRepository.findById(cid)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + cid));

        if (project.getEmployees() == null) {
            project.setEmployees(new ArrayList<>());
        } else {
            project.getEmployees().clear();
        }

        project.getEmployees().add(client);
        projectRepository.save(project);
        return "Project reassigned successfully";
    }
    @Override
    public String unassignEmployeeFromProject(Long pid, Long eid) {
        Project project = projectRepository.findById(pid)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + pid));
        Employee employee = employeeRepository.findById(eid)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + eid));

        if (project.getEmployees() == null || project.getEmployees().isEmpty()) {
            return "No employees assigned to this project";
        }

        boolean removed = project.getEmployees().removeIf(existing -> existing.getId().equals(employee.getId()));
        if (!removed) {
            return "Employee is not assigned to this project";
        }

        projectRepository.save(project);
        return "Employee unassigned from project successfully";
    }
    @Override
    public List<Employee> getEmployeesByProjectName(String name) {
        Project project = getProjectByName(name);
        if (project.getEmployees() == null) {
            return new ArrayList<>();
        }
        return project.getEmployees();
    }
    @Override
    public List<Project> getProjectsByEmployeeId(Long eid) {
        Employee employee = employeeRepository.findById(eid)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + eid));
        return employee.getProjects();
    }
}