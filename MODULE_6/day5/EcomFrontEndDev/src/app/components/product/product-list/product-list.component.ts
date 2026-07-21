import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ProductService } from '../../../services/product.service';
import { ProductResponseDTO } from '../../../dto/product';
import { AuthService } from '../../../services/auth.service';
import { CartService } from '../../../services/cart.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent implements OnInit {
  private readonly productService = inject(ProductService);
  private readonly authService = inject(AuthService);
  private readonly cartService = inject(CartService);

  readonly products = signal<ProductResponseDTO[]>([]);
  readonly error = signal('');
  readonly success = signal('');
  readonly isAdmin = computed(() => this.authService.getRole() === 'ADMIN');
  readonly isUser = computed(() => this.authService.getRole() === 'USER');
  searchType: 'all' | 'name' | 'brand' | 'category' = 'all';
  searchText = '';

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.productService.getAllProducts().subscribe({
      next: (data) => {
        this.products.set(data);
        this.error.set('');
        this.success.set('');
      },
      error: (err) => this.error.set(this.getErrorMessage(err, 'Unable to load products.'))
    });
  }

  search(): void {
    const keyword = this.searchText.trim();
    if (!keyword || this.searchType === 'all') {
      this.load();
      return;
    }

    const request =
      this.searchType === 'name'
        ? this.productService.getProductsByName(keyword)
        : this.searchType === 'brand'
          ? this.productService.getProductsByBrand(keyword)
          : this.productService.getProductsByCategory(keyword);

    request.subscribe({
      next: (data) => {
        this.products.set(data);
        this.error.set('');
      },
      error: (err) => this.error.set(this.getErrorMessage(err, 'Unable to filter products with current search value.'))
    });
  }

  clearSearch(): void {
    this.searchType = 'all';
    this.searchText = '';
    this.load();
  }

  remove(id: number): void {
    const confirmed = window.confirm('Are you sure you want to delete this product?');
    if (!confirmed) {
      return;
    }

    this.productService.deleteProduct(id).subscribe({
      next: () => {
        this.success.set('Product deleted successfully.');
        this.load();
      },
      error: (err) => this.error.set(this.getErrorMessage(err, 'Delete failed.'))
    });
  }

  addToCart(product: ProductResponseDTO): void {
    this.cartService.addItem({
      productId: product.id,
      productName: product.name,
      quantity: 1,
      cost: product.cost
    });
    this.success.set(`${product.name} added to cart.`);
    this.error.set('');
  }

  private getErrorMessage(err: HttpErrorResponse, fallback: string): string {
    if (err.status === 400) {
      return 'Invalid request. Please check your input and try again.';
    }

    if (err.status === 403) {
      return 'Access denied for this action.';
    }

    if (err.status === 404) {
      return 'Product not found.';
    }

    if (err.status >= 500) {
      return 'Server error. Please try again in a moment.';
    }

    return fallback;
  }
}
