import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../api.config';
import { Loan, LoanCreateRequest, LoanSummaryDTO, Page } from '../models';

@Injectable({ providedIn: 'root' })
export class LoanService {
  private readonly base = `${API_BASE_URL}/api/loans`;

  constructor(private http: HttpClient) {}

  createLoan(request: LoanCreateRequest): Observable<Loan> {
    return this.http.post<Loan>(this.base, request);
  }

  getLoans(page = 0, size = 10): Observable<Page<LoanSummaryDTO>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<LoanSummaryDTO>>(this.base, { params });
  }

  reviseInterestRateByType(loanType: string, rate: number): Observable<number> {
    const params = new HttpParams().set('loanType', loanType).set('rate', rate);
    return this.http.patch<number>(`${this.base}/interest-rate`, null, { params });
  }

  reviseLoanInterest(loanId: number, rate: number): Observable<Loan> {
    const params = new HttpParams().set('rate', rate);
    return this.http.put<Loan>(`${this.base}/${loanId}/interest`, null, { params });
  }

  deleteLoan(loanId: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${loanId}`);
  }

  approveLoan(loanId: number): Observable<Loan> {
    return this.http.put<Loan>(`${this.base}/${loanId}/approve`, null);
  }

  rejectLoan(loanId: number, reason: string): Observable<Loan> {
    return this.http.put<Loan>(`${this.base}/${loanId}/reject`, { reason });
  }

  collectionByCity(): Observable<Array<[string, number]>> {
    return this.http.get<Array<[string, number]>>(`${this.base}/reports/collection-by-city`);
  }

  loansWithZeroOverdue(): Observable<LoanSummaryDTO[]> {
    return this.http.get<LoanSummaryDTO[]>(`${this.base}/reports/zero-overdue`);
  }
}
