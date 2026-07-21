import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { EmployeeService } from '../../services/employee-service';
import EmployeeDTO from '../../dto/EmployeeDTO';
import { ShowEmployee } from '../show-employee/show-employee';

 
 
@Component({
  selector: 'app-show-employees',
  imports: [ShowEmployee],
  templateUrl: './show-employees.html',
  styleUrl: './show-employees.css',
})
export class ShowEmployees implements OnInit {

  employeeService: EmployeeService = inject(EmployeeService);

  protected pstatus: WritableSignal<{loading: boolean,error?: string,success?: boolean}> = signal({loading: false});

  ngOnInit() {
    this.pstatus.set({loading: true});
    setTimeout(() => {
    this.getAllEmployees();
    }, 1000);
  }

  getAllEmployees() {
    this.employeeService.getAll().subscribe({
      next: (data: EmployeeDTO[]) => {
        this.pstatus.set({loading: false, success: true});
        console.log(data);
        this.employeeService.employees.set(data);
      },
      error: (error) => {
        console.error('Error fetching employee details:', error);
        this.pstatus.set({loading: false, error: 'Failed to fetch employee details.'});
      },
      complete: () => {
        console.log('Employee details fetched successfully.');
      },
    });
  }
}