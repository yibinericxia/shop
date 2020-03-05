import { NgModule } from '@angular/core';
import { RouterModule, Routes }  from '@angular/router';

import { ShopComponent } from './shop/shop.component';
import { LoginComponent } from './shop/login/login.component';
import { OrdersComponent } from './shop/orders/orders.component';
import { CartComponent } from './shop/cart/cart.component';

const routes: Routes = [
  { path: '', component: ShopComponent },
  { path: 'cart', component: CartComponent },
  { path: 'order', component: OrdersComponent },
  { path: 'login', component: LoginComponent },
  { path: '**', redirectTo: '/', pathMatch: 'full' }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes,
//      { enableTracing: true } // <-- debugging purposes only
    )
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
