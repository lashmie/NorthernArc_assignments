//package org.northernarc.webapp.controller;
//
//import org.northernarc.webapp.model.Project;
//import org.northernarc.webapp.service.ProjectServiceDao;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/projects")
//public class ProjectController {
//
//    @Autowired
//    private ProjectServiceDao projectService;
//
//    @PostMapping
//    public Project saveProject(@RequestBody Project project) {
//        return projectService.saveProject(project);
//    }
//
//    @GetMapping
//    public List<Project> getAllProjects() {
//        return projectService.getAllProjects();
//    }
//
//    @GetMapping("/{id}")
//    public Project getProjectById(@PathVariable Long id) {
//        return projectService.getProjectById(id);
//    }
//
//    @PutMapping("/{id}")
//    public Project updateProject(@PathVariable Long id,
//                                 @RequestBody Project project) {
//        return projectService.updateProject(id, project);
//    }
//
//    @DeleteMapping("/{id}")
//    public String deleteProject(@PathVariable Long id) {
//        projectService.deleteProject(id);
//        return "Project deleted successfully";
//    }
//}
package org.northernarc.webapp.controller;

import org.northernarc.webapp.model.Employee;
import org.northernarc.webapp.model.Project;
import org.northernarc.webapp.service.ProjectServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectServiceDao projectService;

    @PostMapping
    public ResponseEntity<Project> saveProject(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.saveProject(project));
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<Project> getProjectByName(@PathVariable String name) {
        return ResponseEntity.ok(projectService.getProjectByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
            @PathVariable Long id,
            @RequestBody Project project) {

        return ResponseEntity.ok(
                projectService.updateProject(id, project)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok("Project deleted successfully");
    }

    @GetMapping("/assign/{pid}/{cid}")
    public ResponseEntity<String> assignProjectToClient(@PathVariable Long pid, @PathVariable Long cid) {
        String result = projectService.assignProjectToClient(pid, cid);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{pid}/employees/{eid}")
    public ResponseEntity<String> addEmployeeToProject(@PathVariable Long pid, @PathVariable Long eid) {
        String result = projectService.addEmployeeToProject(pid, eid);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/reassign/{pid}/{cid}")
    public ResponseEntity<String> reassignProjectToClient(@PathVariable Long pid, @PathVariable Long cid) {
        String result = projectService.reassignProjectToClient(pid, cid);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/employeesByProject/{name}")
    public ResponseEntity<List<Employee>> getEmployeesByProjectName(@PathVariable String name) {
        List<Employee> employees = projectService.getEmployeesByProjectName(name);
        return ResponseEntity.ok(employees);
    }

    @DeleteMapping("/{pid}/employees/{eid}")
    public ResponseEntity<String> unassignEmployeeFromProject(@PathVariable Long pid, @PathVariable Long eid) {
        String result = projectService.unassignEmployeeFromProject(pid, eid);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/show/{eid}")
    public ResponseEntity<List<Project>> getProjectsByEmployeeId(@PathVariable Long eid) {
        List<Project> projects = projectService.getProjectsByEmployeeId(eid);
        return ResponseEntity.ok(projects);
    }

}