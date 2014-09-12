package com.battlehack.cart;

import java.util.Iterator;

public class CartIterator implements Iterator<CartItem> {
  private Cart cart;
  private int ptr = 0;

  public CartIterator(Cart c) {
    cart = c;
  }

  public boolean hasNext() {
    return ptr < cart.size();
  }

  public CartItem next() {
    return cart.get(ptr++);
  }

  public void remove() {
    throw new UnsupportedOperationException();
  }
}