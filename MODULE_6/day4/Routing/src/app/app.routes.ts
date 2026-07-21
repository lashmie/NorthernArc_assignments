// import { Routes } from '@angular/router';

// export const routes: Routes = [
//     //loading component home ,servies ,contact us ,about (lazy loading)
//     {path:'',components:home},
//    // {path: 'home', loadComponent: () => import('./home/home.component').then(m => m.HomeComponent)},
//     {path: 'services', loadComponent: () => import('./services/services.component').then(m => m.ServicesComponent)},
//     {path: 'contact', loadComponent: () => import('./contact/contact.component').then(m => m.ContactComponent)},
//     {path: 'about', loadComponent: () => import('./about/about.component').then(m => m.AboutComponent)},
//     ];
import { Routes } from '@angular/router';
import {App} from './app';
import {Home} from './components/home/home';
import {About} from './components/about/about';
import {Services} from './components/services/services';
import {Contact} from './components/contact/contact';

export const routes: Routes = [
  {path: '', component: Home},//this is eager loading
  {path: 'about', component: About},
  {path: 'services', component: Services},
  {path:'contact',loadComponent: () => import('./components/contact/contact').then(m => m.Contact)},
 
{path:'person/:id', loadComponent: () => import('./components/person/person').then(m => m.Person)},//dynamic route
 
//   this is lazy loading
//   {path:'contact',loadComponent: () => import('./components/contact/contact').then(m => m.Contact)}
//redirect always give atlast
  {path: '**', redirectTo: ''}
];
 