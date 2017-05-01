package com.example;

import java.util.ArrayList;

import com.example.R;
import com.example.adapter.MessageAdapter;
import com.example.adapter.PhoneBeanAdapter;
import com.example.bean.MyDBHelper;
import com.example.bean.PhoneBean;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessagePhoneActivity extends Activity {

	private ListView listview;
	private MyDBHelper myDBHelper;
	private PhoneBeanAdapter adapter;
	private ArrayList<PhoneBean> list;
	private String type;
	//private TextView pnTextView;

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 1:
				list.clear();
				list = getList();
				adapter = new PhoneBeanAdapter(MessagePhoneActivity.this, R.layout.receiverphone_item, list);
		        listview.setAdapter(adapter);
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

		setContentView(R.layout.receiverphone_main);

		Intent intent = getIntent();
		
		type = intent.getStringExtra("type");
		
		//pnTextView = (TextView)findViewById(R.id.pntextview1);
		
		if(type == null)
			return;
		
		if(type.equals("0")){
			setTitle("收件箱");
		}else if(type.equals("1")){
			setTitle("发件箱");
		}else{
			setTitle("草稿箱");
		}
		
		listview = (ListView) findViewById(R.id.listview2);
		
		
		myDBHelper = new MyDBHelper(this);
		
		list = getList();


		adapter = new PhoneBeanAdapter(this, R.layout.receiverphone_item, list);
		listview.setAdapter(adapter);
		
		final String t = type;
		
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent,View view, int position, long id)
			{
					Intent intent = new Intent(MessagePhoneActivity.this,MessageContextActivity.class);
					intent.putExtra("phone", list.get(position).getPhonenumber());
					intent.putExtra("type", t);
					startActivityForResult(intent,1);
			    	//RecevierPhoneActivity.this.finish();
			}
		});
        
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Intent intent = new Intent(MessagePhoneActivity.this,QQMessageActivity.class);
				intent.putExtra("pnumber", list.get(arg2).getPhonenumber());
				startActivityForResult(intent,1);
				// TODO Auto-generated method stub
				return false;
			}
        	
		});
        
        
        listview.setOnCreateContextMenuListener(new AdapterView.OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				
			}
		});
        
		IntentFilter filter = new IntentFilter("broadcast");
		 //注册广播的接收器，在注册时把本对象的Handler传进去。
		registerReceiver(new SMSReceiver(handler), filter);

	}
	
	
	private ArrayList<PhoneBean> getList(){
		ArrayList<PhoneBean> l = new ArrayList<PhoneBean>();
		Cursor cursor = myDBHelper.selectGroupByPhone(type);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Cursor cursor1 = myDBHelper.selectByPhone(type, cursor.getString(0));
			int notenum = cursor1.getCount();
			PhoneBean phonebean = new PhoneBean(cursor.getString(0),notenum);
			l.add(phonebean);
			cursor.moveToNext();
		}		
		return l;
	}
	
	public boolean onCreatePanelMenu(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0,1,1,"返回");
		return super.onCreatePanelMenu(featureId, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
			/*Intent intent = new Intent(MessageMainActivity.this,
					MainActivity.class);

			startActivity(intent);*/
			MessagePhoneActivity.this.setResult(RESULT_OK);
		    this.finish();
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		list.clear();
		list = getList();
		adapter = new PhoneBeanAdapter(MessagePhoneActivity.this, R.layout.receiverphone_item, list);
        listview.setAdapter(adapter);
		super.onActivityResult(requestCode, resultCode, data);

		
	}

}
