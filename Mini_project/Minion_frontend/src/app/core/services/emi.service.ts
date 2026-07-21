import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../api.config';
import { EmiPayment, EmiPaymentRequest, EmiSchedule } from '../models';

@Injectable({ providedIn: 'root' })
export class EmiService {
  private readonly base = `${API_BASE_URL}/api/emis`;

  constructor(private http: HttpClient) {}

  payEmi(emiId: number, request: EmiPaymentRequest): Observable<EmiSchedule> {
    return this.http.post<EmiSchedule>(`${this.base}/${emiId}/payments`, request);
  }

  recalculatePenalty(emiId: number): Observable<EmiSchedule> {
    return this.http.patch<EmiSchedule>(`${this.base}/${emiId}/penalty/recalculate`, null);
  }

  highestOverdue(): Observable<EmiSchedule> {
    return this.http.get<EmiSchedule>(`${this.base}/reports/highest-overdue`);
  }

  latestPayments(): Observable<EmiPayment[]> {
    return this.http.get<EmiPayment[]>(`${this.base}/payments/reports/latest`);
  }
}
