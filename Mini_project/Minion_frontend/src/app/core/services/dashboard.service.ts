import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../api.config';
import { DashboardDTO, LoanDashboardDTO } from '../models';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly base = API_BASE_URL;

  constructor(private http: HttpClient) {}

  /** ADMIN / MANAGER comprehensive dashboard. */
  getDashboard(): Observable<DashboardDTO> {
    return this.http.get<DashboardDTO>(`${this.base}/api/dashboard`);
  }

  /** ADMIN-only dashboard. */
  getAdminDashboard(): Observable<DashboardDTO> {
    return this.http.get<DashboardDTO>(`${this.base}/api/dashboard/admin`);
  }

  /** Public loan dashboard with collection-by-city. */
  getLoanDashboard(): Observable<LoanDashboardDTO> {
    return this.http.get<LoanDashboardDTO>(`${this.base}/api/loans/dashboard`);
  }
}
