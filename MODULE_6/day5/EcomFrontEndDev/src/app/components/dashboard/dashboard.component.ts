import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ProductService } from '../../services/product.service';
import { CustomerService } from '../../services/customer.service';
import { OrderService } from '../../services/order.service';
import { OrderResponseDTO } from '../../dto/order';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly productService = inject(ProductService);
  private readonly customerService = inject(CustomerService);
  private readonly orderService = inject(OrderService);

  readonly role = signal(this.authService.getRole());
  readonly isAdmin = computed(() => this.role() === 'ADMIN');
  readonly error = signal('');
  readonly loading = signal(false);
  readonly totalProducts = signal(0);
  readonly totalCustomers = signal(0);
  readonly totalOrders = signal(0);
  readonly recentOrders = signal<OrderResponseDTO[]>([]);

  ngOnInit(): void {
    this.loadSummary();
  }

  private loadSummary(): void {
    this.loading.set(true);
    this.error.set('');

    this.productService.getAllProducts().subscribe({
      next: (data) => this.totalProducts.set(data.length)
    });

    if (this.isAdmin()) {
      this.customerService.getAllCustomers().subscribe({
        next: (data) => this.totalCustomers.set(data.length)
      });

      this.orderService.getAllOrders().subscribe({
        next: (orders) => {
          this.totalOrders.set(orders.length);
          this.recentOrders.set(orders.slice(0, 5));
          this.loading.set(false);
        }
      });
      return;
    }

    this.authService.resolveUserId().subscribe({
      next: (customerId) => {
        if (!customerId) {
          this.loading.set(false);
          this.error.set('User ID not present in token and no profile lookup endpoint found. Showing limited dashboard.');
          this.totalOrders.set(0);
          this.recentOrders.set([]);
          return;
        }

        this.orderService.getOrdersByCustomer(customerId).subscribe({
          next: (orders) => {
            this.totalOrders.set(orders.length);
            this.recentOrders.set(orders.slice(0, 5));
            this.loading.set(false);
          },
          error: () => {
            this.loading.set(false);
            this.error.set('Unable to load your order summary.');
          }
        });
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Unable to resolve user context from token.');
      }
    });
  }
}
