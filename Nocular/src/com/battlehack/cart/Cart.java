package com.battlehack.cart;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Cart implements Iterable<CartItem> {

  private List<CartItem> items;

  public Cart() {
    items = new ArrayList<CartItem>();
  }

  public Iterator<CartItem> iterator() {
    return new CartIterator(this);
  }

  // adds CartItem to cart and return the number of that CartItem in the cart
  public int add(Product p) {
    int index = items.indexOf(new CartItem(p));
    if (index < 0) {   // new item
      items.add(new CartItem(p));
      return 1;
    } else {
      items.get(index).increment();
      return items.get(index).count();
    }
  }

  // removes the CartItem if it exists
  // returns the last count of CartItem, or 0 if it doesn't exist
  public int remove(Product p) {
    int index = items.indexOf(new CartItem(p));
    if (index < 0) {
      return 0;
    } else {
      CartItem item = items.get(index);
      int n = item.count();
      if (n>1) {
        item.decrement();
        return items.get(index).count();
      } else {
        items.remove(index);
        return n;
      }
    }
  }

  public int size() {
    return items.size();
  }

  public CartItem get(int i) {
    return items.get(i);
  }

  public static void main(String[] args) {
    Cart cart = new Cart();
    Product p1 = new Product("123");
    Product p2 = new Product("456");
    Product p3 = new Product("123");
    cart.add(p1);
    cart.add(p2);
    cart.add(p3);

    System.out.println("list of items in cart");
    for (CartItem item: cart) {
      System.out.println(item);
    }
  }
}