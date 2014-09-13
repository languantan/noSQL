package com.battlehack.util;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.battlehack.R;

public class ShoppingListCursorAdapter extends CursorAdapter {

	LayoutInflater mLayoutInflater;
	Resources res;

	public ShoppingListCursorAdapter(Context context, Cursor c) {
		super(context, c, 0);
		mLayoutInflater = LayoutInflater.from(context);
		res = context.getResources();
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		TextView name = (TextView) v.findViewById(R.id.item_name);
		ImageView image = (ImageView) v.findViewById(R.id.product_image);
		TextView quantity = (TextView) v.findViewById(R.id.item_qty);
		// TODO: price
		// TextView price = (TextView) v.findViewById(R.id.item_price)

		String productName = c.getString(c.getColumnIndex(CartDBOpenHelper.PRODUCT_NAME));
		int productImage = c.getInt(c.getColumnIndex(CartDBOpenHelper.PRODUCT_IMAGE));
		int quantityAmt = c.getInt(c.getColumnIndex(CartDBOpenHelper.PRODUCT_QUANTITY));
		
		name.setText(productName);
//		image.setImageResource(productImage);
		quantity.setText(quantityAmt+"");
		
		final Bitmap bitmap = BitmapFactory.decodeResource(res, productImage);
	    final Rounder drawable = new Rounder(res, bitmap);
	    image.setImageDrawable(drawable);
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		return mLayoutInflater.inflate(R.layout.shopping_item, parent, false);
	}

}