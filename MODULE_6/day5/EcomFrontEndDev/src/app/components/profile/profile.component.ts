import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CustomerService } from '../../services/customer.service';
import { CustomerResponseDTO, CustomerUpdateDTO } from '../../dto/customer';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly customerService = inject(CustomerService);
  private readonly router = inject(Router);

  readonly profile = signal<CustomerResponseDTO | null>(null);
  readonly error = signal('');
  readonly success = signal('');
  readonly loading = signal(false);
  readonly editMode = signal(false);
  private customerId = 0;

  model: CustomerUpdateDTO = {
    name: '',
    email: '',
    address: ''
  };

  ngOnInit(): void {
    this.loading.set(true);
    this.authService.resolveUserId().subscribe({
      next: (id) => {
        if (!id) {
          this.loading.set(false);
          this.error.set('Unable to resolve user profile from token. Backend must provide a user-id claim or a lookup endpoint like /api/ecom/customer/me.');
          return;
        }

        this.customerId = id;
        this.fetchProfile();
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Unable to resolve user profile context.');
      }
    });
  }

  toggleEdit(): void {
    this.editMode.set(!this.editMode());
    this.error.set('');
    this.success.set('');
  }

  save(): void {
    if (!this.model.name?.trim() || !this.model.email?.trim() || !this.model.address?.trim()) {
      this.error.set('Name, email, and address are required.');
      return;
    }

    this.loading.set(true);
    this.customerService.updateCustomer(this.customerId, this.model).subscribe({
      next: (data) => {
        this.loading.set(false);
        this.profile.set(data);
        this.model = { name: data.name, email: data.email, address: data.address };
        this.editMode.set(false);
        this.success.set('Profile updated successfully.');
        this.error.set('');
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);
        this.error.set(this.getErrorMessage(err, 'Unable to update profile right now.'));
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  private fetchProfile(): void {
    this.loading.set(true);
    this.customerService.getCustomerById(this.customerId).subscribe({
      next: (data) => {
        this.loading.set(false);
        this.profile.set(data);
        this.model = { name: data.name, email: data.email, address: data.address };
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);
        this.error.set(this.getErrorMessage(err, 'Unable to load profile.'));
      }
    });
  }

  private getErrorMessage(err: HttpErrorResponse, fallback: string): string {
    if (err.status === 400) {
      return 'Invalid profile data. Please verify your inputs.';
    }
    if (err.status === 401) {
      return 'Unauthorized. Please login again.';
    }
    if (err.status === 403) {
      return 'Access denied for profile.';
    }
    if (err.status === 404) {
      return 'Profile not found.';
    }
    if (err.status >= 500) {
      return 'Server error while processing profile.';
    }
    return fallback;
  }
}
