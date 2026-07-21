import { Component, computed, inject, signal } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { Role } from '../../core/models';

interface NavItem {
  label: string;
  path: string;
  icon: string;
  roles: Role[];
}

@Component({
  selector: 'app-layout',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './layout.html',
  styleUrl: './layout.css'
})
export class LayoutComponent {
  private auth = inject(AuthService);
  private router = inject(Router);

  sidebarOpen = signal(true);

  role = this.auth.role;
  email = this.auth.email;

  private readonly allItems: NavItem[] = [
    { label: 'Dashboard', path: '/dashboard', icon: '📊', roles: ['ADMIN', 'MANAGER', 'USER'] },
    { label: 'Loans', path: '/loans', icon: '💳', roles: ['ADMIN', 'MANAGER', 'USER'] },
    { label: 'New Loan', path: '/loans/new', icon: '➕', roles: ['ADMIN', 'MANAGER'] },
    { label: 'Pay EMI', path: '/emis/pay', icon: '💰', roles: ['ADMIN', 'MANAGER', 'USER'] },
    { label: 'Register Customer', path: '/customers/register', icon: '🧑', roles: ['ADMIN'] },
    { label: 'Customer Reports', path: '/customers/reports', icon: '⚠️', roles: ['ADMIN', 'MANAGER'] },
    { label: 'Loan Reports', path: '/reports', icon: '📈', roles: ['ADMIN', 'MANAGER'] }
  ];

  navItems = computed(() => {
    const current = this.role();
    if (!current) return [];
    return this.allItems.filter((item) => item.roles.includes(current));
  });

  toggleSidebar(): void {
    this.sidebarOpen.update((v) => !v);
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
