import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductRequestDTO, ProductResponseDTO, ProductUpdateDTO } from '../dto/product';

@Injectable({
	providedIn: 'root'
})
export class ProductService {
	private readonly baseUrl = 'http://localhost:8080/api/ecom/product';

	constructor(private readonly http: HttpClient) {}

	getAllProducts(): Observable<ProductResponseDTO[]> {
		return this.http.get<ProductResponseDTO[]>(this.baseUrl);
	}

	getProductById(id: number): Observable<ProductResponseDTO> {
		return this.http.get<ProductResponseDTO>(`${this.baseUrl}/${id}`);
	}

	addProduct(payload: ProductRequestDTO): Observable<ProductResponseDTO> {
		return this.http.post<ProductResponseDTO>(this.baseUrl, payload);
	}

	updateProduct(id: number, payload: ProductUpdateDTO): Observable<ProductResponseDTO> {
		return this.http.put<ProductResponseDTO>(`${this.baseUrl}/${id}`, payload);
	}

	deleteProduct(id: number): Observable<void> {
		return this.http.delete<void>(`${this.baseUrl}/${id}`);
	}

	getProductsByCategory(category: string): Observable<ProductResponseDTO[]> {
		return this.http.get<ProductResponseDTO[]>(`${this.baseUrl}/category/${category}`);
	}

	getProductsByBrand(brand: string): Observable<ProductResponseDTO[]> {
		return this.http.get<ProductResponseDTO[]>(`${this.baseUrl}/brand/${brand}`);
	}

	getProductsByName(name: string): Observable<ProductResponseDTO[]> {
		return this.http.get<ProductResponseDTO[]>(`${this.baseUrl}/name/${name}`);
	}
}
