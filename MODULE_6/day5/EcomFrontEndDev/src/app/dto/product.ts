export interface ProductRequestDTO {
	name: string;
	brand: string;
	category: string;
	cost: number;
	description?: string;
}

export interface ProductResponseDTO {
	id: number;
	name: string;
	brand: string;
	category: string;
	cost: number;
	description?: string;
}

export interface ProductUpdateDTO {
	name?: string;
	brand?: string;
	category?: string;
	cost?: number;
	description?: string;
}
