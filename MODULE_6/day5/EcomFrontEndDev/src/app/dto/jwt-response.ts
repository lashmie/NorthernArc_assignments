export interface jwtResponseDTO {
	token: string;
	role?: string;
	roles?: string[];
	authorities?: Array<string | { authority?: string; role?: string; name?: string }>;
}
