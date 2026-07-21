import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../../services/customer.service';
import { CustomerUpdateDTO } from '../../../dto/customer';

@Component({
  selector: 'app-update-customer',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './update-customer.component.html',
  styleUrl: './update-customer.component.css'
})
export class UpdateCustomerComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly customerService = inject(CustomerService);
  private readonly router = inject(Router);

  readonly error = signal('');
  readonly success = signal('');
  readonly loading = signal(false);
  private customerId = 0;
  model: CustomerUpdateDTO = { name: '', email: '', address: '' };

  ngOnInit(): void {
    this.customerId = Number(this.route.snapshot.paramMap.get('id'));
    this.customerService.getCustomerById(this.customerId).subscribe({
      next: (data) => {
        this.model = { name: data.name ?? '', email: data.email ?? '', address: data.address ?? '' };
      },
      error: (err: HttpErrorResponse) => this.error.set(this.getErrorMessage(err, 'Unable to load customer.'))
    });
  }

  submit(): void {
    if (!this.model.name || !this.model.email || !this.model.address) {
      this.error.set('All fields are required.');
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.success.set('');

    this.customerService.updateCustomer(this.customerId, this.model).subscribe({
      next: () => {
        this.loading.set(false);
        this.success.set('Customer updated successfully. Redirecting...');
        setTimeout(() => this.router.navigate(['/customers']), 600);
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);
        this.error.set(this.getErrorMessage(err, 'Update failed.'));
      }
    });
  }

  private getErrorMessage(err: HttpErrorResponse, fallback: string): string {
    if (err.status === 400) {
      return 'Invalid customer data. Please check all fields.';
    }
    if (err.status === 401) {
      return 'Unauthorized. Please login again.';
    }
    if (err.status === 403) {
      return 'Access denied for updating customer.';
    }
    if (err.status === 404) {
      return 'Customer not found.';
    }
    if (err.status >= 500) {
      return 'Server error while updating customer.';
    }
    return fallback;
  }
}
