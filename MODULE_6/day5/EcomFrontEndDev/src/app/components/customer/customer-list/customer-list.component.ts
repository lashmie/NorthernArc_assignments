import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CustomerService } from '../../../services/customer.service';
import { CustomerResponseDTO } from '../../../dto/customer';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './customer-list.component.html',
  styleUrl: './customer-list.component.css'
})
export class CustomerListComponent implements OnInit {
  private readonly customerService = inject(CustomerService);

  readonly customers = signal<CustomerResponseDTO[]>([]);
  readonly error = signal('');
  readonly success = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.customerService.getAllCustomers().subscribe({
      next: (data) => {
        this.customers.set(data);
        this.error.set('');
      },
      error: () => {
        this.error.set('Unable to load customers.');
      }
    });
  }

  remove(id: number): void {
    const confirmed = window.confirm('Are you sure you want to delete this customer?');
    if (!confirmed) {
      return;
    }

    this.customerService.deleteCustomer(id).subscribe({
      next: () => {
        this.success.set('Customer deleted successfully.');
        this.load();
      },
      error: () => this.error.set('Delete failed.')
    });
  }
}
