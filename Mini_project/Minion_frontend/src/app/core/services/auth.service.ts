import { Injectable, computed, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { API_BASE_URL } from '../api.config';
import { AuthResponse, Customer, LoginRequest, RegisterRequest, Role } from '../models';

const TOKEN_KEY = 'minion_token';
const ROLE_KEY = 'minion_role';
const EXP_KEY = 'minion_exp';
const EMAIL_KEY = 'minion_email';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly base = `${API_BASE_URL}/api/auth`;

  private readonly _role = signal<Role | null>(this.readValidRole());
  private readonly _email = signal<string | null>(localStorage.getItem(EMAIL_KEY));

  readonly role = this._role.asReadonly();
  readonly email = this._email.asReadonly();
  readonly isLoggedIn = computed(() => this._role() !== null);

  constructor(private http: HttpClient) {}

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.base}/login`, request).pipe(
      tap((res) => this.storeSession(res, request.email))
    );
  }

  register(request: RegisterRequest): Observable<Customer> {
    return this.http.post<Customer>(`${this.base}/register`, request);
  }

  signup(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.base}/signup`, request).pipe(
      tap((res) => this.storeSession(res, request.email))
    );
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(ROLE_KEY);
    localStorage.removeItem(EXP_KEY);
    localStorage.removeItem(EMAIL_KEY);
    this._role.set(null);
    this._email.set(null);
  }

  getToken(): string | null {
    const exp = Number(localStorage.getItem(EXP_KEY));
    if (exp && Date.now() > exp) {
      this.logout();
      return null;
    }
    return localStorage.getItem(TOKEN_KEY);
  }

  hasRole(...roles: Role[]): boolean {
    const current = this._role();
    return current !== null && roles.includes(current);
  }

  private storeSession(res: AuthResponse, email: string): void {
    localStorage.setItem(TOKEN_KEY, res.token);
    localStorage.setItem(ROLE_KEY, res.role);
    localStorage.setItem(EXP_KEY, String(res.expiresAtEpochMillis));
    localStorage.setItem(EMAIL_KEY, email);
    this._role.set(res.role);
    this._email.set(email);
  }

  private readValidRole(): Role | null {
    const exp = Number(localStorage.getItem(EXP_KEY));
    if (exp && Date.now() > exp) {
      this.logout();
      return null;
    }
    return (localStorage.getItem(ROLE_KEY) as Role | null) ?? null;
  }
}
