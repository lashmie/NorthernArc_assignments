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
import jakarta.validation.Valid;
import org.northernarc.webapp.dto.EmployeeRequestDto;
import org.northernarc.webapp.dto.EmployeeResponseDto;
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
   // @PostMapping
//    public EmployeeResponseDto saveEmployee(@Valid @RequestBody EmployeeRequestDto employeeRequestDto) {
//        return employeeService.saveEmployee(employeeRequestDto);
//    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> saveEmployee(@Valid @RequestBody EmployeeRequestDto employeeRequestDto) {
        return ResponseEntity.ok(employeeService.saveEmployee(employeeRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDto employeeRequestDto) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, employeeRequestDto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee deleted successfully");
    }
}