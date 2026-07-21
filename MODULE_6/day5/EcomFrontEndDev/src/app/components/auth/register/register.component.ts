import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { CustomerRequestDTO } from '../../../dto/customer';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly loading = signal(false);
  readonly error = signal('');
  readonly success = signal('');

  model: CustomerRequestDTO = {
    name: '',
    email: '',
    address: '',
    username: '',
    password: ''
  };

  submit(): void {
    const payload = this.model;
    const validationError = this.getValidationError(payload);
    if (validationError) {
      this.error.set(validationError);
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.success.set('');

    this.authService.register(payload).subscribe({
      next: () => {
        this.loading.set(false);
        this.success.set('Registration successful. You can login now.');
        this.model = { name: '', email: '', address: '', username: '', password: '' };
        setTimeout(() => this.router.navigate(['/login']), 700);
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);
        this.error.set(this.extractErrorMessage(err));
      }
    });
  }

  private extractErrorMessage(err: HttpErrorResponse): string {
    if (!err) {
      return 'Registration failed. Unknown error.';
    }

    if (typeof err.error === 'string' && err.error.trim()) {
      return err.error;
    }

    if (err.error?.message) {
      return String(err.error.message);
    }

    if (Array.isArray(err.error?.errors) && err.error.errors.length > 0) {
      return String(err.error.errors[0]);
    }

    if (err.status === 0) {
      return 'Cannot reach backend server at localhost:8080.';
    }

    if (err.status === 409) {
      return 'Username or email already exists.';
    }

    if (err.status === 400) {
      return 'Invalid registration data. Please check all fields.';
    }

    return `Registration failed (${err.status}). Please try again.`;
  }

  private getValidationError(payload: CustomerRequestDTO): string {
    if (payload.name.trim().length < 3) {
      return 'Name must be at least 3 characters.';
    }

    const isEmailValid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(payload.email);
    if (!isEmailValid) {
      return 'Please enter a valid email address.';
    }

    if (payload.address.trim().length < 5) {
      return 'Address must be at least 5 characters.';
    }

    if (payload.username.trim().length < 3) {
      return 'Username must be at least 3 characters.';
    }

    if (payload.password.length < 8) {
      return 'Password must be at least 8 characters.';
    }

    return '';
  }
}
