export interface CustomerRequestDTO {
	name: string;
	email: string;
	address: string;
	username: string;
	password: string;
}

export interface CustomerResponseDTO {
	id: number;
	name: string;
	email: string;
	address: string;
}

export interface CustomerUpdateDTO {
	name?: string;
	email?: string;
	address?: string;
}
