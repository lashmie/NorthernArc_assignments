import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CustomerService } from '../../core/services/customer.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-customer-register',
  imports: [ReactiveFormsModule],
  templateUrl: './customer-register.html',
  styleUrl: './customer-register.css'
})
export class CustomerRegisterComponent {
  private fb = inject(FormBuilder);
  private customerService = inject(CustomerService);
  private auth = inject(AuthService);

  loading = signal(false);
  error = signal<string | null>(null);
  success = signal<string | null>(null);

  form = this.fb.nonNullable.group({
    customerName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    phoneNumber: ['', [Validators.required, Validators.pattern(/^[6-9]\d{9}$/)]],
    city: ['', [Validators.required]]
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading.set(true);
    this.error.set(null);
    this.success.set(null);
    // Backend /api/auth/register expects the same fields; USER role is assigned server-side.
    this.auth.register(this.form.getRawValue()).subscribe({
      next: (customer) => {
        this.loading.set(false);
        this.success.set(`Customer "${customer.customerName}" registered (ID ${customer.customerId ?? '—'}).`);
        this.form.reset({ customerName: '', email: '', password: '', phoneNumber: '', city: '' });
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(err?.error?.message ?? 'Registration failed.');
      }
    });
  }
}
