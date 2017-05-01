package com.example;

import java.util.Date;

import com.example.bean.MessageBean;
import com.example.bean.MyDBHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

	private MyDBHelper myDBHelper;
	private Handler handler;
	// private Handler handler;
	//public static final int MSG_ID = 10;

	public SMSReceiver(){
		
	}
	
	public SMSReceiver(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		
		if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
			Bundle bundle = intent.getExtras();
			SmsMessage[] msgs = null;
			String str = "ERROR";
			//
			Object[] pdus = (Object[]) bundle.get("pdus");
			myDBHelper = new MyDBHelper(context);

			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i != msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				try {
					str = "Message From:"
							+ msgs[i].getDisplayOriginatingAddress();
					str += ":";
					str += msgs[i].getMessageBody();
					str += "\n";
					String phonenumber = msgs[i].getDisplayOriginatingAddress();
					String messagebody = msgs[i].getMessageBody();
					Date date = new Date();
					String time = date.toLocaleString();
					myDBHelper.insert(phonenumber, messagebody, "0", time);
					Toast.makeText(context, str, Toast.LENGTH_LONG).show();
					
					MessageBean m = new MessageBean(phonenumber, messagebody,time);
					
					Intent newIntent = new Intent("broadcast");
					
					newIntent.putExtra("m", m);
					//新短信在数据库中的_id
					// 利用广播的方式把intent发送出去。
					context.sendBroadcast(newIntent);
				} catch (Exception e) {
					Toast.makeText(context, e.getStackTrace().toString(),
							Toast.LENGTH_LONG).show();
				}
			}

			/*
			 * Message msg = Message.obtain(handler, MSG_ID, len); //
			 * 把消息放到消息队列的结尾。 handler.sendMessage(msg);
			 */
			myDBHelper.close();
			
			
			//新建一个intent

		} else {
			MessageBean m = (MessageBean) intent.getSerializableExtra("m");
			
			//intent.getIntExtra("num",-1);
			
			Message msg = Message.obtain(handler, 1, m);
			// 把消息放到消息队列的结尾
			handler.sendMessage(msg);
		}

	}

}
