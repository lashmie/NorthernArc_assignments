import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
	providedIn: 'root'
})
export class AuthGuardService implements CanActivate {
	constructor(
		private readonly authService: AuthService,
		private readonly router: Router
	) {}

	canActivate(route: ActivatedRouteSnapshot, _state: RouterStateSnapshot): boolean {
		if (!this.authService.isLoggedIn()) {
			this.router.navigate(['/login']);
			return false;
		}

		const expectedRoles = (route.data['roles'] as string[] | undefined) ?? [];
		if (expectedRoles.length === 0) {
			return true;
		}

		const userRole = this.authService.getRole();
		const username = this.authService.getUsernameFromToken();
		const inferredRole = username?.toLowerCase() === 'admin' ? 'ADMIN' : userRole;
		const allowed = expectedRoles.includes(inferredRole);
		if (!allowed) {
			this.router.navigate(['/dashboard']);
			return false;
		}

		return true;
	}
}
