import { Routes } from '@angular/router';
import { authGuard, roleGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/login/login').then((m) => m.LoginComponent)
  },
  {
    path: 'signup',
    loadComponent: () => import('./features/signup/signup').then((m) => m.SignupComponent)
  },
  {
    path: '',
    loadComponent: () => import('./features/layout/layout').then((m) => m.LayoutComponent),
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadComponent: () => import('./features/dashboard/dashboard').then((m) => m.DashboardComponent)
      },
      {
        path: 'loans',
        loadComponent: () => import('./features/loans/loans').then((m) => m.LoansComponent)
      },
      {
        path: 'loans/new',
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] },
        loadComponent: () => import('./features/loans/loan-create').then((m) => m.LoanCreateComponent)
      },
      {
        path: 'emis/pay',
        loadComponent: () => import('./features/emis/emi-pay').then((m) => m.EmiPayComponent)
      },
      {
        path: 'customers/register',
        canActivate: [roleGuard],
        data: { roles: ['ADMIN'] },
        loadComponent: () => import('./features/customers/customer-register').then((m) => m.CustomerRegisterComponent)
      },
      {
        path: 'customers/reports',
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] },
        loadComponent: () => import('./features/customers/customer-reports').then((m) => m.CustomerReportsComponent)
      },
      {
        path: 'reports',
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] },
        loadComponent: () => import('./features/reports/reports').then((m) => m.ReportsComponent)
      }
    ]
  },
  { path: '**', redirectTo: '' }
];
