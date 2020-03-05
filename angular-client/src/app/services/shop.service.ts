import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Product } from '../shop/model/product';
import { OrderedProduct } from '../shop/model/ordered-product';
import { OrderedProducts } from '../shop/model/ordered-products';

@Injectable({
  providedIn: 'root'
})
export class ShopService {

  private productsUrl = "/api/products";
  private ordersUrl = "/api/orders";

  private orderedProduct: OrderedProduct;
  private order: OrderedProducts = new OrderedProducts();
  private total: number;
  private paid: boolean;
  private startOver = true;

  private productOrderSubject = new Subject();
  private ordersSubject = new Subject();

  ProductOrderChanged = this.productOrderSubject.asObservable();
  OrdersChanged = this.ordersSubject.asObservable();
 
  constructor(private http: HttpClient) { }

  reset() {
    this.order.orderedProducts = [];
    this.total = 0;
    this.paid = false;
  }

  getAllProducts() {
    return this.http.get(this.productsUrl);
  }

  saveOrder(order: OrderedProducts) {
    order.createdTime = new Date();
    return this.http.post(this.ordersUrl, order);
  }

  set SelectedProductOrder(value: OrderedProduct) {
    this.orderedProduct = value;
    this.productOrderSubject.next();
  }

  get SelectedProductOrder() {
    return this.orderedProduct;
  }

  set ProductOrders(value: OrderedProducts) {
    this.order = value;
    this.ordersSubject.next();
  }

  get ProductOrders() {
    return this.order;
  }

  set Total(value: number) {
    this.total = value;
  }

  get Total() {
    return this.total;
  }

  set Paid(value: boolean) {
    this.paid = value;
  }

  get Paid() {
    return this.paid;
  }

  set StartOver(value: boolean) {
    this.startOver = value;
  }

  get StartOver() {
    return this.startOver;
  }
}
