import { Routes } from '@angular/router';

import { CartComponent } from './components/cart/cart.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ProductListComponent } from './components/product/product-list/product-list.component';
import { CustomerListComponent } from './components/customer/customer-list/customer-list.component';
import { LoginComponent } from './components/auth/login/login.component';
import { LogoutComponent } from './components/auth/logout/logout.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AuthGuardService } from './services/auth-guard.service';
import { CustomerDetailsComponent } from './components/customer/customer-details/customer-details.component';
import { UpdateCustomerComponent } from './components/customer/update-customer/update-customer.component';
import { AddCustomerComponent } from './components/customer/add-customer/add-customer.component';
import { PlaceOrderComponent } from './components/order/place-order/place-order.component';
import { ProductDetailsComponent } from './components/product/product-details/product-details.component';
import { UpdateProductComponent } from './components/product/update-product/update-product.component';
import { AddProductComponent } from './components/product/add-product/add-product.component';
import { OrderDetailsComponent } from './components/order/order-details/order-details.component';
import { OrderListComponent } from './components/order/order-list/order-list.component';

export const routes: Routes = [
	{ path: '', pathMatch: 'full', redirectTo: 'products' },
	{ path: 'login', component: LoginComponent },
	{ path: 'logout', component: LogoutComponent },
	{ path: 'register', component: RegisterComponent },
	{ path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuardService] },

	{ path: 'customers', component: CustomerListComponent, canActivate: [AuthGuardService], data: { roles: ['ADMIN'] } },
	{ path: 'customers/add', component: AddCustomerComponent, canActivate: [AuthGuardService], data: { roles: ['ADMIN'] } },
	{ path: 'customers/:id', component: CustomerDetailsComponent, canActivate: [AuthGuardService], data: { roles: ['ADMIN'] } },
	{ path: 'customers/update/:id', component: UpdateCustomerComponent, canActivate: [AuthGuardService], data: { roles: ['ADMIN'] } },

	{ path: 'products', component: ProductListComponent, canActivate: [AuthGuardService], data: { roles: ['ADMIN', 'USER'] } },
	{ path: 'add-product', component: AddProductComponent, canActivate: [AuthGuardService], data: { roles: ['ADMIN'] } },
	{ path: 'products/add', component: AddProductComponent, canActivate: [AuthGuardService], data: { roles: ['ADMIN'] } },
	{ path: 'products/:id', component: ProductDetailsComponent, canActivate: [AuthGuardService], data: { roles: ['ADMIN', 'USER'] } },
	{ path: 'products/update/:id', component: UpdateProductComponent, canActivate: [AuthGuardService], data: { roles: ['ADMIN'] } },
	{ path: 'cart', component: CartComponent, canActivate: [AuthGuardService], data: { roles: ['USER'] } },
	{ path: 'checkout', component: PlaceOrderComponent, canActivate: [AuthGuardService], data: { roles: ['USER'] } },
	{ path: 'profile', component: ProfileComponent, canActivate: [AuthGuardService], data: { roles: ['USER'] } },

	{ path: 'orders', component: OrderListComponent, canActivate: [AuthGuardService], data: { roles: ['ADMIN', 'USER'] } },
	{ path: 'orders/:id', component: OrderDetailsComponent, canActivate: [AuthGuardService], data: { roles: ['ADMIN', 'USER'] } },

	{ path: '**', redirectTo: 'login' }
];
