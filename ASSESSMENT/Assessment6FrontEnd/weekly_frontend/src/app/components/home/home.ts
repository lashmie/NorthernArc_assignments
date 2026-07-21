import { Component, computed, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ScanService } from '../../core/services/scan.service';
import { HealthService } from '../../core/services/health.service';
import { Scan } from '../../models/scan';
import { Loading } from '../shared/loading/loading';
import { EmptyState } from '../shared/empty-state/empty-state';

@Component({
  selector: 'app-home',
  imports: [RouterLink, Loading, EmptyState],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {
  private readonly scanService = inject(ScanService);
  private readonly healthService = inject(HealthService);

  protected readonly scans = signal<Scan[]>([]);
  protected readonly loading = signal(false);
  protected readonly error = signal<string | null>(null);

  // Backend status state
  protected readonly statusLoading = signal(false);
  protected readonly healthOnline = signal<boolean | null>(null);
  protected readonly ready = signal<boolean | null>(null);
  protected readonly statusError = signal<string | null>(null);

  protected readonly totalScans = computed(() => this.scans().length);

  protected readonly totalPages = computed(() =>
    this.scans().reduce((sum, s) => sum + (s.numPages ?? 0), 0)
  );

  protected readonly totalBrokenLinks = computed(() =>
    this.scans().reduce((sum, s) => sum + (s.numBrokenLinks ?? 0), 0)
  );

  protected readonly totalMissingImages = computed(() =>
    this.scans().reduce((sum, s) => sum + (s.numMissingImages ?? 0), 0)
  );

  protected readonly recentScans = computed(() =>
    [...this.scans()].sort((a, b) => (b.id ?? 0) - (a.id ?? 0)).slice(0, 5)
  );

  constructor() {
    this.checkBackendStatus();
    this.loadScans();
  }

  checkBackendStatus(): void {
    this.statusLoading.set(true);
    this.statusError.set(null);
    this.healthOnline.set(null);
    this.ready.set(null);

    this.healthService.checkHealth().subscribe({
      next: () => {
        this.healthOnline.set(true);
        this.checkReadyStatus();
      },
      error: (err) => {
        this.healthOnline.set(false);
        this.ready.set(false);
        this.statusError.set(
          err?.userMessage ?? 'The backend service is currently unavailable.'
        );
        this.statusLoading.set(false);
      }
    });
  }

  private checkReadyStatus(): void {
    this.healthService.checkReady().subscribe({
      next: () => {
        this.ready.set(true);
        this.statusLoading.set(false);
      },
      error: (err) => {
        this.ready.set(false);
        this.statusError.set(
          err?.userMessage ?? 'The backend service is currently unavailable.'
        );
        this.statusLoading.set(false);
      }
    });
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
}
