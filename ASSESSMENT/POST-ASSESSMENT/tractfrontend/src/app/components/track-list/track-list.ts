import { Component, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { TrackService } from '../../core/services/track.service';
import { Track } from '../../models/track';
import { PageHeader } from '../shared/page-header/page-header';
import { Loading } from '../shared/loading/loading';
import { EmptyState } from '../shared/empty-state/empty-state';

@Component({
  selector: 'app-track-list',
  imports: [RouterLink, DatePipe, PageHeader, Loading, EmptyState],
  templateUrl: './track-list.html'
})
export class TrackList implements OnInit {
  private readonly trackService = inject(TrackService);

  readonly tracks = signal<Track[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');
  readonly success = signal('');
  readonly deletingId = signal<number | null>(null);

  ngOnInit(): void {
    this.loadTracks();
  }

  loadTracks(): void {
    this.loading.set(true);
    this.error.set('');
    this.trackService.getAllTracks().subscribe({
      next: (data) => {
        this.tracks.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.message);
        this.loading.set(false);
      }
    });
  }

  deleteTrack(track: Track): void {
    if (track.id == null) {
      return;
    }
    const confirmed = confirm(`Are you sure you want to delete "${track.title}"?`);
    if (!confirmed) {
      return;
    }

    this.deletingId.set(track.id);
    this.error.set('');
    this.success.set('');
    this.trackService.deleteTrack(track.id).subscribe({
      next: () => {
        this.success.set(`"${track.title}" deleted successfully.`);
        this.deletingId.set(null);
        this.loadTracks();
      },
      error: (err) => {
        this.error.set(err.message);
        this.deletingId.set(null);
      }
    });
  }
}
