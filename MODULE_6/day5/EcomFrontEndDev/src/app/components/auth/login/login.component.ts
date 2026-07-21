import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { jwtRequestDTO } from '../../../dto/jwt-request';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly loading = signal(false);
  readonly error = signal('');
  credentials: jwtRequestDTO = { username: '', password: '' };
  formSubmitted = false;
  hidePassword = true;

  ngOnInit(): void {
    if (localStorage.getItem('jwt_token') !== null) {
      this.router.navigate(['/products']);
    }
  }

  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }

  onSubmit(isValid: boolean | null): void {
    this.formSubmitted = true;
    if (!isValid) {
      return;
    }

    const payload = this.credentials;
    if (!payload.username || !payload.password) {
      this.error.set('Username and password are required.');
      return;
    }

    this.loading.set(true);
    this.error.set('');

    this.authService.login(payload).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/products']);
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(err?.error?.message ?? 'Invalid credentials or server unavailable.');
      }
    });
  }
}
