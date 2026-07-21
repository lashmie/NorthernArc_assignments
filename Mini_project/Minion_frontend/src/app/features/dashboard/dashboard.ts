import { Component, inject, signal } from '@angular/core';
import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { DashboardService } from '../../core/services/dashboard.service';
import { AuthService } from '../../core/services/auth.service';
import { DashboardDTO, LoanDashboardDTO } from '../../core/models';

@Component({
  selector: 'app-dashboard',
  imports: [CurrencyPipe, DecimalPipe, RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent {
  private dashboardService = inject(DashboardService);
  private auth = inject(AuthService);

  role = this.auth.role;
  loading = signal(true);
  error = signal<string | null>(null);

  data = signal<DashboardDTO | null>(null);
  loanData = signal<LoanDashboardDTO | null>(null);

  constructor() {
    this.load();
  }

  private load(): void {
    this.loading.set(true);
    // Public loan dashboard is available to everyone.
    this.dashboardService.getLoanDashboard().subscribe({
      next: (d) => this.loanData.set(d),
      error: () => this.loanData.set(null)
    });

    if (this.auth.hasRole('ADMIN', 'MANAGER')) {
      this.dashboardService.getDashboard().subscribe({
        next: (d) => {
          this.data.set(d);
          this.loading.set(false);
        },
        error: (err) => {
          this.error.set(err?.error?.message ?? 'Unable to load dashboard.');
          this.loading.set(false);
        }
      });
    } else {
      this.loading.set(false);
    }
  }

  cityEntries(map: { [city: string]: number } | undefined): Array<[string, number]> {
    return map ? Object.entries(map) : [];
  }
}
