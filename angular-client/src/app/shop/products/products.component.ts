import { Component, OnInit, OnDestroy } from '@angular/core';
import { Product } from '../model/product';
import { ShopService } from '../../services/shop.service';
import { OrderedProduct } from '../model/ordered-product';
import { OrderedProducts } from '../model/ordered-products';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit, OnDestroy {
  orderedProducts: OrderedProduct[] = [];
  private cartOrder: OrderedProducts;
  private total: number;
  sub: Subscription;

  constructor(private shopService: ShopService) { 
  }

  ngOnInit(): void {
    this.orderedProducts = [];
    this.cartOrder = this.shopService.ProductOrders;
    this.loadProducts();
    this.loadOrders();
    this.loadCart();
    this.loadTotal();
  }

  loadProducts() {
    this.shopService.getAllProducts().subscribe(
      (products: any[]) => {
          products.forEach(product => {
            this.orderedProducts.push(new OrderedProduct(product, 0))
          })
       },
      (error) => {
        console.log(error);
      }
    );
  }

  loadCart() {
    this.sub = this.shopService.ProductOrderChanged.subscribe(() => {
      this.shopService.ProductOrders = this.cartOrder;
      this.total = this.calTotal(this.cartOrder.orderedProducts);
      this.shopService.Total = this.total;
    });
  }

  addToCart(order: OrderedProduct) {
    this.shopService.SelectedProductOrder = order;
    this.cartOrder.orderedProducts.push(new OrderedProduct(order.product, order.quantity))
    this.total = this.calTotal(this.cartOrder.orderedProducts);
    this.shopService.Total = this.total;
}

  removeFromCart(productOrder: OrderedProduct) {
    if (this.isProductSelected(productOrder.product)) {
      this.cartOrder.orderedProducts.splice(this.getProductIndex(productOrder.product), 1);
      this.shopService.ProductOrders = this.cartOrder;
      this.cartOrder = this.shopService.ProductOrders;
    }
  }

  isProductSelected(product: Product): boolean {
    return this.getProductIndex(product) > -1;
  }

  getProductIndex(product: Product): number {
    return this.shopService.ProductOrders.orderedProducts.findIndex(
      value => value.product === product
    );
  }

  loadOrders() {
    this.sub = this.shopService.OrdersChanged.subscribe(() => {
      this.cartOrder = this.shopService.ProductOrders;
    })
  }

  loadTotal() {
    this.sub = this.shopService.OrdersChanged.subscribe(() => {
      this.total = this.calTotal(this.cartOrder.orderedProducts);
      this.shopService.Total = this.total;
    });
  }

  private calTotal(products: OrderedProduct[]): number {
    let sum = 0;
    products.forEach(value => {
      sum += value.product.price * value.quantity;
    });
    return sum;
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }
    
  reset() {
    this.orderedProducts = [];
    this.loadProducts();
    this.shopService.ProductOrders.orderedProducts = [];
    this.loadOrders();
  }

}
