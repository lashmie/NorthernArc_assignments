import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
	CustomerRequestDTO,
	CustomerResponseDTO,
	CustomerUpdateDTO
} from '../dto/customer';
import { OrderResponseDTO } from '../dto/order';

@Injectable({
	providedIn: 'root'
})
export class CustomerService {
	private readonly baseUrl = 'http://localhost:8080/api/ecom/customer';

	constructor(private readonly http: HttpClient) {}

	getAllCustomers(): Observable<CustomerResponseDTO[]> {
		return this.http.get<CustomerResponseDTO[]>(this.baseUrl);
	}

	getCustomerById(id: number): Observable<CustomerResponseDTO> {
		return this.http.get<CustomerResponseDTO>(`${this.baseUrl}/${id}`);
	}

	addCustomer(payload: CustomerRequestDTO): Observable<CustomerResponseDTO> {
		return this.http.post<CustomerResponseDTO>(this.baseUrl, payload);
	}

	updateCustomer(id: number, payload: CustomerUpdateDTO): Observable<CustomerResponseDTO> {
		return this.http.put<CustomerResponseDTO>(`${this.baseUrl}/${id}`, payload);
	}

	deleteCustomer(id: number): Observable<void> {
		return this.http.delete<void>(`${this.baseUrl}/${id}`);
	}

	getCustomerOrders(id: number): Observable<OrderResponseDTO[]> {
		return this.http.get<OrderResponseDTO[]>(`${this.baseUrl}/${id}/orders`);
	}
}
