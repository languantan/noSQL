package com.battlehack.util;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.battlehack.R;

public class ShoppingListCursorAdapter extends CursorAdapter {

	LayoutInflater mLayoutInflater;

	public ShoppingListCursorAdapter(Context context, Cursor c) {
		super(context, c, 0);
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		TextView name = (TextView) v.findViewById(R.id.item_name);
		ImageView image = (ImageView) v.findViewById(R.id.product_image);
		// TODO: price
		// TextView price = (TextView) v.findViewById(R.id.item_price);
		// TODO: quantity
		// TextView quantity = (TextView) v.findViewById(R.id.item_quantity);
		
		name.setText(c.getString(c.getColumnIndex(CartDBOpenHelper.PRODUCT_NAME)));
		image.setImageResource(c.getInt(c.getColumnIndex(CartDBOpenHelper.PRODUCT_NAME)));

	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		return mLayoutInflater.inflate(R.layout.shopping_item, parent, false);
	}

}