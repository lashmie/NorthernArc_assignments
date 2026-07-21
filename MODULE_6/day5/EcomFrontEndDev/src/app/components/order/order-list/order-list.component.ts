import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { OrderService } from '../../../services/order.service';
import { OrderResponseDTO } from '../../../dto/order';
import { AuthService } from '../../../services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-order-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './order-list.component.html',
  styleUrl: './order-list.component.css'
})
export class OrderListComponent implements OnInit {
  private readonly orderService = inject(OrderService);
  private readonly authService = inject(AuthService);

  readonly orders = signal<OrderResponseDTO[]>([]);
  readonly error = signal('');
  readonly loading = signal(false);
  readonly role = computed(() => this.authService.getRole());
  readonly isAdmin = computed(() => this.role() === 'ADMIN');
  readonly isUser = computed(() => this.role() === 'USER');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');

    if (this.isAdmin()) {
      this.orderService.getAllOrders().subscribe({
        next: (data) => {
          this.orders.set(data);
          this.loading.set(false);
        },
        error: (err: HttpErrorResponse) => {
          this.loading.set(false);
          this.error.set(this.getErrorMessage(err, 'Unable to load orders.'));
        }
      });
      return;
    }

    this.authService.resolveUserId().subscribe({
      next: (customerId) => {
        if (!customerId) {
          this.error.set('Unable to identify customer id from token. Backend must include a numeric user-id claim or provide /api/ecom/customer/me.');
          this.orders.set([]);
          this.loading.set(false);
          return;
        }

        this.orderService.getOrdersByCustomer(customerId).subscribe({
          next: (data) => {
            this.orders.set(data);
            this.loading.set(false);
          },
          error: (err: HttpErrorResponse) => {
            this.loading.set(false);
            this.error.set(this.getErrorMessage(err, 'Unable to load your orders.'));
          }
        });
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Unable to resolve user context from token.');
      }
    });
  }

  cancel(orderId: number): void {
    this.orderService.cancelOrder(orderId).subscribe({
      next: () => this.load(),
      error: (err: HttpErrorResponse) => this.error.set(this.getErrorMessage(err, 'Unable to cancel order.'))
    });
  }

  private getErrorMessage(err: HttpErrorResponse, fallback: string): string {
    if (err.status === 400) {
      return 'Bad request. Please verify order input.';
    }
    if (err.status === 401) {
      return 'Unauthorized. Please login again.';
    }
    if (err.status === 403) {
      return 'Access denied for this order action.';
    }
    if (err.status === 404) {
      return 'Order not found.';
    }
    if (err.status >= 500) {
      return 'Server error while processing orders.';
    }
    return fallback;
  }
}
