export interface OrderRequestDTO {
	productId: number;
	quantity: number;
}

export interface OrderResponseDTO {
	id: number;
	customerId: number;
	customerName: string;
	orderItemIds: number[];
	status: string;
	orderDate?: string;
}

export interface OrderUpdateDTO {
	customerId?: number;
	orderItemIds?: number[];
	status: string;
}
