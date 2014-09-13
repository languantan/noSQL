package com.battlehack.cart;

import com.battlehack.R;

public class Product {

	private final String barcode;
	private final String name;
	private final String description;
	private final double price;
	private final int image;

	public Product(String barcode) {
		this.barcode = barcode;

		switch (barcode) {
		case "8851019110158":
			this.name = "Pocky Milk Flavour Biscuit Stick";
			this.description = "Crunchy biscuit sticks dipped in a sweet and creamy coating; a perfect snack or dessert anytime.";
			this.price = 0.80;
			this.image = R.drawable.pockymilk_small;
			break;
		case "9556072030182":
			this.name = "Twisties BBQ Curry Dude Flavoured Corn Snacks";
			this.description = "Twisties BBQ Curry Dude Flavoured Corn Snacks are crispy and crunchy and bursting with savoury and spicy curry taste. A delicious treat that is perfect for lunch time or snacking.";
			this.price = 1.00;
			this.image = R.drawable.twistiescurry_small;
			break;
		case "8888002081003":
			this.name = "Sprite Can Soda";
			this.description = "Sprite Can Soda, a refreshing taste of fun and youth.";
			this.price = 1.00;
			this.image = R.drawable.sprite_big;
			break;
		case "8888002049003":
			this.name = "Root Beer Can Soda";
			this.description = "Sprite Can Soda, a refreshing taste of fun and youth.";
			this.price = 1.00;
			this.image = R.drawable.rootbeer_big;
			break;
		case "955619002739":
			this.name = "Ribena Blackcurrant Fruit Drink";
			this.description = "Everyone loves the delicious taste of Ribena Blackcurrant Fruit Drink! It is made with pure New Zealand blackcurrant juice, which is a naturally rich source of vitamin C. It has no artificial colors, flavors, or sweeteners. It is also Halal certified.";
			this.price = 0.70;
			this.image = R.drawable.ribena_small;
			break;
		case "8888002076009":
			this.name = "Coca-Cola";
			this.description = "Enjoy the classic taste of Coke. Enjoyed over a billion times everyday!";
			this.price = 0.90;
			this.image = R.drawable.coke_small;
			break;
		case "8888589338835":
			this.name = "Jia Jia Heritage Herbal Tea";
			this.description = "Brewed from 11 all natural herbs; cooling with a sweet, soothing taste; no preservatives";
			this.price = 0.70;
			this.image = R.drawable.jiajia_small;
			break;
		case "8888200700294":
			this.name = "F&N Seasons Ice Lemon Tea";
			this.description = "F&N Seasons Ice Lemon Tea is a perennial classic that never fails to please. Brewed from real tea leaves with a squeeze of lemon for that authentic, homemade taste that is so refreshing, especially on hot summer days. With no preservatives.";
			this.price = 0.80;
			this.image = R.drawable.lemontea_small;
			break;
		case "028400072724":
			this.name = "Lay's Sour Cream & Onion Potato Chips";
			this.description = "Lay's Sour Cream & Onion Potato Chips are naturally delicious! It all starts with farm grown potatoes, cooked in all natural oil with a dash of salt so every chip is perfectly crispy and tasty. Then we add the tang of sour cream and mild onions. They have no artificial flavors, no preservatives, no MSG, and 0 g of trans fat.";
			this.price = 0.70;
			this.image = R.drawable.layssourcream_small;
			break;
		case "028400019934":
			this.name = "Ruffles Original";
			this.description = "Ruffles Original Potato Chips are made with potatoes, all natural oil and salt for the delicious taste you love. Ridges make these chips extra crunchy and perfect for dipping!";
			this.price = 0.70;
			this.image = R.drawable.rufflesoriginal_small;
			break;
		case "9556057397019":
			this.name = "Kronenbourg 1664 Original Beer";
			this.description = "Award winning pale lager; with a warm golden colour, floral aroma and perfectly balanced sweetness";
			this.price = 3.80;
			this.image = R.drawable.kronenbourg_small;
			break;

		default:
			this.name = "Unrecognized Product";
			this.description = "This product has not been registered on our system.";
			this.price = 0;
			this.image = R.drawable.unknown;
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

	public int image() {
		return image;
	}

	@Override
	public String toString() {
		return "[" + this.barcode + "] " + this.name + ": " + this.price;
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof Product)
				&& this.barcode.equals(((Product) o).barcode);
	}
}