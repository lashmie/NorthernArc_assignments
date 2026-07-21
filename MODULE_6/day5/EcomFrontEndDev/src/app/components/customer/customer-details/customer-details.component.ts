import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CustomerService } from '../../../services/customer.service';
import { CustomerResponseDTO } from '../../../dto/customer';
import { OrderResponseDTO } from '../../../dto/order';

@Component({
  selector: 'app-customer-details',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './customer-details.component.html',
  styleUrl: './customer-details.component.css'
})
export class CustomerDetailsComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly customerService = inject(CustomerService);

  readonly customer = signal<CustomerResponseDTO | null>(null);
  readonly orders = signal<OrderResponseDTO[]>([]);
  readonly error = signal('');
  readonly loading = signal(false);

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loading.set(true);

    this.customerService.getCustomerById(id).subscribe({
      next: (data) => {
        this.customer.set(data);
        this.error.set('');
      },
      error: (err: HttpErrorResponse) => {
        this.customer.set(null);
        this.error.set(this.getErrorMessage(err, 'Unable to load customer details.'));
      }
    });

    this.customerService.getCustomerOrders(id).subscribe({
      next: (data) => {
        this.orders.set(data);
        this.loading.set(false);
      },
      error: (err: HttpErrorResponse) => {
        this.orders.set([]);
        this.loading.set(false);
        this.error.set(this.getErrorMessage(err, 'Unable to load customer orders.'));
      }
    });
  }

  private getErrorMessage(err: HttpErrorResponse, fallback: string): string {
    if (err.status === 401) {
      return 'Unauthorized. Please login again.';
    }
    if (err.status === 403) {
      return 'Access denied for customer details.';
    }
    if (err.status === 404) {
      return 'Customer not found.';
    }
    if (err.status >= 500) {
      return 'Server error while loading customer details.';
    }
    return fallback;
  }
}
