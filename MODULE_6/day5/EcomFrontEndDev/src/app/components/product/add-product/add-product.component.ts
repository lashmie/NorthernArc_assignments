import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ProductService } from '../../../services/product.service';
import { ProductRequestDTO } from '../../../dto/product';

@Component({
  selector: 'app-add-product',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-product.component.html',
  styleUrl: './add-product.component.css'
})
export class AddProductComponent {
  private readonly productService = inject(ProductService);
  private readonly router = inject(Router);

  readonly error = signal('');
  readonly success = signal('');
  readonly loading = signal(false);
  model: ProductRequestDTO = { name: '', brand: '', category: '', cost: 0 };

  submit(): void {
    if (!this.model.name || !this.model.brand || !this.model.category || this.model.cost <= 0) {
      this.error.set('Please enter valid product details.');
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.success.set('');

    this.productService.addProduct(this.model).subscribe({
      next: () => {
        this.loading.set(false);
        this.success.set('Product created successfully. Redirecting...');
        setTimeout(() => this.router.navigate(['/products']), 600);
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);
        this.error.set(this.getErrorMessage(err));
      }
    });
  }

  private getErrorMessage(err: HttpErrorResponse): string {
    if (err.status === 400) {
      return 'Invalid product data. Please check all fields.';
    }
    if (err.status === 401) {
      return 'Unauthorized. Please login again.';
    }
    if (err.status === 403) {
      return 'Access denied for adding product.';
    }
    if (err.status >= 500) {
      return 'Server error while creating product.';
    }
    return 'Unable to create product.';
  }
}
