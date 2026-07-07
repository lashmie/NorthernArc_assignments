// import { Component } from '@angular/core';
// import { FormsModule } from '@angular/forms';

// @Component({
//   selector: 'app-root',
//   standalone: true,
//   imports: [FormsModule],
//   templateUrl: './app.html'
// })
// export class App {
// }
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

import { EmployeeRequestDTO } from './dto/employeeResquestDTO';
import { EmployeeResponseDTO } from './dto/employeeResponseDTO';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html'
})
export class App implements OnInit {

  url = "http://localhost:8080/api/employees";

  employees: EmployeeResponseDTO[] = [];

  employee: EmployeeRequestDTO = {
    name: "",
    salary: 0
  };

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees() {
    this.http.get<EmployeeResponseDTO[]>(this.url)
      .subscribe(data => {
        this.employees = data;
      });
  }

  addEmployee() {
    this.http.post<EmployeeResponseDTO>(this.url, this.employee)
      .subscribe(() => {
        alert("Employee Added");

        this.employee = {
          name: "",
          salary: 0
        };

        this.loadEmployees();
      });
  }

  deleteEmployee(id: number) {
    this.http.delete(this.url + "/" + id, { responseType: 'text' })
      .subscribe(msg => {
        alert(msg);
        this.loadEmployees();
      });
  }

  sortSalary() {
    this.http.get<EmployeeResponseDTO[]>(this.url + "/salary-sort")
      .subscribe(data => {
        this.employees = data;
      });
  }

  reverseSort() {
    this.http.get<EmployeeResponseDTO[]>(this.url + "/salary-rsort")
      .subscribe(data => {
        this.employees = data;
      });
  }
}