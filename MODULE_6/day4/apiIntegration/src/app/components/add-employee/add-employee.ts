import { Component, inject, signal, WritableSignal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { EmployeeServices } from '../../services/employee-services';
import EmployeeRequestDTO from '../dto/EmployeeRequestDTO';
import EmployeeResponseDTO from '../dto/EmployeeResponseDTO';

@Component({
  selector: 'app-add-employee',
  imports: [FormsModule],
  templateUrl: './add-employee.html',
  styleUrl: './add-employee.css',
})
export class AddEmployee {
  employeeService: EmployeeServices = inject(EmployeeServices);

  employee: EmployeeRequestDTO = {
    id: 0,
    name: '',
    salary: 0,
  };

  status: WritableSignal<{ loading: boolean; error?: string; success?: boolean }> = signal({
    loading: false,
    error: '',
    success: false,
  });

  onSubmit():void{
    this.status.set({loading:true, error:'', success:false});
    this.employeeService.add(this.employee).subscribe({
      next: (data) => {
        this.status.set({loading:false, success:true});
        console.log('Employee added successfully');
        const newEmployee: EmployeeResponseDTO = {
          name: data.name,
          salary: data.salary,
        };
        this.employeeService.employees.set([
          ...this.employeeService.employees(),
          newEmployee,
        ]);
      },
      error: (err) => {
        console.log(err);
        this.status.set({loading:false, error:'failed to add employee'});
      },
    });
  }

  onReset(): void {
    this.employee = {
      id: 0,
      name: '',
      salary: 0,
    };
    this.status.set({loading:false, error:'', success:false});
  }
}
