package com.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.bean.MyDBHelper;
import com.example.bean.SettingData;

import android.app.*;
import android.content.*;
import android.database.Cursor;
import android.os.*;
import android.provider.ContactsContract;
import android.telephony.*;
import android.view.*;
import android.widget.*;

public class WriteMessageActivity extends Activity {
	private Button sendSMSButton;
	private AutoCompleteTextView phoneNumberText;
	private EditText messageText;
	private MyDBHelper myDBHelper;
	private List<String> strs;

	private View.OnClickListener onSendButtonClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			sendOrSave("1");
			Intent intent = new Intent(WriteMessageActivity.this,WriteMessageActivity.class);
			startActivity(intent);
			WriteMessageActivity.this.finish();
		}
	};

	private void sendOrSave(String type) {
		String phoneNo = "";
		String noPhone = phoneNumberText.getText().toString();

		boolean flag = false;

		for (int i = 0; i < noPhone.length(); i++) {
			if (!Character.isDigit(noPhone.charAt(i))) {
				flag = true;
				break;
			}
		}

		if (flag) {

			phoneNo = queryPhoneNumbers(phoneNumberText.getText().toString());
		} else {
			phoneNo = noPhone;
		}

		String message = messageText.getText().toString();
		if (phoneNo.length() > 0 && message.length() > 0) {
			// sendSMS(phoneNo, message);
			
			if (type.equals("1")) {
				if(SettingData.save){
					Date date = new Date();
					String time = date.toLocaleString();
					myDBHelper.insert(phoneNo, message, type, time);
					Toast.makeText(WriteMessageActivity.this, "已保存到发件箱！",
							Toast.LENGTH_LONG).show();
				}
				sendMessageWithResponse(phoneNo, message);
			}else{
				Date date = new Date();
				String time = date.toLocaleString();
				myDBHelper.insert(phoneNo, message, type, time);
				Toast.makeText(WriteMessageActivity.this, "已保存到草稿箱！",
						Toast.LENGTH_LONG).show();
			}

		} else {
			Toast.makeText(WriteMessageActivity.this, "没有这个联系人！",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void onSaveRabbageBox(View view) {
		sendOrSave("2");
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.writemessage);

		Intent intent = getIntent();

		//

		sendSMSButton = (Button) findViewById(R.id.sendSMSButton);
		sendSMSButton.setOnClickListener(onSendButtonClick);
		phoneNumberText = (AutoCompleteTextView) findViewById(R.id.phoneNumEditText);
		messageText = (EditText) findViewById(R.id.messageEditText);
		myDBHelper = new MyDBHelper(this);

		String phonenum = intent.getStringExtra("phonenumber");

		if (phonenum != null) {
			phoneNumberText.setText(phonenum);
		}

		strs = getContactName();


		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, strs);

		phoneNumberText.setAdapter(adapter);
	}

	public String queryPhoneNumbers(String name) {
		String phone = "";
		//
		ContentResolver cr = getContentResolver();
		Cursor pCur = cr.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?",
				new String[] { name }, null);
		if (pCur.moveToNext()) {
			phone = pCur
					.getString(pCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		}
		pCur.close();
		return phone;
	}

	public List<String> getContactName() {
		List<String> list = new ArrayList<String>();
		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);

		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				// String id =
				// cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				list.add(name);
			}
		}

		return list;
	}

	@Override
	public boolean onCreatePanelMenu(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, "返回");
		return super.onCreatePanelMenu(featureId, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
			WriteMessageActivity.this.setResult(RESULT_OK);
			this.finish();
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private void sendMessageWithResponse(String phoneNumber, String message) {
		// 表示发送时候ACTION名称
		String SENT = "SMS_SENT";
		// 表示收到短信时的ACTION名称。
		String DELIVERED = "SMS_DELIVERED";
		// 两次调用getBroadcast方法，得到两个PendingIntent,一个是为发送成功时使用的，一个是为接收成功时使用的。
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);
		//
		registerReceiver(new SendBroadcastReceiver(), new IntentFilter(SENT));
		registerReceiver(new DeliverBroadcastReceiver(), new IntentFilter(
				DELIVERED));
		//
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
	}

	/**
	 * 
	 */
	private class SendBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String text = null;
			switch (getResultCode()) {
			case Activity.RESULT_OK:
				text = "SMS sent";
				break;
			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				text = "Generic failure";
				break;
			case SmsManager.RESULT_ERROR_NO_SERVICE:
				text = "No service";
				break;
			case SmsManager.RESULT_ERROR_NULL_PDU:
				text = "Nnll pdu";
			case SmsManager.RESULT_ERROR_RADIO_OFF:
				text = "Radio off";
				break;
			default:
				break;
			}
			if (text != null) {
				Toast.makeText(context, text, Toast.LENGTH_LONG).show();
			}
		}
	}

	private class DeliverBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String text = null;
			switch (getResultCode()) {
			case Activity.RESULT_OK:
				text = "SMS delivered";
				break;
			case Activity.RESULT_CANCELED:
				text = "SMS not delivered";
				break;
			default:
				break;
			}
			if (text != null) {
				Toast.makeText(context, text, Toast.LENGTH_LONG).show();
			}

		}
	}

}
