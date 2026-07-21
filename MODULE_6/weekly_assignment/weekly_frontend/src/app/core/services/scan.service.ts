import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Scan, ScanOrderBy } from '../../models/scan';

@Injectable({ providedIn: 'root' })
export class ScanService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8080/scan';

  /** GET /scan → all scans (backend already excludes soft-deleted) */
  getAllScans(): Observable<Scan[]> {
    return this.http.get<Scan[]>(this.baseUrl);
  }

  /** GET /scan/{id} */
  getScanById(id: number): Observable<Scan> {
    return this.http.get<Scan>(`${this.baseUrl}/${id}`);
  }

  /** POST /scan */
  createScan(scan: Scan): Observable<Scan> {
    return this.http.post<Scan>(this.baseUrl, scan);
  }

  /** DELETE /scan/{id} → soft delete */
  deleteScan(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  /** GET /scan/search/{domainName}?orderBy={column} */
  searchScans(domainName: string, orderBy: ScanOrderBy): Observable<Scan[]> {
    const params = new HttpParams().set('orderBy', orderBy);
    return this.http.get<Scan[]>(
      `${this.baseUrl}/search/${encodeURIComponent(domainName)}`,
      { params }
    );
  }
}
