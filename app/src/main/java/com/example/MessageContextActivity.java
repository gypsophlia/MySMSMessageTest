package com.example;

import java.util.ArrayList;

import com.example.adapter.MessageAdapter;
import com.example.bean.MessageBean;
import com.example.bean.MyDBHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
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

public class MessageContextActivity extends Activity{

	private ListView listview;
	private MyDBHelper myDBHelper;
	private ArrayList<MessageBean> list;
	private MessageAdapter adapter;
	private String phoneNumber;
	private String type;
	private TextView pnTextView;
	//private int position;
	
	
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
		        adapter = new MessageAdapter(MessageContextActivity.this,R.layout.message_item,list);
		        listview.setAdapter(adapter);
				break;
			default:
				break;
			}
		}
	};
	
	/*public MessageContextActivity(){
		
	}
	
	public MessageContextActivity(Handler handler){
		
		this.handler = handler;
		
	}*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_main);
		
		//position = 0;
		listview = (ListView)findViewById(R.id.listview3);
		pnTextView = (TextView)findViewById(R.id.pntextview);
		
		myDBHelper = new MyDBHelper(this);
		
		Intent intent = getIntent();
		phoneNumber = intent.getStringExtra("phone");
		type = intent.getStringExtra("type");
		
		
		list = getList();
		
		if(type.equals("0")){
			setTitle("收件箱");
			pnTextView.setText("From:"+phoneNumber);
		}else if(type.equals("1")){
			setTitle("发件箱");
			pnTextView.setText("To:"+phoneNumber);
		}else{
			setTitle("草稿箱");
			pnTextView.setText(phoneNumber + ":");
		}
		
		
		
		adapter = new MessageAdapter(this,R.layout.message_item,list);
		
		listview.setAdapter(adapter);
		
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent,View view, int position, long id)
			{
		    	AlertDialog.Builder builder = new AlertDialog.Builder(MessageContextActivity.this);
		    	builder.setTitle("");
		    	final String[] items = {"回复","删除","呼叫","取消"};
		    	final int p = position;
		    	builder.setItems(items,new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					    switch(which){
					    case 0:
					    	Intent intent = new Intent(MessageContextActivity.this,WriteMessageActivity.class);
					    	intent.putExtra("phonenumber", list.get(p).getPhonenumber());
					    	startActivity(intent);
					    	break;
					    case 1:
							myDBHelper.delete(list.get(p).getId());
							list.remove(p);	
							adapter = new MessageAdapter(MessageContextActivity.this,R.layout.message_item,list);
							listview.setAdapter(adapter);
							break;
					    case 2:
							intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:" + list.get(p).getPhonenumber()));
							startActivity(intent);
							break;
					    }
					}
				});
		    	builder.show();
			}
		});
        
          
        
		IntentFilter filter = new IntentFilter("broadcast");
		 //注册广播的接收器，在注册时把本对象的Handler传进去。
		registerReceiver(new SMSReceiver(handler), filter);
		
		
		/*Message msg = Message.obtain(handler, 1, "OK");
		// 把消息放到消息队列的结尾
		handler.sendMessage(msg);*/
		
	}
	
	private ArrayList<MessageBean> getList(){
		ArrayList<MessageBean> l = new ArrayList<MessageBean>();
		Cursor cursor = myDBHelper.selectByPhone(type,phoneNumber);
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()){
			MessageBean messageBean = new MessageBean(cursor.getInt(0),cursor.getString(1),cursor.getString(2),
					cursor.getString(3));
			l.add(messageBean);
			cursor.moveToNext();
		}
		
		return l;
	}
	
	public boolean onCreatePanelMenu(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		//menu.add(0, 1, 1, "删除所有短信");
		menu.add(0,1,1,"返回");
		return super.onCreatePanelMenu(featureId, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
			MessageContextActivity.this.setResult(RESULT_OK);
		    this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
