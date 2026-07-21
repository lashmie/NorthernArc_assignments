import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CustomerService } from '../../../services/customer.service';
import { CustomerRequestDTO } from '../../../dto/customer';

@Component({
  selector: 'app-add-customer',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-customer.component.html',
  styleUrl: './add-customer.component.css'
})
export class AddCustomerComponent {
  private readonly customerService = inject(CustomerService);
  private readonly router = inject(Router);

  readonly error = signal('');
  readonly success = signal('');
  readonly loading = signal(false);

  model: CustomerRequestDTO = {
    name: '',
    email: '',
    address: '',
    username: '',
    password: ''
  };

  submit(): void {
    const payload = this.model;
    if (!payload.name || !payload.email || !payload.address || !payload.username || !payload.password) {
      this.error.set('All fields are required.');
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.success.set('');

    this.customerService.addCustomer(payload).subscribe({
      next: () => {
        this.loading.set(false);
        this.success.set('Customer created successfully. Redirecting...');
        setTimeout(() => this.router.navigate(['/customers']), 600);
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);
        this.error.set(this.getErrorMessage(err));
      }
    });
  }

  private getErrorMessage(err: HttpErrorResponse): string {
    if (err.status === 400) {
      return 'Invalid customer data. Please check all fields.';
    }
    if (err.status === 401) {
      return 'Unauthorized. Please login again.';
    }
    if (err.status === 403) {
      return 'Access denied for creating customer.';
    }
    if (err.status === 409) {
      return 'Username or email already exists.';
    }
    if (err.status >= 500) {
      return 'Server error while creating customer.';
    }
    return 'Unable to create customer.';
  }
}
