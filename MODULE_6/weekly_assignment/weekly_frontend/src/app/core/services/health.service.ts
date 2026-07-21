import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class HealthService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8080';

  /** GET /health → 200 OK when the backend is healthy. */
  checkHealth(): Observable<void> {
    return this.http.get<void>(`${this.baseUrl}/health`);
  }

  /** GET /ready → 200 OK when the backend is ready to serve requests. */
  checkReady(): Observable<void> {
    return this.http.get<void>(`${this.baseUrl}/ready`);
  }
}
