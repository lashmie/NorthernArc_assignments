import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-not-found',
  imports: [RouterLink],
  template: `
    <div class="text-center py-5">
      <h1 class="display-1 fw-bold text-primary">404</h1>
      <h3>Page Not Found</h3>
      <p class="text-muted">The page you are looking for does not exist.</p>
      <a routerLink="/" class="btn btn-primary mt-2">
        <i class="bi bi-house-door me-1"></i>Go Home
      </a>
    </div>
  `
})
export class NotFound {}
