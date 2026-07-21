import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ScanService } from '../../../core/services/scan.service';
import { Scan, ScanOrderBy, SCAN_ORDER_BY_OPTIONS } from '../../../models/scan';
import { Loading } from '../../shared/loading/loading';
import { EmptyState } from '../../shared/empty-state/empty-state';

@Component({
  selector: 'app-search-scan',
  imports: [RouterLink, FormsModule, Loading, EmptyState],
  templateUrl: './search-scan.html',
  styleUrl: './search-scan.css'
})
export class SearchScan {
  private readonly scanService = inject(ScanService);
  private readonly router = inject(Router);

  protected readonly orderByOptions = SCAN_ORDER_BY_OPTIONS;

  protected domainName = '';
  protected orderBy: ScanOrderBy = 'domainName';

  protected readonly results = signal<Scan[]>([]);
  protected readonly loading = signal(false);
  protected readonly error = signal<string | null>(null);
  protected readonly searched = signal(false);

  // --- Find / Delete by ID ---
  protected scanId: number | null = null;
  protected readonly idScan = signal<Scan | null>(null);
  protected readonly idLoading = signal(false);
  protected readonly idDeleting = signal(false);
  protected readonly idError = signal<string | null>(null);
  protected readonly idSuccess = signal<string | null>(null);
  protected readonly idSearched = signal(false);

  search(): void {
    const term = this.domainName.trim();
    if (!term) {
      this.error.set('Please enter a domain name to search.');
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.searched.set(true);
    this.scanService.searchScans(term, this.orderBy).subscribe({
      next: (data) => {
        this.results.set(data ?? []);
        this.loading.set(false);
      },
      error: (err) => {
        this.results.set([]);
        this.error.set(err?.userMessage ?? 'Search failed. Please try again.');
        this.loading.set(false);
      }
    });
  }

  reset(): void {
    this.domainName = '';
    this.orderBy = 'domainName';
    this.results.set([]);
    this.searched.set(false);
    this.error.set(null);
  }

  /** Look up a single scan by its ID. */
  findById(): void {
    const id = this.scanId;
    if (id == null || Number.isNaN(id) || id <= 0) {
      this.idError.set('Please enter a valid scan ID.');
      this.idScan.set(null);
      this.idSearched.set(true);
      return;
    }

    this.idLoading.set(true);
    this.idError.set(null);
    this.idSuccess.set(null);
    this.idScan.set(null);
    this.idSearched.set(true);
    this.scanService.getScanById(id).subscribe({
      next: (data) => {
        this.idScan.set(data);
        this.idLoading.set(false);
      },
      error: (err) => {
        this.idError.set(err?.userMessage ?? `No scan found with ID ${id}.`);
        this.idLoading.set(false);
      }
    });
  }

  /** View the full details page for the found scan. */
  viewById(): void {
    const scan = this.idScan();
    if (scan?.id != null) {
      this.router.navigate(['/scans', scan.id]);
    }
  }

  /** Delete a scan by ID (with confirmation). */
  deleteById(): void {
    const scan = this.idScan();
    if (!scan || scan.id == null) {
      return;
    }
    const confirmed = confirm(
      `Are you sure you want to delete scan #${scan.id} ("${scan.domainName}")? This cannot be undone.`
    );
    if (!confirmed) {
      return;
    }

    this.idDeleting.set(true);
    this.idError.set(null);
    this.idSuccess.set(null);
    this.scanService.deleteScan(scan.id).subscribe({
      next: () => {
        this.idDeleting.set(false);
        this.idSuccess.set(`Scan #${scan.id} ("${scan.domainName}") was deleted successfully.`);
        this.idScan.set(null);
      },
      error: (err) => {
        this.idDeleting.set(false);
        this.idError.set(err?.userMessage ?? 'Failed to delete the scan.');
      }
    });
  }

  resetById(): void {
    this.scanId = null;
    this.idScan.set(null);
    this.idSearched.set(false);
    this.idError.set(null);
    this.idSuccess.set(null);
  }
}
