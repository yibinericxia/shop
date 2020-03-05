import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { OrderedProducts } from '../model/ordered-products';
import { ShopService } from '../../services/shop.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  orderCompleted: boolean;
  order: OrderedProducts;
  total: number;

  @Output() onOrderCompleted: EventEmitter<boolean>;

  constructor(private shopService: ShopService, private router: Router) {
    this.orderCompleted = false;
    this.total = 0;
    this.onOrderCompleted = new EventEmitter<boolean>();
  }

  ngOnInit(): void {
    this.order = this.shopService.ProductOrders;
    this.total = this.shopService.Total;
  }

  reset() {
    this.order = new OrderedProducts();
    this.order.orderedProducts = [];
    this.orderCompleted = false;
    this.total = 0;
  }

  completeOrder() {
    this.orderCompleted = true;
    this.shopService.Total = this.total;
    this.onOrderCompleted.emit(this.orderCompleted);
    this.router.navigate(['/order']);
  }

}
