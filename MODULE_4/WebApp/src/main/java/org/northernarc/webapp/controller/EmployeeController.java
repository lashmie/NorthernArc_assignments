//package org.northernarc.webapp.controller;
//
//import org.northernarc.webapp.model.Employee;
//import org.northernarc.webapp.service.EmployeeServiceDao;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/employees")
//public class EmployeeController {
//
//    @Autowired
//    private EmployeeServiceDao employeeService;
//
//    @PostMapping
//    public Employee saveEmployee(@RequestBody Employee employee) {
//        return employeeService.saveEmployee(employee);
//    }
//
//    @GetMapping
//    public List<Employee> getAllEmployees() {
//        return employeeService.getAllEmployees();
//    }
//
//    @GetMapping("/{id}")
//    public Employee getEmployeeById(@PathVariable Long id) {
//        return employeeService.getEmployeeById(id);
//    }
//
//    @PutMapping("/{id}")
//    public Employee updateEmployee(@PathVariable Long id,
//                                   @RequestBody Employee employee) {
//        return employeeService.updateEmployee(id, employee);
//    }
//
//    @DeleteMapping("/{id}")
//    public String deleteEmployee(@PathVariable Long id) {
//        employeeService.deleteEmployee(id);
//        return "Employee deleted successfully";
//    }
//}
package org.northernarc.webapp.controller;
import org.northernarc.webapp.model.Employee;
import org.northernarc.webapp.service.EmployeeServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeServiceDao employeeService;

    @PostMapping
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.saveEmployee(employee));
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, employee)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee deleted successfully");
    }
}