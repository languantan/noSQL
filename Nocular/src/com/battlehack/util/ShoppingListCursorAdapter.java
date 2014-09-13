package com.battlehack.util;

import com.battlehack.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShoppingListCursorAdapter extends CursorAdapter{
	
	LayoutInflater mLayoutInflater;

	public ShoppingListCursorAdapter(Context context, Cursor c) {
		super(context, c, 0);
		mLayoutInflater = LayoutInflater.from(context);
		
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		
		
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		return mLayoutInflater.inflate(R.layout.);
	}

	
	
	
}