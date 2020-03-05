import { Category } from './category';

export class Product {
  id: number;
  name: string;
  categories: Category[];
  price: number;
  imageUrl: string;

  constructor(obj?: Partial<Product>) {
    Object.assign(this, obj);
  }
}