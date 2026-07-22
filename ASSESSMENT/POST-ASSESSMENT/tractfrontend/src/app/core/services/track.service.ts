import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Track } from '../../models/track';
import { TrackRequest } from '../../models/track-request';

@Injectable({ providedIn: 'root' })
export class TrackService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8080/music/platform/v1';

  getAllTracks(): Observable<Track[]> {
    return this.http.get<Track[]>(`${this.baseUrl}/tracks`);
  }

  createTrack(trackRequest: TrackRequest): Observable<Track> {
    return this.http.post<Track>(`${this.baseUrl}/tracks`, trackRequest);
  }

  deleteTrack(trackId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/tracks/${trackId}`);
  }

  searchTrack(title: string, albumName?: string): Observable<Track> {
    let params = new HttpParams().set('title', title);
    if (albumName && albumName.trim()) {
      params = params.set('albumName', albumName.trim());
    }
    return this.http.get<Track>(`${this.baseUrl}/tracks/search`, { params });
  }
}
