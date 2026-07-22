import { Component, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { TrackService } from '../../core/services/track.service';
import { Track } from '../../models/track';
import { PageHeader } from '../shared/page-header/page-header';
import { Loading } from '../shared/loading/loading';

@Component({
  selector: 'app-home',
  imports: [RouterLink, DatePipe, PageHeader, Loading],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  private readonly trackService = inject(TrackService);

  readonly tracks = signal<Track[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

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

  recentTracks(): Track[] {
    return this.tracks().slice(0, 5);
  }
}
