package com.battlehack;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.battlehack.cart.Product;
import com.battlehack.payment.PaymentMethodPageActivity;
import com.battlehack.util.CartDBOpenHelper;
import com.battlehack.util.ShoppingListCursorAdapter;
import com.battlehack.util.SwipeyHelper;
import com.battlehack.util.SwipeyHelper.Action;
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
	//NFC
	NfcAdapter mAdapter;
	PendingIntent mPendingIntent;
	IntentFilter mFilters[];
	public static final String MIME_TEXT_PLAIN = "text/plain";
	private String STORE_NAME = "NONE";
	
	// private Button mButton;
	private ScanditSDK mBarcodePicker;
	private SlidingUpPanelLayout mMainLayout;
	
	private Cursor mDbCursor;
	private CartDBOpenHelper mHelper;

	private String MyScanditSdkAppKey = "LtvvEDljEeSA45lnMKZSeyWIOk73l0WRPd5GKOUHg3M";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mHelper = new CartDBOpenHelper(this);
		
		checkNFCTag();

		initializeAndStartBarcodeScanning();
	}
	
	public void checkNFCTag() {
		mAdapter = NfcAdapter.getDefaultAdapter(this);
	    mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	    IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
	    try{
	        ndef.addDataType("text/plain");
	    }catch(MalformedMimeTypeException e){
	        throw new RuntimeException("FAILED", e);
	    }
	    Intent intent = getIntent();
	    getNdefMessages(intent);
	}
	
	public void getNdefMessages(Intent intent){
		String action = intent.getAction();
	    if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
	        String type = intent.getType();
	        if (MIME_TEXT_PLAIN.equals(type)) {
	            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
	            new NdefReaderTask().execute(tag);
	             
	        } else {
	            Log.d("NOC", "Wrong mime type: " + type);
	        }
	    }
	}
	
	public void OnClickChoosePaymentMethod(View v) {
		Intent intent = new Intent(this, PaymentMethodPageActivity.class);
		intent.putExtra("STORE", STORE_NAME);
		intent.putExtra("TOTAL", 12.25);
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
		

		mMainLayout.setPanelHeight(500);
		addToCart(cleanedBarcode);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mMainLayout.setPanelHeight(100);;
				
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
		SQLiteDatabase db = mHelper.getReadableDatabase();
		mDbCursor = db.rawQuery("SELECT *, COUNT(*) AS "+ CartDBOpenHelper.PRODUCT_QUANTITY
				+ ", MAX(" + CartDBOpenHelper.TIMESTAMP + ")"
				+ " FROM " + CartDBOpenHelper.CART_TABLE_NAME
				+ " GROUP BY " + CartDBOpenHelper.PRODUCT_NAME
				+ " ORDER BY " + CartDBOpenHelper.TIMESTAMP + " DESC"
				, null);

		startManagingCursor(mDbCursor);
		ListView shoppingList = (ListView) findViewById(R.id.shopping_list);
		final CursorAdapter mAdapter = new ShoppingListCursorAdapter(this, mDbCursor);
		shoppingList.setAdapter(mAdapter);
		
		//Set up stuff
		final SwipeyHelper swiper = new SwipeyHelper();
		shoppingList.setOnTouchListener(swiper);
		shoppingList.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				if(swiper.swipeDetected()) {
		            if(swiper.getAction() == Action.LEFTRIGHT) {
						mDbCursor.moveToPosition(pos);
						removeItem(mDbCursor.getInt(mDbCursor.getColumnIndex(BaseColumns._ID)),false);
		            }
		        }  
			}
		});
	}

	private void addToCart(String barcode) {
		SQLiteDatabase writeDB = mHelper.getWritableDatabase();
		
		Product product = new Product(barcode);
		writeDB.insert(CartDBOpenHelper.CART_TABLE_NAME, null, product.getContentValues());
		writeDB.close();
		updateListView();
	}
	
	private void removeItem(int id, boolean deleteAll){
		SQLiteDatabase writeDB = mHelper.getWritableDatabase();
		writeDB.execSQL("DELETE FROM " + CartDBOpenHelper.CART_TABLE_NAME + " WHERE " + BaseColumns._ID + "= ? ", new String[]{""+id});
//		Cursor c = writeDB.rawQuery( "SELECT * FROM " + CartDBOpenHelper.CART_TABLE_NAME
//				+ " WHERE " + CartDBOpenHelper.PRODUCT_NAME + "=?"
//				, new String[]{name});
//		if(c.moveToFirst()) {
//			Log.i("NOC A", c.getString(c.getColumnIndex(CartDBOpenHelper.PRODUCT_NAME)));
//		}
//		writeDB.delete(CartDBOpenHelper.CART_TABLE_NAME, CartDBOpenHelper.PRODUCT_NAME+"=?", new String[]{name});
//		writeDB.rawQuery( "DELETE FROM " + CartDBOpenHelper.CART_TABLE_NAME
//				+ " WHERE " + CartDBOpenHelper.PRODUCT_NAME + "=?"
//				, new String[]{name});
		writeDB.close();
		updateListView();
	}
	
	//Reads NDEF Tag
	private class NdefReaderTask extends AsyncTask<Tag, Void, String> {
		 
	    @Override
	    protected String doInBackground(Tag... params) {
	        Tag tag = params[0];
	        Ndef ndef = Ndef.get(tag);
	        if (ndef == null) {
	            return null;
	        }
	        NdefMessage ndefMessage = ndef.getCachedNdefMessage();	 
	        NdefRecord[] records = ndefMessage.getRecords();
	        for (NdefRecord ndefRecord : records) {
	            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
	                try {
	                    return readText(ndefRecord);
	                } catch (UnsupportedEncodingException e) {
	                    Log.e("NOC", "Unsupported Encoding", e);
	                }
	            }
	        }
	        return null;
	    }
	    
	    private String readText(NdefRecord record) throws UnsupportedEncodingException {
	        byte[] payload = record.getPayload();
	        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
	        int languageCodeLength = payload[0] & 0063;	         
	        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
	    }
	     
	    @Override
	    protected void onPostExecute(String result) {
	        if (result != null) {
	        	STORE_NAME = result;
	        	Toast.makeText(getApplicationContext(), "You are currently in: " + result, Toast.LENGTH_SHORT).show();
	        }
	    }
	}
	
}
