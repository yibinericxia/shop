import { Component, OnInit } from '@angular/core';
import { OrderedProducts } from '../model/ordered-products';
import { ShopService } from '../../services/shop.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {
  paid: boolean;
  total: number;
  order: OrderedProducts;

  constructor(private shopService: ShopService) { 
  }

  ngOnInit(): void {
    this.paid = this.shopService.Paid;
    this.total = this.shopService.Total;
    this.order = this.shopService.ProductOrders;
  }

  reset() {
    this.paid = false;
    this.total = 0;
    this.order.orderedProducts = [];
  }

  pay() {
    this.shopService.saveOrder(this.order).subscribe((res) => {
      this.paid = true;
      this.shopService.Paid = this.paid;
      this.shopService.StartOver = true;
    },
    (err: HttpErrorResponse) => {
      if (err.error instanceof Error) {
        console.log('An error occurred:', err.error.message);
      } else {
        console.log(`backend error code ${err.status}, details: ${err.error.message}`);
      }
    });
  }
}
