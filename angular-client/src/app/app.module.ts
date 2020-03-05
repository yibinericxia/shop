import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ShopComponent } from './shop/shop.component';
import { ProductsComponent } from './shop/products/products.component';
import { OrdersComponent } from './shop/orders/orders.component';
import { CartComponent } from './shop/cart/cart.component';
import { ShopService } from './services/shop.service';
import { LoginComponent } from './shop/login/login.component';
import { AuthService } from './services/auth.service';
import { NavbarComponent } from './shop/navbar/navbar.component';

@NgModule({
  declarations: [
    AppComponent,
    ShopComponent,
    ProductsComponent,
    OrdersComponent,
    CartComponent,
    LoginComponent,
    NavbarComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [
    ShopService,
    AuthService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
