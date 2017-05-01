package com.example;

import java.util.ArrayList;
import java.util.Date;

import com.example.adapter.MenuAdapter;
import com.example.adapter.MessageAdapter;
import com.example.bean.MessageBean;
import com.example.bean.MyDBHelper;
import com.example.bean.SettingData;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class QQMessageActivity extends Activity{
	private ListView listview;
	private ArrayList<MessageBean> list;
	private MessageAdapter adapter;
	private String phoneNumber;
	private EditText messageStrEdit;
	private String messagecontext;
	private String messagetime;
	private MyDBHelper myDBHelper;
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 1:
				// 从收到的消息对象中把数据提取出来。
				MessageBean content = (MessageBean)msg.obj;
				// 把收到的消息添加到适配器中。
				adapter.add(content);
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qqmessage_main);
		
		listview = (ListView)findViewById(R.id.qqlistview);
		list = new ArrayList<MessageBean>();
		messageStrEdit = (EditText)findViewById(R.id.qqmessageecontext);
		
		Intent intent = getIntent();
		phoneNumber = intent.getStringExtra("pnumber");
		messagecontext = intent.getStringExtra("messagecontext");
		messagetime = intent.getStringExtra("messagetime");
		
		if(messagecontext!=null && messagetime != null){
			MessageBean m = new MessageBean( phoneNumber, messagecontext, messagetime);
			list.add(m);
		}
		
		myDBHelper = new MyDBHelper(this);
		
		adapter = new MessageAdapter(this,R.layout.message_item,list);		
		listview.setAdapter(adapter);
		
		IntentFilter filter = new IntentFilter("broadcast");
		 //注册广播的接收器，在注册时把本对象的Handler传进去。
		registerReceiver(new SMSReceiver(handler), filter);
	}
	

	public void onSendButtonClick(View view){
		String message = messageStrEdit.getText().toString();
		sendMessageWithResponse(phoneNumber,  message);		
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
		
		Date date = new Date();
		String time = date.toLocaleString();
		
		if(SettingData.save){
			myDBHelper.insert(phoneNumber, message, "1", time);
		}
		
		MessageBean m = new MessageBean( phoneNumber, message, time);
		
		adapter.add(m);
				
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
