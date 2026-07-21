import { Component, inject, Input, signal, WritableSignal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { EmployeeServices } from '../../services/employee-services';
import EmployeeResponseDTO from '../dto/EmployeeResponseDTO';

@Component({
  selector: 'app-show-employee',
  imports: [],
  templateUrl: './show-employee.html',
  styleUrl: './show-employee.css',
})
export class ShowEmployee {
  employeeService: EmployeeServices = inject(EmployeeServices);
  @Input() employee!: EmployeeResponseDTO;
  removed = signal(false);
  deleteBlocked = signal(false);

  status: WritableSignal<{ loading: boolean; error?: string; success?: boolean }> = signal({
    loading: false,
    error: '',
    success: false,
  });

  remove(): void {
    if (this.deleteBlocked()) {
      return;
    }

    this.status.set({ loading: true, error: '', success: false });
    const employeeWithId = this.employee as EmployeeResponseDTO & { id?: number };

    if (employeeWithId.id !== undefined) {
      this.employeeService.remove(employeeWithId.id).subscribe({
        next: () => {
          console.log('Employee removed successfully.');
          this.status.set({ loading: false, success: true });
          this.removed.set(true);
          this.employeeService.employees.set(
            this.employeeService.employees().filter((emp) => {
              const listEmployee = emp as EmployeeResponseDTO & { id?: number };
              return listEmployee.id !== employeeWithId.id;
            })
          );
        },
        error: (error: unknown) => {
          console.error('Error removing employee:', error);
          const httpError = error as HttpErrorResponse;
          const rawMessage =
            typeof httpError?.error?.message === 'string'
              ? httpError.error.message
              : '';
          const isConstraintError =
            rawMessage.toLowerCase().includes('foreign key constraint') ||
            rawMessage.toLowerCase().includes('project_employees');
          const backendMessage = isConstraintError
            ? 'Employee is linked to a project. Remove project mapping first.'
            : 'Failed to remove employee.';

          if (isConstraintError) {
            this.deleteBlocked.set(true);
          }

          this.status.set({ loading: false, error: backendMessage, success: false });
        },
      });
      return;
    }

    this.status.set({ loading: false, error: 'Employee id is missing.', success: false });
  }
}


