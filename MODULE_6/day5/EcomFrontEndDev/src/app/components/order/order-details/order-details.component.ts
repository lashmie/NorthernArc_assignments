import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { OrderService } from '../../../services/order.service';
import { OrderResponseDTO } from '../../../dto/order';
import { FormsModule } from '@angular/forms';
import { OrderItemService } from '../../../services/order-item.service';
import { OrderItemResponseDTO } from '../../../dto/order-item';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-order-details',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './order-details.component.html',
  styleUrl: './order-details.component.css'
})
export class OrderDetailsComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly orderService = inject(OrderService);
  private readonly orderItemService = inject(OrderItemService);
  private readonly authService = inject(AuthService);

  readonly order = signal<OrderResponseDTO | null>(null);
  readonly orderItems = signal<OrderItemResponseDTO[]>([]);
  readonly error = signal('');
  readonly success = signal('');
  readonly showCancelConfirm = signal(false);
  readonly role = computed(() => this.authService.getRole());
  readonly isAdmin = computed(() => this.role() === 'ADMIN');
  readonly isUser = computed(() => this.role() === 'USER');
  readonly orderTotal = computed(() =>
    this.orderItems().reduce((sum, item) => sum + item.quantity * (item.unitPrice ?? 0), 0)
  );
  selectedStatus = 'PENDING';
  readonly statusOptions = ['PENDING', 'COMPLETED', 'CANCELLED'];

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.orderService.getOrderById(id).subscribe({
      next: (data) => {
        this.order.set(data);
        this.selectedStatus = data.status;
      },
      error: () => this.error.set('Unable to load order.')
    });

    this.orderItemService.getItemsByOrderId(id).subscribe({
      next: (items) => this.orderItems.set(items),
      error: () => this.error.set('Unable to load order items.')
    });
  }

  updateStatus(): void {
    const current = this.order();
    if (!current?.id || !this.selectedStatus) {
      return;
    }

    this.orderService.updateOrder(current.id, { status: this.selectedStatus }).subscribe({
      next: (updated) => {
        this.order.set(updated);
        this.success.set('Order status updated successfully.');
        this.error.set('');
      },
      error: () => this.error.set('Unable to update order status.')
    });
  }

  cancel(id: number): void {
    const current = this.order();
    if (!current || current.status !== 'PENDING') {
      this.error.set('Only PENDING orders can be cancelled.');
      return;
    }

    this.orderService.cancelOrder(id).subscribe({
      next: () => {
        this.success.set('Order cancelled successfully.');
        this.error.set('');
        this.order.set({ ...current, status: 'CANCELLED' });
        this.showCancelConfirm.set(false);
      },
      error: () => this.error.set('Unable to cancel order.')
    });
  }

  openCancelConfirm(): void {
    this.showCancelConfirm.set(true);
  }

  closeCancelConfirm(): void {
    this.showCancelConfirm.set(false);
  }

  confirmCancel(): void {
    const current = this.order();
    if (!current) {
      return;
    }

    this.cancel(current.id);
  }
}
