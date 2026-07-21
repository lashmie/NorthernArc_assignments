import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent {
  private readonly cartService = inject(CartService);
  private readonly router = inject(Router);

  readonly items = this.cartService.items;
  readonly totalCost = this.cartService.totalCost;
  readonly isEmpty = computed(() => this.items().length === 0);
  readonly message = signal('');

  updateQuantity(productId: number, quantityValue: string): void {
    const quantity = Number(quantityValue);
    if (!Number.isFinite(quantity) || quantity < 1) {
      this.message.set('Quantity must be at least 1.');
      return;
    }

    this.cartService.updateQuantity(productId, quantity);
    this.message.set('');
  }

  removeItem(productId: number): void {
    this.cartService.removeItem(productId);
    this.message.set('Item removed from cart.');
  }

  goToCheckout(): void {
    if (this.isEmpty()) {
      this.message.set('Your cart is empty. Add products before checkout.');
      return;
    }

    this.router.navigate(['/checkout']);
  }
}
