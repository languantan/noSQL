package com.battlehack;

class Product {
  
  private final String barcode;
  private final String name;
  private final String description;
  private final double price;
  private final int image;//Drawable image;

  public Product(String barcode) {
    this.barcode = barcode;

    switch (barcode) {
      case "8851019110158":
        this.name = "Pocky Milk Flavour Biscuit Stick";
        this.description = "Crunchy biscuit sticks dipped in a sweet and creamy coating; a perfect snack or dessert anytime.";
        this.price = 0.80;
        this.image = 1;//R.drawable.pockymilk;
        break;
      case "9556072030182":
        this.name = "Twisties BBQ Curry Dude Flavoured Corn Snacks";
        this.description = "Twisties BBQ Curry Dude Flavoured Corn Snacks are crispy and crunchy and bursting with savoury and spicy curry taste. A delicious treat that is perfect for lunch time or snacking.";
        this.price = 1.00;
        this.image = 2;//R.drawable.twistiescurry;
        break;
      default:
        this.name = "Unrecognized Product";
        this.description = "This product has not been registered on our system.";
        this.price = 0;
        this.image = 0;//R.drawable.unknown;
    }
  }

  public String barcode() {
    return barcode;
  }

  public String name() {
    return name;
  }

  public String description() {
    return description;
  }

  public double price() {
    return price;
  }

  public int image() { //Drawable image() {
    return image;
  }

  @Override
  public String toString() {
    return "[" + this.barcode + "] " + this.name + ": " + this.price;
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Product) && this.barcode.equals(((Product)o).barcode);
  }
}