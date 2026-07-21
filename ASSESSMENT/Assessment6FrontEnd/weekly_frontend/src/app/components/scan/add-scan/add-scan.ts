import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { ScanService } from '../../../core/services/scan.service';
import { Scan } from '../../../models/scan';

@Component({
  selector: 'app-add-scan',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './add-scan.html',
  styleUrl: './add-scan.css'
})
export class AddScan {
  private readonly fb = inject(FormBuilder);
  private readonly scanService = inject(ScanService);
  private readonly router = inject(Router);

  protected readonly submitting = signal(false);
  protected readonly error = signal<string | null>(null);
  protected readonly submitted = signal(false);

  protected readonly form = this.fb.nonNullable.group({
    domainName: ['', [Validators.required, Validators.minLength(3)]],
    numPages: [0, [Validators.required, Validators.min(0)]],
    numBrokenLinks: [0, [Validators.required, Validators.min(0)]],
    numMissingImages: [0, [Validators.required, Validators.min(0)]]
  });

  isInvalid(controlName: string): boolean {
    const control = this.form.get(controlName);
    return !!control && control.invalid && (control.touched || this.submitted());
  }

  submit(): void {
    this.submitted.set(true);
    this.error.set(null);

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const scan: Scan = this.form.getRawValue();
    this.submitting.set(true);
    this.scanService.createScan(scan).subscribe({
      next: () => {
        this.submitting.set(false);
        this.router.navigate(['/scans']);
      },
      error: (err) => {
        this.submitting.set(false);
        this.error.set(err?.userMessage ?? 'Failed to create the scan.');
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/scans']);
  }
}
