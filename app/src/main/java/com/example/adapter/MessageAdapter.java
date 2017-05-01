package com.example.adapter;

import java.util.ArrayList;

import com.example.R;
import com.example.bean.MessageBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends ArrayAdapter<MessageBean>{
	@Override
	public void add(MessageBean object) {
		// TODO Auto-generated method stub
		super.add(object);
	}

	private ArrayList<MessageBean> list;
	private Context context;
	
	public MessageAdapter(Context context, int textViewResourceId,
			ArrayList<MessageBean> list)
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
			v = inflater.inflate(R.layout.message_item, null);
		}
		//
		MessageBean msg = list.get(position);
		//CheckBox checkbox = (CheckBox)v.findViewById(R.id.checkbox);
		
		/*checkbox.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				msg.setChecked(checkbox.isChecked());
			}
		});*/
		ImageView imageview = (ImageView)v.findViewById(R.id.messageimageview);
		TextView phoneView = (TextView)v.findViewById(R.id.phonenumber1);
		TextView messageView = (TextView)v.findViewById(R.id.messagebody);
		//TextView bottomView = (TextView)v.findViewById(R.id.bottomtext)
		imageview.setImageResource(msg.getImageId());
		phoneView.setText(msg.getPhonenumber());
		messageView.setText(msg.getMessagebody() + "\n     时间:" + msg.getTime());
		return v;
	}
	
}
