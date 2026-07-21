import { Injectable, computed, signal } from '@angular/core';
import { CartItem } from '../dto/cart-item';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private readonly storageKey = 'ecom_cart';
  readonly items = signal<CartItem[]>(this.loadCart());
  readonly itemCount = computed(() => this.items().reduce((sum, item) => sum + item.quantity, 0));
  readonly totalCost = computed(() => this.items().reduce((sum, item) => sum + item.quantity * item.cost, 0));

  addItem(newItem: CartItem): void {
    const existingItems = this.items();
    const index = existingItems.findIndex((item) => item.productId === newItem.productId);

    if (index >= 0) {
      const updated = [...existingItems];
      updated[index] = { ...updated[index], quantity: updated[index].quantity + newItem.quantity };
      this.items.set(updated);
    } else {
      this.items.set([...existingItems, newItem]);
    }

    this.persist();
  }

  updateQuantity(productId: number, quantity: number): void {
    if (quantity <= 0) {
      this.removeItem(productId);
      return;
    }

    const updated = this.items().map((item) =>
      item.productId === productId ? { ...item, quantity } : item
    );
    this.items.set(updated);
    this.persist();
  }

  removeItem(productId: number): void {
    this.items.set(this.items().filter((item) => item.productId !== productId));
    this.persist();
  }

  clear(): void {
    this.items.set([]);
    localStorage.removeItem(this.storageKey);
  }

  private loadCart(): CartItem[] {
    const raw = localStorage.getItem(this.storageKey);
    if (!raw) {
      return [];
    }

    try {
      const parsed = JSON.parse(raw) as CartItem[];
      return Array.isArray(parsed) ? parsed : [];
    } catch {
      return [];
    }
  }

  private persist(): void {
    localStorage.setItem(this.storageKey, JSON.stringify(this.items()));
  }
}
