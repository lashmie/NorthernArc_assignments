package org.northernarc.webapp.service;

import org.northernarc.webapp.model.Employee;
import org.northernarc.webapp.model.Project;

import java.util.List;

public interface ProjectServiceDao {
    Project saveProject(Project project);

    List<Project> getAllProjects();

    Project getProjectById(Long id);

    Project getProjectByName(String name);

    Project updateProject(Long id, Project project);

    void deleteProject(Long id);

    String assignProjectToClient(Long pid, Long cid);

    String addEmployeeToProject(Long pid, Long eid);

    String reassignProjectToClient(Long pid, Long cid);

    String unassignEmployeeFromProject(Long pid, Long eid);

    List<Employee> getEmployeesByProjectName(String name);

    List<Project> getProjectsByEmployeeId(Long eid);
}
