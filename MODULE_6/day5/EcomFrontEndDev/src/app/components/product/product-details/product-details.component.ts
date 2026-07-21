import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProductService } from '../../../services/product.service';
import { ProductResponseDTO } from '../../../dto/product';
import { AuthService } from '../../../services/auth.service';
import { CartService } from '../../../services/cart.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-product-details',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './product-details.component.html',
  styleUrl: './product-details.component.css'
})
export class ProductDetailsComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly productService = inject(ProductService);
  private readonly authService = inject(AuthService);
  private readonly cartService = inject(CartService);

  readonly product = signal<ProductResponseDTO | null>(null);
  readonly error = signal('');
  readonly success = signal('');

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.productService.getProductById(id).subscribe({
      next: (data) => {
        this.product.set(data);
        this.error.set('');
      },
      error: (err: HttpErrorResponse) => {
        this.product.set(null);
        this.error.set(err.status === 404 ? 'Product not found.' : 'Unable to load product details.');
      }
    });
  }

  isUser(): boolean {
    return this.authService.getRole() === 'USER';
  }

  isAdmin(): boolean {
    return this.authService.getRole() === 'ADMIN';
  }

  addToCart(): void {
    const p = this.product();
    if (!p) {
      return;
    }

    this.cartService.addItem({
      productId: p.id,
      productName: p.name,
      quantity: 1,
      cost: p.cost
    });
    this.success.set('Product added to cart.');
  }
}
