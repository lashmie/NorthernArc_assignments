import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, map, of, tap } from 'rxjs';
import { jwtRequestDTO } from '../dto/jwt-request';
import { jwtResponseDTO } from '../dto/jwt-response';
import { CustomerRequestDTO, CustomerResponseDTO } from '../dto/customer';

@Injectable({
	providedIn: 'root'
})
export class AuthService {
	private readonly baseUrl = 'http://localhost:8080';
	private readonly tokenKey = 'jwt_token';
	private readonly roleKey = 'ecom_role';
	private readonly userIdKey = 'ecom_user_id';
	private readonly usernameKey = 'ecom_username';

	constructor(private readonly http: HttpClient) {}

	login(payload: jwtRequestDTO): Observable<jwtResponseDTO> {
		return this.http.post<jwtResponseDTO>(`${this.baseUrl}/auth/login`, payload).pipe(
			tap((response) => {
				const token = this.normalizeToken(response.token);
				this.setToken(token);
				this.setRole(response, token);
				this.hydrateRoleFromBackend(token);
			})
		);
	}

	register(payload: CustomerRequestDTO): Observable<CustomerResponseDTO> {
		return this.http.post<CustomerResponseDTO>(`${this.baseUrl}/auth/register`, payload);
	}

	logout(): void {
		localStorage.removeItem(this.tokenKey);
		localStorage.removeItem(this.roleKey);
		localStorage.removeItem(this.userIdKey);
		localStorage.removeItem(this.usernameKey);
	}

	getToken(): string | null {
		return localStorage.getItem(this.tokenKey);
	}

	isLoggedIn(): boolean {
		return !!this.getToken();
	}

	getRole(): string {
		const storedRole = (localStorage.getItem(this.roleKey) ?? '').toUpperCase();
		const token = this.getToken();
		if (!token) {
			return storedRole || 'USER';
		}

		const payload = this.getTokenPayload();
		if (!payload) {
			const usernameFallback = this.getUsernameFromToken();
			return this.inferRoleFromUsername(usernameFallback) || storedRole || 'USER';
		}

		const roleFromToken = this.extractRole(payload);
		if (roleFromToken && roleFromToken !== storedRole) {
			localStorage.setItem(this.roleKey, roleFromToken);
			return roleFromToken;
		}

		const username = this.getUsernameFromToken();
		const inferred = this.inferRoleFromUsername(username);
		if (inferred && inferred !== storedRole) {
			localStorage.setItem(this.roleKey, inferred);
			return inferred;
		}

		return roleFromToken || inferred || storedRole || 'USER';
	}

	getUsernameFromToken(): string | null {
		const payload = this.getTokenPayload();
		const raw = payload?.['sub'] ?? payload?.['username'] ?? localStorage.getItem(this.usernameKey);
		const username = typeof raw === 'string' ? raw.trim() : '';
		if (!username) {
			return null;
		}

		localStorage.setItem(this.usernameKey, username);
		return username;
	}

	getTokenPayload(): Record<string, unknown> | null {
		const token = this.getToken();
		if (!token) {
			return null;
		}

		return this.getTokenPayloadFromToken(token);
	}

	getUserIdFromToken(): number | null {
		const payload = this.getTokenPayload();
		if (payload) {
			const id = this.extractNumericId(payload);
			if (id) {
				localStorage.setItem(this.userIdKey, String(id));
				return id;
			}
		}

		const stored = Number(localStorage.getItem(this.userIdKey));
		return Number.isFinite(stored) && stored > 0 ? stored : null;
	}

	resolveUserId(): Observable<number | null> {
		const cached = this.getUserIdFromToken();
		if (cached) {
			return of(cached);
		}

		const username = this.getUsernameFromToken();
		if (!username) {
			return of(null);
		}

		const encodedUsername = encodeURIComponent(username);
		const probes = [
			`${this.baseUrl}/api/ecom/customer/username/${encodedUsername}`,
			`${this.baseUrl}/api/ecom/customer/by-username/${encodedUsername}`,
			`${this.baseUrl}/api/ecom/customer/me`
		];

		return this.tryResolveUserId(probes, 0);
	}

	private setToken(token: string): void {
		localStorage.setItem(this.tokenKey, token);
	}

	private setRole(response: jwtResponseDTO, token: string): void {
		const roleFromResponse = this.extractRole(response as unknown as Record<string, unknown>);
		if (roleFromResponse) {
			localStorage.setItem(this.roleKey, roleFromResponse);
			const idFromResponse = this.extractNumericId(response as unknown as Record<string, unknown>);
			if (idFromResponse) {
				localStorage.setItem(this.userIdKey, String(idFromResponse));
			}
		}

		const payload = this.getTokenPayloadFromToken(token);
		if (!payload) {
			localStorage.setItem(this.roleKey, this.inferRoleFromUsername(localStorage.getItem(this.usernameKey)) || 'USER');
			return;
		}

		const username = typeof payload['sub'] === 'string' ? payload['sub'].trim() : '';
		if (username) {
			localStorage.setItem(this.usernameKey, username);
		}

		localStorage.setItem(this.roleKey, this.extractRole(payload));
		const idFromToken = this.extractNumericId(payload);
		if (idFromToken) {
			localStorage.setItem(this.userIdKey, String(idFromToken));
		}

		if (!this.extractRole(payload)) {
			const inferred = this.inferRoleFromUsername(username);
			if (inferred) {
				localStorage.setItem(this.roleKey, inferred);
			}
		}
	}

