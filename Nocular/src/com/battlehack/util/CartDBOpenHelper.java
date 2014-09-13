package com.battlehack.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class CartDBOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "nocular";
	private static final int DATABASE_VERSION = 1;
	public static final String CART_TABLE_NAME = "shoppingcart";
	public static final String TIMESTAMP = "timestamp";
	public static final String PRODUCT_NAME = "name";
	public static final String PRODUCT_IMAGE = "image";
	public static final String ITEM_PRICE = "item_price";
	
	//FOR COUNT(*) AS Property
	public static final String PRODUCT_QUANTITY = "quantity";
	
	private static final String CART_TABLE_CREATE = "CREATE TABLE "
			+ CART_TABLE_NAME + " (" 
			+ BaseColumns._ID + " INTEGER PRIMARY KEY,"
			+ TIMESTAMP + " DATETIME DEFAULT (datetime('now', 'localtime')), "
			+ PRODUCT_NAME + " TEXT, "
			+ PRODUCT_IMAGE + " INTEGER, "
			+ ITEM_PRICE + " REAL);";

	public CartDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CART_TABLE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CART_TABLE_NAME);
		onCreate(db);
	}

}
