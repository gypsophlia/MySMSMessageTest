package com.example;

import java.util.ArrayList;

import com.example.adapter.MenuAdapter;
import com.example.bean.MenuInformation;
import com.example.bean.MyDBHelper;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.*;

public class MainActivity extends Activity {
	private ListView listview;
	private ArrayList<MenuInformation> list;
	private MenuAdapter adapter;
	private MyDBHelper myDBHelper;
	
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
		        adapter = new MenuAdapter(MainActivity.this, R.layout.list_item, list);
		        listview.setAdapter(adapter);
				break;
			default:
				break;
			}
		}
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listview = (ListView)findViewById(R.id.listview);
        myDBHelper = new MyDBHelper(this);
                     
        list = getList();
        
        adapter = new MenuAdapter(this, R.layout.list_item, list);
        listview.setAdapter(adapter);
        listview.setBackgroundDrawable(null);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent,View view, int position, long id)
			{
				Intent intent;
				switch(position){
				case 0:
					intent = new Intent(MainActivity.this,WriteMessageActivity.class);
					startActivityForResult(intent,1);
			    	break;
				case 1:
					intent = new Intent(MainActivity.this,MessagePhoneActivity.class);
					intent.putExtra("type", "0");
					startActivityForResult(intent,1);
					break;
				case 2:
					intent = new Intent(MainActivity.this,MessagePhoneActivity.class);
					intent.putExtra("type", "1");
			    	startActivityForResult(intent,1);
					break;
				case 3:
					intent = new Intent(MainActivity.this,MessagePhoneActivity.class);
					intent.putExtra("type", "2");
					startActivityForResult(intent,1);
			    	break;
				case 4:
					intent = new Intent(MainActivity.this,MessageSettingActivity.class);
			    	startActivity(intent);
			    	break;
					
				}

			}
		});
        
        
		IntentFilter filter = new IntentFilter("broadcast");
		 //注册广播的接收器，在注册时把本对象的Handler传进去。
		registerReceiver(new SMSReceiver(handler), filter);
		

    }
    
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub\
		list.clear();
		list = getList();
        adapter = new MenuAdapter(MainActivity.this, R.layout.list_item, list);
        listview.setAdapter(adapter);
		super.onActivityResult(requestCode, resultCode, data);

	}


	private ArrayList<MenuInformation> getList(){
    	ArrayList<MenuInformation> l = new ArrayList<MenuInformation>();
    	

        MenuInformation writeM = new MenuInformation("撰写短信", R.drawable.write, "");
        l.add(writeM);
        
        Cursor cusor1 = myDBHelper.select("0");
        int num1 = cusor1.getCount();
        MenuInformation reciveM = new MenuInformation("收到短信", R.drawable.recive, num1+"");
        l.add(reciveM);
        
        Cursor cusor2 = myDBHelper.select("1");
        int num2 = cusor2.getCount();
        MenuInformation sendedM = new MenuInformation("已发送短信", R.drawable.sended, num2+"");
        l.add(sendedM);
        
        Cursor cusor3 = myDBHelper.select("2");
        int num3 = cusor3.getCount();
        MenuInformation garbageM = new MenuInformation("草稿箱", R.drawable.ra, num3+"");
        l.add(garbageM);
        
        MenuInformation settingM = new MenuInformation("短信设置",R.drawable.setting,"");
        l.add(settingM);
        
    	
    	return l;
    }
    
    
	@Override
	public boolean onCreatePanelMenu(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, "退出");
		return super.onCreatePanelMenu(featureId, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
		    this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
    
  /*  public void onWriteButtonClick(View view){
    	Intent intent = new Intent(this,WriteMessageActivity.class);
    	startActivity(intent);
    }*/
    
}