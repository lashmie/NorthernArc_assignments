import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../../services/product.service';
import { ProductUpdateDTO } from '../../../dto/product';

@Component({
  selector: 'app-update-product',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './update-product.component.html',
  styleUrl: './update-product.component.css'
})
export class UpdateProductComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly productService = inject(ProductService);
  private readonly router = inject(Router);

  readonly error = signal('');
  readonly success = signal('');
  readonly loading = signal(false);
  private productId = 0;
  model: ProductUpdateDTO = { name: '', brand: '', category: '', cost: 0 };

  ngOnInit(): void {
    this.productId = Number(this.route.snapshot.paramMap.get('id'));
    this.productService.getProductById(this.productId).subscribe({
      next: (data) => {
        this.model = {
          name: data.name ?? '',
          brand: data.brand ?? '',
          category: data.category ?? '',
          cost: data.cost ?? 0
        };
      },
      error: (err: HttpErrorResponse) => this.error.set(this.getErrorMessage(err, 'Unable to load product.'))
    });
  }

  submit(): void {
    if (!this.model.name || !this.model.brand || !this.model.category || (this.model.cost ?? 0) <= 0) {
      this.error.set('Please enter valid product details.');
      return;
    }

    this.loading.set(true);
    this.error.set('');

    this.productService.updateProduct(this.productId, this.model).subscribe({
      next: () => {
        this.loading.set(false);
        this.success.set('Product updated successfully. Redirecting...');
        setTimeout(() => this.router.navigate(['/products']), 600);
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);
        this.error.set(this.getErrorMessage(err, 'Update failed.'));
      }
    });
  }

  private getErrorMessage(err: HttpErrorResponse, fallback: string): string {
    if (err.status === 400) {
      return 'Invalid product data. Please check all fields.';
    }
    if (err.status === 401) {
      return 'Unauthorized. Please login again.';
    }
    if (err.status === 403) {
      return 'Access denied for updating product.';
    }
    if (err.status === 404) {
      return 'Product not found.';
    }
    if (err.status >= 500) {
      return 'Server error while updating product.';
    }
    return fallback;
  }
}
