import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { TrackService } from '../../core/services/track.service';
import { Track } from '../../models/track';
import { PageHeader } from '../shared/page-header/page-header';
import { Loading } from '../shared/loading/loading';
import { EmptyState } from '../shared/empty-state/empty-state';

@Component({
  selector: 'app-search-track',
  imports: [FormsModule, RouterLink, DatePipe, PageHeader, Loading, EmptyState],
  templateUrl: './search-track.html'
})
export class SearchTrack {
  private readonly trackService = inject(TrackService);

  readonly title = signal('');
  readonly albumName = signal('');
  readonly track = signal<Track | null>(null);
  readonly loading = signal(false);
  readonly error = signal('');
  readonly searched = signal(false);

  onSearch(): void {
    if (!this.title().trim()) {
      return;
    }
    this.loading.set(true);
    this.error.set('');
    this.searched.set(true);
    this.track.set(null);

    this.trackService.searchTrack(this.title().trim(), this.albumName().trim()).subscribe({
      next: (data) => {
        this.track.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.message);
        this.loading.set(false);
      }
    });
  }
}
