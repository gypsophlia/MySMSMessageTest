package com.example;

import com.example.bean.SettingData;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class MessageSettingActivity extends Activity{

	private RadioButton sradioButton;
	private RadioButton nsradioButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_setting);
		
		sradioButton = (RadioButton)findViewById(R.id.sButton1);
		nsradioButton = (RadioButton)findViewById(R.id.nsButton1);
		
		if(SettingData.save){
			sradioButton.setChecked(true);
		}else{
			nsradioButton.setChecked(true);
		}
		
	}
	
	public void onSaveClick(View view){
		if(sradioButton.isChecked()){
			SettingData.save = true;
		}
	}
	
	public void onNsaveClick(View view){
		if(nsradioButton.isChecked()){
			SettingData.save = false;
		}
	}

}
