import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../api.config';
import { Customer } from '../models';

@Injectable({ providedIn: 'root' })
export class CustomerService {
  private readonly base = `${API_BASE_URL}/api/customers`;

  constructor(private http: HttpClient) {}

  createCustomer(customer: Customer): Observable<Customer> {
    return this.http.post<Customer>(this.base, customer);
  }

  overdueCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.base}/reports/overdue`);
  }

  topDefaulters(): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.base}/reports/top-defaulters`);
  }
}
