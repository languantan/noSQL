package com.battlehack.payment;

import java.math.BigDecimal;
import java.sql.SQLData;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.battlehack.R;
import com.battlehack.util.CartDBOpenHelper;
import com.paypal.android.sdk.cb;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class PaymentMethodPageActivity extends Activity {
	// Testing credentials
	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
	private static final String CONFIG_CLIENT_ID = "AWmIMhDw_Mzxplky70GIajE9GmO_tNS2x_GUiVxljWFDaNbW-n2C7mrNj9sM";
	private static final String CONFIG_CLIENT_SECRET = "ENCzdhA2bGRbCu7OA3_wGzKgZXzglzyWP8Zo-62jrB1Ejg5uGhQWCyVXi";

	private static String STORE_NAME = "XYZ NOCULAR";
	private double TOTAL = 0;
	private static int REQUEST_CODE = 100;
	private boolean txnComplete = false;

	private TextView tv;

	private static PayPalConfiguration config = new PayPalConfiguration()
			.environment(CONFIG_ENVIRONMENT)
			.clientId(CONFIG_CLIENT_ID)
			// The following are only used in PayPalFuturePaymentActivity.
			.merchantName("XYZ NOCULAR")
			.merchantPrivacyPolicyUri(
					Uri.parse("https://www.example.com/privacy"))
			.merchantUserAgreementUri(
					Uri.parse("https://www.example.com/legal"));

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_payment_method_page);

		TOTAL = getIntent().getDoubleExtra("TOTAL", 0.0);
		STORE_NAME = getIntent().getStringExtra("STORE");

		tv = (TextView) findViewById(R.id.tv_pay_storename);

		setUpPayPalService();
		startUpPayPalService(TOTAL);
	}

	public void setUpPayPalService() {
		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		startService(intent);
	}

	public void startUpPayPalService(double total) {
		PayPalPayment txn = getTransactionDetails(
				PayPalPayment.PAYMENT_INTENT_SALE, total);
		Intent intent = new Intent(this, PaymentActivity.class);
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, txn);
		startActivityForResult(intent, REQUEST_CODE);
		
	}

	private PayPalPayment getTransactionDetails(String paymentIntent,
			double total) {
		return new PayPalPayment(new BigDecimal(total), "SGD", STORE_NAME,
				paymentIntent);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				PaymentConfirmation confirm = data
						.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				if (confirm != null) {
					try {
						String tmp = confirm.getPayment().toJSONObject()
								.toString(4);
						txnComplete = true;
						CartDBOpenHelper dbHelper = new CartDBOpenHelper(this);
						SQLiteDatabase db = dbHelper.getWritableDatabase();
						db.delete(CartDBOpenHelper.CART_TABLE_NAME, null, null);
						db.close();
					} catch (JSONException e) {
						Toast.makeText(getApplicationContext(), "GG",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.payment_method_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
