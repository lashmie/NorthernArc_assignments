import { Component, inject, signal } from '@angular/core';
import { CurrencyPipe, DatePipe, DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { LoanService } from '../../core/services/loan.service';
import { AuthService } from '../../core/services/auth.service';
import { LoanSummaryDTO } from '../../core/models';

@Component({
  selector: 'app-loans',
  imports: [CurrencyPipe, DatePipe, DecimalPipe, RouterLink],
  templateUrl: './loans.html',
  styleUrl: './loans.css'
})
export class LoansComponent {
  private loanService = inject(LoanService);
  private auth = inject(AuthService);

  loading = signal(true);
  error = signal<string | null>(null);
  message = signal<string | null>(null);

  loans = signal<LoanSummaryDTO[]>([]);
  page = signal(0);
  size = signal(10);
  totalPages = signal(0);
  totalElements = signal(0);

  canManage = this.auth.hasRole('ADMIN', 'MANAGER');
  canDelete = this.auth.hasRole('ADMIN');
  canApprove = this.auth.hasRole('ADMIN', 'MANAGER');

  isPending(status: string): boolean {
    return (status || '').toUpperCase() === 'PENDING';
  }

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.loanService.getLoans(this.page(), this.size()).subscribe({
      next: (res) => {
        this.loans.set(res.content ?? []);
        this.totalPages.set(res.totalPages ?? 0);
        this.totalElements.set(res.totalElements ?? 0);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err?.error?.message ?? 'Unable to load loans.');
        this.loading.set(false);
      }
    });
  }

  prev(): void {
    if (this.page() > 0) {
      this.page.update((p) => p - 1);
      this.load();
    }
  }

  next(): void {
    if (this.page() < this.totalPages() - 1) {
      this.page.update((p) => p + 1);
      this.load();
    }
  }

  reviseInterest(loan: LoanSummaryDTO): void {
    const input = prompt(`New annual interest rate for loan #${loan.loanId} (current ${loan.annualInterestRate}%):`);
    if (input === null) return;
    const rate = Number(input);
    if (Number.isNaN(rate) || rate <= 0) {
      this.error.set('Please enter a valid positive interest rate.');
      return;
    }
    this.loanService.reviseLoanInterest(loan.loanId, rate).subscribe({
      next: () => {
        this.message.set(`Interest updated for loan #${loan.loanId}.`);
        this.load();
      },
      error: (err) => this.error.set(err?.error?.message ?? 'Failed to update interest.')
    });
  }

  deleteLoan(loan: LoanSummaryDTO): void {
    if (!confirm(`Delete loan #${loan.loanId}? This cannot be undone.`)) return;
    this.loanService.deleteLoan(loan.loanId).subscribe({
      next: () => {
        this.message.set(`Loan #${loan.loanId} deleted.`);
        this.load();
      },
      error: (err) => this.error.set(err?.error?.message ?? 'Failed to delete loan.')
    });
  }

  approveLoan(loan: LoanSummaryDTO): void {
    if (!confirm(`Approve loan #${loan.loanId}?`)) return;
    this.error.set(null);
    this.message.set(null);
    this.loanService.approveLoan(loan.loanId).subscribe({
      next: () => {
        this.message.set(`Loan #${loan.loanId} approved.`);
        this.load();
      },
      error: (err) => this.error.set(err?.error?.message ?? 'Failed to approve loan.')
    });
  }

  rejectLoan(loan: LoanSummaryDTO): void {
    const reason = prompt(`Reason for rejecting loan #${loan.loanId}:`);
    if (reason === null) return;
    if (!reason.trim()) {
      this.error.set('A rejection reason is required.');
      return;
    }
    this.error.set(null);
    this.message.set(null);
    this.loanService.rejectLoan(loan.loanId, reason.trim()).subscribe({
      next: () => {
        this.message.set(`Loan #${loan.loanId} rejected.`);
        this.load();
      },
      error: (err) => this.error.set(err?.error?.message ?? 'Failed to reject loan.')
    });
  }

  badgeClass(status: string): string {
    return 'badge badge-' + (status || '').toLowerCase();
  }
}
