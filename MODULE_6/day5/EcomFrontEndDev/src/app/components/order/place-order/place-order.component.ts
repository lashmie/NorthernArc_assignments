import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { OrderService } from '../../../services/order.service';
import { OrderRequestDTO } from '../../../dto/order';
import { CartService } from '../../../services/cart.service';
import { forkJoin } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-place-order',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent {
  private readonly orderService = inject(OrderService);
  private readonly router = inject(Router);
  private readonly cartService = inject(CartService);

  readonly error = signal('');
  readonly success = signal('');
  readonly loading = signal(false);
  readonly items = this.cartService.items;
  readonly total = this.cartService.totalCost;
  readonly isEmpty = computed(() => this.items().length === 0);
  readonly placedOrderIds = signal<number[]>([]);
  readonly estimatedDeliveryDate = signal('');

  submit(): void {
    if (this.isEmpty()) {
      this.error.set('Cart is empty. Add products before placing an order.');
      return;
    }

    const requests = this.items().map((item) => {
      const payload: OrderRequestDTO = {
        productId: item.productId,
        quantity: item.quantity
      };
      return this.orderService.placeOrder(payload);
    });

    this.loading.set(true);
    this.error.set('');
    this.success.set('');

    forkJoin(requests).subscribe({
      next: (responses) => {
        this.loading.set(false);
        this.cartService.clear();
        const ids = responses.map((order) => order.id).filter((id) => Number.isFinite(id));
        this.placedOrderIds.set(ids);
        this.estimatedDeliveryDate.set(this.getEstimatedDeliveryDate());
        this.success.set('Order placed successfully. Redirecting to My Orders...');
        setTimeout(() => this.router.navigate(['/orders']), 1200);
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);
        if (err.status === 400) {
          this.error.set('Invalid order request. Please verify cart quantities.');
          return;
        }
        if (err.status >= 500) {
          this.error.set('Server error while placing order. Please retry.');
          return;
        }
        this.error.set('Unable to place order.');
      }
    });
  }

  private getEstimatedDeliveryDate(): string {
    const date = new Date();
    date.setDate(date.getDate() + 5);
    return date.toISOString().split('T')[0];
  }
}
