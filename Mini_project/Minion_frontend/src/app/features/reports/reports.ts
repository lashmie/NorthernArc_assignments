import { Component, inject, signal } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { LoanService } from '../../core/services/loan.service';
import { EmiService } from '../../core/services/emi.service';
import { EmiPayment, EmiSchedule, LoanSummaryDTO } from '../../core/models';

@Component({
  selector: 'app-reports',
  imports: [CurrencyPipe, DatePipe, ReactiveFormsModule],
  templateUrl: './reports.html',
  styleUrl: './reports.css'
})
export class ReportsComponent {
  private fb = inject(FormBuilder);
  private loanService = inject(LoanService);
  private emiService = inject(EmiService);

  error = signal<string | null>(null);
  message = signal<string | null>(null);

  collection = signal<Array<[string, number]>>([]);
  zeroOverdue = signal<LoanSummaryDTO[]>([]);
  highestOverdue = signal<EmiSchedule | null>(null);
  latestPayments = signal<EmiPayment[]>([]);

  loadingCollection = signal(true);
  loadingZero = signal(true);
  loadingEmi = signal(true);
  savingRate = signal(false);

  loanTypes = ['PERSONAL_LOAN', 'HOME_LOAN', 'CAR_LOAN', 'EDUCATION_LOAN', 'BUSINESS_LOAN'];

  rateForm = this.fb.nonNullable.group({
    loanType: ['PERSONAL_LOAN', [Validators.required]],
    rate: [8.5, [Validators.required, Validators.min(0.01)]]
  });

  constructor() {
    this.loadReports();
  }

  private loadReports(): void {
    this.loanService.collectionByCity().subscribe({
      next: (rows) => { this.collection.set(rows ?? []); this.loadingCollection.set(false); },
      error: () => { this.loadingCollection.set(false); }
    });

    this.loanService.loansWithZeroOverdue().subscribe({
      next: (rows) => { this.zeroOverdue.set(rows ?? []); this.loadingZero.set(false); },
      error: () => { this.loadingZero.set(false); }
    });

    this.emiService.highestOverdue().subscribe({
      next: (emi) => this.highestOverdue.set(emi),
      error: () => this.highestOverdue.set(null)
    });

    this.emiService.latestPayments().subscribe({
      next: (rows) => { this.latestPayments.set(rows ?? []); this.loadingEmi.set(false); },
      error: () => { this.loadingEmi.set(false); }
    });
  }

  reviseRate(): void {
    if (this.rateForm.invalid) {
      this.rateForm.markAllAsTouched();
      return;
    }
    const { loanType, rate } = this.rateForm.getRawValue();
    this.savingRate.set(true);
    this.error.set(null);
    this.message.set(null);
    this.loanService.reviseInterestRateByType(loanType, rate).subscribe({
      next: (count) => {
        this.savingRate.set(false);
        this.message.set(`Interest rate updated for ${count} ${loanType} loan(s).`);
      },
      error: (err) => {
        this.savingRate.set(false);
        this.error.set(err?.error?.message ?? 'Failed to update interest rate.');
      }
    });
  }
}
