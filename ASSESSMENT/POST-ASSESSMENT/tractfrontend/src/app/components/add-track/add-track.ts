import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TrackService } from '../../core/services/track.service';
import { TrackRequest } from '../../models/track-request';
import { PageHeader } from '../shared/page-header/page-header';

@Component({
  selector: 'app-add-track',
  imports: [FormsModule, RouterLink, PageHeader],
  templateUrl: './add-track.html'
})
export class AddTrack {
  private readonly trackService = inject(TrackService);
  private readonly router = inject(Router);

  readonly title = signal('');
  readonly albumName = signal('');
  readonly releaseDate = signal('');
  readonly playCount = signal<number | null>(null);

  readonly loading = signal(false);
  readonly error = signal('');
  readonly success = signal('');
  readonly submitted = signal(false);

  isValid(): boolean {
    return (
      this.title().trim().length > 0 &&
      this.albumName().trim().length > 0 &&
      this.releaseDate().trim().length > 0 &&
      this.playCount() !== null &&
      (this.playCount() as number) >= 0
    );
  }

  onSubmit(): void {
    this.submitted.set(true);
    this.error.set('');
    this.success.set('');

    if (!this.isValid()) {
      return;
    }

    const request: TrackRequest = {
      title: this.title().trim(),
      albumName: this.albumName().trim(),
      // Convert yyyy-MM-dd from the date input into an ISO date-time string
      releaseDate: new Date(this.releaseDate()).toISOString(),
      playCount: this.playCount() as number
    };

    this.loading.set(true);
    this.trackService.createTrack(request).subscribe({
      next: () => {
        this.success.set('Track created successfully!');
        this.loading.set(false);
        setTimeout(() => this.router.navigate(['/tracks']), 1000);
      },
      error: (err) => {
        this.error.set(err.message);
        this.loading.set(false);
      }
    });
  }
}
