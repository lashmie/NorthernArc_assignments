import { Component, inject, signal } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { EmiService } from '../../core/services/emi.service';
import { EmiSchedule } from '../../core/models';

@Component({
  selector: 'app-emi-pay',
  imports: [ReactiveFormsModule, CurrencyPipe],
  templateUrl: './emi-pay.html',
  styleUrl: './emi-pay.css'
})
export class EmiPayComponent {
  private fb = inject(FormBuilder);
  private emiService = inject(EmiService);

  loading = signal(false);
  error = signal<string | null>(null);
  result = signal<EmiSchedule | null>(null);

  paymentModes = ['BANK_TRANSFER', 'CASH', 'CHEQUE', 'UPI', 'CARD'];

  form = this.fb.nonNullable.group({
    emiId: [null as number | null, [Validators.required, Validators.min(1)]],
    amount: [null as number | null, [Validators.required, Validators.min(0.01)]],
    paymentMode: ['BANK_TRANSFER', [Validators.required]],
    referenceNumber: ['']
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const { emiId, amount, paymentMode, referenceNumber } = this.form.getRawValue();
    this.loading.set(true);
    this.error.set(null);
    this.result.set(null);
    this.emiService.payEmi(emiId!, {
      amount: amount!,
      paymentMode,
      referenceNumber: referenceNumber || undefined
    }).subscribe({
      next: (res) => {
        this.result.set(res);
        this.loading.set(false);
        this.form.patchValue({ amount: null, referenceNumber: '' });
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(err?.error?.message ?? 'Payment failed.');
      }
    });
  }

  badgeClass(status: string | undefined): string {
    return 'badge badge-' + (status || '').toLowerCase();
  }
}
