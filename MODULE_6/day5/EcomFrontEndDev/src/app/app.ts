import { Component, inject } from '@angular/core';
import { NgIf } from '@angular/common';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { CartService } from './services/cart.service';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  imports: [NgIf, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private readonly cartService = inject(CartService);
  private readonly authService = inject(AuthService);

  readonly cartCount = this.cartService.itemCount;

  checkToken(): string | null {
    return this.authService.getToken();
  }

  role(): string {
    return this.authService.getRole();
  }
}
