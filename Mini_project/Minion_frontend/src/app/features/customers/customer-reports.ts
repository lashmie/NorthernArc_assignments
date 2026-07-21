import { Component, inject, signal } from '@angular/core';
import { CustomerService } from '../../core/services/customer.service';
import { Customer } from '../../core/models';

@Component({
  selector: 'app-customer-reports',
  imports: [],
  templateUrl: './customer-reports.html',
  styleUrl: './customer-reports.css'
})
export class CustomerReportsComponent {
  private customerService = inject(CustomerService);

  tab = signal<'overdue' | 'defaulters'>('overdue');
  loading = signal(true);
  error = signal<string | null>(null);
  customers = signal<Customer[]>([]);

  constructor() {
    this.load();
  }

  switchTab(tab: 'overdue' | 'defaulters'): void {
    if (this.tab() === tab) return;
    this.tab.set(tab);
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    const source$ = this.tab() === 'overdue'
      ? this.customerService.overdueCustomers()
      : this.customerService.topDefaulters();
    source$.subscribe({
      next: (list) => {
        this.customers.set(list ?? []);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err?.error?.message ?? 'Unable to load report.');
        this.loading.set(false);
      }
    });
  }
}
