import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrderRequestDTO, OrderResponseDTO, OrderUpdateDTO } from '../dto/order';

@Injectable({
	providedIn: 'root'
})
export class OrderService {
	private readonly baseUrl = 'http://localhost:8080/api/ecom/order';

	constructor(private readonly http: HttpClient) {}

	getAllOrders(): Observable<OrderResponseDTO[]> {
		return this.http.get<OrderResponseDTO[]>(this.baseUrl);
	}

	getOrderById(id: number): Observable<OrderResponseDTO> {
		return this.http.get<OrderResponseDTO>(`${this.baseUrl}/${id}`);
	}

	placeOrder(payload: OrderRequestDTO): Observable<OrderResponseDTO> {
		return this.http.post<OrderResponseDTO>(this.baseUrl, payload);
	}

	updateOrder(id: number, payload: OrderUpdateDTO): Observable<OrderResponseDTO> {
		return this.http.put<OrderResponseDTO>(`${this.baseUrl}/${id}`, payload);
	}

	cancelOrder(id: number): Observable<void> {
		return this.http.delete<void>(`${this.baseUrl}/${id}`);
	}

	getOrdersByCustomer(id: number): Observable<OrderResponseDTO[]> {
		return this.http.get<OrderResponseDTO[]>(`${this.baseUrl}/customer/${id}`);
	}
}
