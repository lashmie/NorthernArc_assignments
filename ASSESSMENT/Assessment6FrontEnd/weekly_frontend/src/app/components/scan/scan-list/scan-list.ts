import { Component, computed, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ScanService } from '../../../core/services/scan.service';
import { Scan } from '../../../models/scan';
import { Loading } from '../../shared/loading/loading';
import { EmptyState } from '../../shared/empty-state/empty-state';

@Component({
  selector: 'app-scan-list',
  imports: [RouterLink, Loading, EmptyState],
  templateUrl: './scan-list.html',
  styleUrl: './scan-list.css'
})
export class ScanList {
  private readonly scanService = inject(ScanService);

  protected readonly scans = signal<Scan[]>([]);
  protected readonly loading = signal(false);
  protected readonly error = signal<string | null>(null);
  protected readonly success = signal<string | null>(null);
  protected readonly deletingId = signal<number | null>(null);

  protected readonly hasScans = computed(() => this.scans().length > 0);

  constructor() {
    this.loadScans();
  }

  loadScans(): void {
    this.loading.set(true);
    this.error.set(null);
    this.scanService.getAllScans().subscribe({
      next: (data) => {
        this.scans.set(data ?? []);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err?.userMessage ?? 'Failed to load scans.');
        this.loading.set(false);
      }
    });
  }

  deleteScan(scan: Scan): void {
    if (scan.id == null) {
      return;
    }
    const confirmed = confirm(
      `Are you sure you want to delete the scan for "${scan.domainName}"? This action cannot be undone.`
    );
    if (!confirmed) {
      return;
    }

    this.deletingId.set(scan.id);
    this.error.set(null);
    this.success.set(null);
    this.scanService.deleteScan(scan.id).subscribe({
      next: () => {
        this.deletingId.set(null);
        this.success.set(`Scan for "${scan.domainName}" was deleted successfully.`);
        this.loadScans();
      },
      error: (err) => {
        this.deletingId.set(null);
        this.error.set(err?.userMessage ?? 'Failed to delete the scan.');
      }
    });
  }
}
