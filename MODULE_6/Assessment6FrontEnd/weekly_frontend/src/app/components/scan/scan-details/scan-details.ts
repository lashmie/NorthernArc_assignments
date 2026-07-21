import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ScanService } from '../../../core/services/scan.service';
import { Scan } from '../../../models/scan';
import { Loading } from '../../shared/loading/loading';

@Component({
  selector: 'app-scan-details',
  imports: [RouterLink, Loading],
  templateUrl: './scan-details.html',
  styleUrl: './scan-details.css'
})
export class ScanDetails {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly scanService = inject(ScanService);

  protected readonly scan = signal<Scan | null>(null);
  protected readonly loading = signal(false);
  protected readonly error = signal<string | null>(null);
  protected readonly deleting = signal(false);

  constructor() {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = Number(idParam);
    if (!idParam || Number.isNaN(id)) {
      this.error.set('Invalid scan id.');
      return;
    }
    this.loadScan(id);
  }

  loadScan(id: number): void {
    this.loading.set(true);
    this.error.set(null);
    this.scanService.getScanById(id).subscribe({
      next: (data) => {
        this.scan.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err?.userMessage ?? 'Failed to load the scan.');
        this.loading.set(false);
      }
    });
  }

  deleteScan(): void {
    const current = this.scan();
    if (!current || current.id == null) {
      return;
    }
    const confirmed = confirm(
      `Are you sure you want to delete the scan for "${current.domainName}"?`
    );
    if (!confirmed) {
      return;
    }
    this.deleting.set(true);
    this.error.set(null);
    this.scanService.deleteScan(current.id).subscribe({
      next: () => {
        this.deleting.set(false);
        this.router.navigate(['/scans']);
      },
      error: (err) => {
        this.deleting.set(false);
        this.error.set(err?.userMessage ?? 'Failed to delete the scan.');
      }
    });
  }
}
