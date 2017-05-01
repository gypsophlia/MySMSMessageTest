package com.example.adapter;

import java.util.ArrayList;
import com.example.R;
import com.example.bean.PhoneBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PhoneBeanAdapter extends ArrayAdapter<PhoneBean>{
	private ArrayList<PhoneBean> list;
	private Context context;
	
	public PhoneBeanAdapter(Context context, int textViewResourceId,
			ArrayList<PhoneBean> list)
	{
		super(context, textViewResourceId,list);
		this.list = list;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
		if(v == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.receiverphone_item, null);
		}
		//
		PhoneBean msg = list.get(position);
		
		ImageView imageview = (ImageView)v.findViewById(R.id.phoneIcon);
		TextView phoneNumberView = (TextView)v.findViewById(R.id.phonenumbertext);
		//TextView bottomView = (TextView)v.findViewById(R.id.bottomtext);
		imageview.setImageResource(msg.getImageId());
		TextView noteNumView = (TextView)v.findViewById(R.id.notenumtext);
		
		noteNumView.setText("有" + msg.getNotenum()+"条");
		phoneNumberView.setText(msg.getPhonenumber());
		
		return v;
	}

	@Override
	public void add(PhoneBean object) {
		// TODO Auto-generated method stub
		super.add(object);
	}
}