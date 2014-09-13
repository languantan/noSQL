package com.battlehack;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.battlehack.cart.Product;
import com.battlehack.payment.PaymentMethodPageActivity;
import com.battlehack.util.CartDBOpenHelper;
import com.battlehack.util.ShoppingListCursorAdapter;
import com.battlehack.util.SystemUiHider;
import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class NocularActivity extends Activity implements ScanditSDKListener {

	// private Button mButton;
	private ScanditSDK mBarcodePicker;
	private SlidingUpPanelLayout mMainLayout;
	private Cursor mDbCursor;

	private String MyScanditSdkAppKey = "LtvvEDljEeSA45lnMKZSeyWIOk73l0WRPd5GKOUHg3M";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		initializeAndStartBarcodeScanning();
	}
	
	public void OnClickChoosePaymentMethod(View v) {
		Intent intent = new Intent(this, PaymentMethodPageActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		// When the activity is in the background immediately stop the
		// scanning to save resources and free the camera.
		mBarcodePicker.stopScanning();

		super.onPause();
	}

	@Override
	protected void onResume() {
		// Once the activity is in the foreground again, restart scanning.
		mBarcodePicker.startScanning();
		super.onResume();
	}

	public void initializeAndStartBarcodeScanning() {
		// Switch to full screen.

		// We instantiate the automatically adjusting barcode picker that will
		// choose the correct picker to instantiate. Be aware that this picker
		// should only be instantiated if the picker is shown full screen as the
		// legacy picker will rotate the orientation and not properly work in
		// non-fullscreen.
		ScanditSDKAutoAdjustingBarcodePicker picker = new ScanditSDKAutoAdjustingBarcodePicker(
				this, MyScanditSdkAppKey,
				ScanditSDKAutoAdjustingBarcodePicker.CAMERA_FACING_BACK);

		// Add both views to activity, with the scan GUI on top.

		mMainLayout = (SlidingUpPanelLayout) getLayoutInflater().inflate(
				R.layout.activity_nocular, null);
		LayoutParams pickerParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mMainLayout.addView(picker, pickerParams);

		mBarcodePicker = picker;

		mBarcodePicker.getOverlayView().setBeepEnabled(false);
		// Register listener, in order to be notified about relevant events
		// (e.g. a successfully scanned bar code).
		mBarcodePicker.getOverlayView().addListener(this);

		getLayoutInflater().inflate(R.layout.frame_sliding, mMainLayout);

		setContentView(mMainLayout);
	}

	public void didScanBarcode(String barcode, String symbology) {
		// Remove non-relevant characters that might be displayed as rectangles
		// on some devices. Be aware that you normally do not need to do this.
		// Only special GS1 code formats contain such characters.
		String cleanedBarcode = "";
		for (int i = 0; i < barcode.length(); i++) {
			if (barcode.charAt(i) > 30) {
				cleanedBarcode += barcode.charAt(i);
			}
		}

		mMainLayout.expandPanel((float)0.2);
		addToCart(cleanedBarcode);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mMainLayout.collapsePanel();
				
			}
		}, 2000);

//		ListView shoppingList = (ListView) findViewById(R.id.shopping_list);
//		String[] names = new String[] { "Coke", "Sprite", "Herbal Tea" };
//
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//				R.layout.shopping_item, R.id.item_name, names);
//		shoppingList.setAdapter(adapter);

		/*
		 * TextView txtProductName = (TextView) findViewById(R.id.product_name);
		 * txtProductName.setText(product.name());
		 * 
		 * ImageView imageProduct = (ImageView)
		 * findViewById(R.id.product_image);
		 * imageProduct.setImageResource(product.image());
		 */

		

	}

	/**
	 * Called when the user entered a bar code manually.
	 * 
	 * @param entry
	 *            The information entered by the user.
	 */
	public void didManualSearch(String entry) {
		// Example code that would typically be used in a real-world app using
		// the Scandit SDK.
		/*
		 * // Stop recognition to save resources. mBarcodePicker.stopScanning();
		 */
		Toast.makeText(this, "User entered: " + entry, Toast.LENGTH_LONG)
				.show();
	}

	public void didCancel() {
		mBarcodePicker.stopScanning();
		finish();
	}

	@Override
	public void onBackPressed() {
		mBarcodePicker.stopScanning();
		finish();
	}

	private void updateListView() {
		CartDBOpenHelper dbHelper = new CartDBOpenHelper(this);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		mDbCursor = db.query(CartDBOpenHelper.CART_TABLE_NAME, null, null,
				null, null, null, null, null);

		startManagingCursor(mDbCursor);
		ListView shoppingList = (ListView) findViewById(R.id.shopping_list);

		CursorAdapter mAdapter = new ShoppingListCursorAdapter(this, mDbCursor);
		shoppingList.setAdapter(mAdapter);
	}

	private void addToCart(String barcode) {
		Product product = new Product(barcode);
		
		CartDBOpenHelper dbHelper = new CartDBOpenHelper(this);
		SQLiteDatabase writeDB = dbHelper.getWritableDatabase();

		writeDB.insert(CartDBOpenHelper.CART_TABLE_NAME, null, product.getContentValues());
		writeDB.close();
		updateListView();
	}
}
