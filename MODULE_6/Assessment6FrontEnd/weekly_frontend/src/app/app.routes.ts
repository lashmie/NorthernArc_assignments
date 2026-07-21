import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./components/home/home').then((m) => m.Home),
    title: 'Home | ScanManager'
  },
  {
    path: 'scans',
    loadComponent: () =>
      import('./components/scan/scan-list/scan-list').then((m) => m.ScanList),
    title: 'All Scans | ScanManager'
  },
  {
    path: 'scans/add',
    loadComponent: () =>
      import('./components/scan/add-scan/add-scan').then((m) => m.AddScan),
    title: 'Add Scan | ScanManager'
  },
  {
    path: 'scans/search',
    loadComponent: () =>
      import('./components/scan/search-scan/search-scan').then((m) => m.SearchScan),
    title: 'Search Scans | ScanManager'
  },
  {
    path: 'scans/:id',
    loadComponent: () =>
      import('./components/scan/scan-details/scan-details').then((m) => m.ScanDetails),
    title: 'Scan Details | ScanManager'
  },
  {
    path: '**',
    loadComponent: () =>
      import('./components/not-found/not-found').then((m) => m.NotFound),
    title: 'Not Found | ScanManager'
  }
];
