package com.battlehack;

public class CartItem {
  private Product product;
  private int count;

  public CartItem(Product p) {
    product = p;
    count = 1;
  }

  public Product product() {
    return product;
  }

  public int count() {
    return count;
  }

  public void increment() {
    count++;
  }

  public void decrement() {
    count--;
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof CartItem) && product.equals(((CartItem)o).product);
  }

  @Override
  public String toString() {
    return product + " x " + count;
  }
}