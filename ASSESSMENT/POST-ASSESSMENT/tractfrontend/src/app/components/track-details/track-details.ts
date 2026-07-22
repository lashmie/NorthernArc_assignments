import { Component, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { TrackService } from '../../core/services/track.service';
import { Track } from '../../models/track';
import { PageHeader } from '../shared/page-header/page-header';
import { Loading } from '../shared/loading/loading';
import { EmptyState } from '../shared/empty-state/empty-state';

@Component({
  selector: 'app-track-details',
  imports: [RouterLink, DatePipe, PageHeader, Loading, EmptyState],
  templateUrl: './track-details.html'
})
export class TrackDetails implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly trackService = inject(TrackService);

  readonly track = signal<Track | null>(null);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    const title = this.route.snapshot.paramMap.get('title') ?? '';
    this.loadTrack(title);
  }

  loadTrack(title: string): void {
    this.loading.set(true);
    this.error.set('');
    this.trackService.searchTrack(title).subscribe({
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
