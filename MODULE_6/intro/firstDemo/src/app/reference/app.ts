import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

import EmployeeDTO from './dto/EmployeeDTO';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html'
})
export class App implements OnInit {

  httpClient = inject(HttpClient);

  url = "http://localhost:8080/api/employees";

  protected employees: WritableSignal<EmployeeDTO[]> = signal([]);

  protected employee = {
    name: '',
    salary: 0
  };

  ngOnInit(): void {
    this.getAll();
  }

  getAll() {
    this.httpClient.get<EmployeeDTO[]>(this.url)
      .subscribe({
        next: (data) => {
          console.log(data);
          this.employees.set(data);
        },
        error: (err) => {
          console.log(err);
        }
      });
  }

  addEmployee() {

    this.httpClient.post<EmployeeDTO>(this.url, this.employee)
      .subscribe({
        next: () => {

          alert("Employee Added Successfully");

          this.employee = {
            name: '',
            salary: 0
          };

          this.getAll();

        },
        error: (err) => {
          console.log(err);
        }
      });

  }

  deleteEmployee(id: number) {

    this.httpClient.delete(this.url + "/" + id, { responseType: 'text' })
      .subscribe({
        next: (msg) => {
          alert(msg);
          this.getAll();
        },
        error: (err) => {
          console.log(err);
        }
      });

  }

  sortAscending() {

    this.httpClient.get<EmployeeDTO[]>(this.url + "/salary-sort")
      .subscribe({
        next: (data) => {
          this.employees.set(data);
        },
        error: (err) => {
          console.log(err);
        }
      });

  }

  sortDescending() {

    this.httpClient.get<EmployeeDTO[]>(this.url + "/salary-rsort")
      .subscribe({
        next: (data) => {
          this.employees.set(data);
        },
        error: (err) => {
          console.log(err);
        }
      });

  }

}