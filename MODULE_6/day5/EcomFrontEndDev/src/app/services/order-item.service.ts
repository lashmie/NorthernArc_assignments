import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrderItemResponseDTO } from '../dto/order-item';

@Injectable({
  providedIn: 'root'
})
export class OrderItemService {
  private readonly baseUrl = 'http://localhost:8080/api/ecom/orderitem';

  constructor(private readonly http: HttpClient) {}

  getItemsByOrderId(id: number): Observable<OrderItemResponseDTO[]> {
    return this.http.get<OrderItemResponseDTO[]>(`${this.baseUrl}/order/${id}`);
  }
}
