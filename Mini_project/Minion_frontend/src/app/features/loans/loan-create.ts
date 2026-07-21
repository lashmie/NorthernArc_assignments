import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { LoanService } from '../../core/services/loan.service';

@Component({
  selector: 'app-loan-create',
  imports: [ReactiveFormsModule, RouterLink, DecimalPipe],
  templateUrl: './loan-create.html',
  styleUrl: './loan-create.css'
})
export class LoanCreateComponent {
  private fb = inject(FormBuilder);
  private loanService = inject(LoanService);
  private router = inject(Router);

  loading = signal(false);
  error = signal<string | null>(null);

  loanTypes = ['PERSONAL_LOAN', 'HOME_LOAN', 'CAR_LOAN', 'EDUCATION_LOAN', 'BUSINESS_LOAN'];

  form = this.fb.nonNullable.group({
    loanType: ['PERSONAL_LOAN', [Validators.required]],
    principalAmount: [100000, [Validators.required, Validators.min(1)]],
    annualInterestRate: [8.5, [Validators.required, Validators.min(0.01)]],
    tenureMonths: [24, [Validators.required, Validators.min(1)]],
    customerId: [null as number | null, [Validators.required, Validators.min(1)]]
  });

  // Live EMI preview using the same formula as the backend.
  previewEmi(): number {
    const { principalAmount, annualInterestRate, tenureMonths } = this.form.getRawValue();
    if (!principalAmount || !annualInterestRate || !tenureMonths) return 0;
    const monthlyRate = annualInterestRate / (12 * 100);
    const factor = Math.pow(1 + monthlyRate, tenureMonths);
    const emi = (principalAmount * monthlyRate * factor) / (factor - 1);
    return Number.isFinite(emi) ? emi : 0;
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading.set(true);
    this.error.set(null);
    this.loanService.createLoan(this.form.getRawValue()).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/loans']);
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(err?.error?.message ?? 'Failed to create loan.');
      }
    });
  }
}
