export interface OrderItemRequestDTO {
  quantity: number;
  productId: number;
  orderId: number;
}

export interface OrderItemResponseDTO {
  id: number;
  quantity: number;
  productId: number;
  productName: string;
  unitPrice?: number;
  orderId: number;
}

export interface OrderItemUpdateDTO {
  quantity?: number;
  productId?: number;
  orderId?: number;
}