	private hydrateRoleFromBackend(token: string): void {
		const payload = this.getTokenPayloadFromToken(token);
		if (!payload) {
			return;
		}

		const explicitRole = this.extractRole(payload);
		if (explicitRole) {
			return;
		}

		this.http.get<CustomerResponseDTO[]>(`${this.baseUrl}/api/ecom/customer`).pipe(
			map(() => 'ADMIN' as const),
			catchError(() => of('USER' as const))
		).subscribe((resolvedRole) => {
			localStorage.setItem(this.roleKey, resolvedRole);
		});
	}

	private tryResolveUserId(urls: string[], index: number): Observable<number | null> {
		if (index >= urls.length) {
			return of(null);
		}

		return this.http.get<Record<string, unknown>>(urls[index]).pipe(
			map((response) => {
				const id = this.extractNumericId(response);
				if (id) {
					localStorage.setItem(this.userIdKey, String(id));
				}
				return id;
			}),
			catchError(() => this.tryResolveUserId(urls, index + 1))
		);
	}

	private getTokenPayloadFromToken(token: string): Record<string, unknown> | null {
		try {
			const normalizedToken = this.normalizeToken(token);
			const payloadPart = normalizedToken.split('.')[1];
			if (!payloadPart) {
				return null;
			}
			const jsonPayload = this.decodeBase64Url(payloadPart);
			if (!jsonPayload) {
				return null;
			}
			return JSON.parse(jsonPayload) as Record<string, unknown>;
		} catch {
			return null;
		}
	}

	private normalizeToken(token: string): string {
		return token.replace(/^Bearer\s+/i, '').trim();
	}

	private decodeBase64Url(value: string): string | null {
		try {
			const base64 = value.replace(/-/g, '+').replace(/_/g, '/');
			const padded = base64 + '='.repeat((4 - (base64.length % 4)) % 4);
			return atob(padded);
		} catch {
			return null;
		}
	}

	private extractRole(payload: Record<string, unknown>): string {
		const candidates: unknown[] = [
			payload['roles'],
			payload['role'],
			payload['authorities'],
			payload['scope'],
			payload['scp']
		];

		for (const candidate of candidates) {
			const normalized = this.normalizeRoleCandidate(candidate);
			if (normalized) {
				return normalized;
			}
		}

		return '';
	}

	private normalizeRoleCandidate(value: unknown): string | null {
		if (!value) {
			return null;
		}

		if (Array.isArray(value)) {
			for (const entry of value) {
				const fromEntry = this.normalizeRoleCandidate(entry);
				if (fromEntry) {
					return fromEntry;
				}
			}
			return null;
		}

		if (typeof value === 'object') {
			const obj = value as Record<string, unknown>;
			return this.normalizeRoleCandidate(obj['authority'] ?? obj['role'] ?? obj['name']);
		}

		const tokenText = String(value).trim();
		if (!tokenText) {
			return null;
		}

		const parts = tokenText.split(/[\s,]+/).filter(Boolean);
		for (const part of parts) {
			const sanitized = part.replace(/[\[\]{}()'\"]/g, '');
			const normalized = sanitized.replace('ROLE_', '').toUpperCase();
			if (normalized === 'ADMIN' || normalized === 'USER') {
				return normalized;
			}
		}

		return null;
	}

	private extractNumericId(payload: Record<string, unknown>): number | null {
		const userObj = typeof payload['user'] === 'object' && payload['user']
			? (payload['user'] as Record<string, unknown>)
			: null;

		const candidates = [
			payload['customerId'],
			payload['customer_id'],
			payload['customerID'],
			payload['userId'],
			payload['user_id'],
			payload['uid'],
			payload['id'],
			payload['subId'],
			payload['sub_id'],
			payload['sub'],
			userObj?.['id'],
			userObj?.['userId'],
			userObj?.['customerId']
		];

		for (const candidate of candidates) {
			const parsed = Number(candidate);
			if (Number.isFinite(parsed) && parsed > 0) {
				return parsed;
			}
		}

		return null;
	}

	private inferRoleFromUsername(username: string | null): string | null {
		if (!username) {
			return null;
		}

		const normalized = username.trim().toLowerCase();
		if (normalized === 'admin') {
			return 'ADMIN';
		}

		return 'USER';
	}
}
