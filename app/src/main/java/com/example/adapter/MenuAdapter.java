package com.example.adapter;

import java.util.ArrayList;

import com.example.R;

import com.example.bean.MenuInformation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends ArrayAdapter<MenuInformation> {
	@Override
	public void add(MenuInformation object) {
		// TODO Auto-generated method stub
		super.add(object);
	}

	private ArrayList<MenuInformation> list;
	private Context context;

	public MenuAdapter(Context context, int textViewResourceId,
			ArrayList<MenuInformation> list) {
		super(context, textViewResourceId, list);
		this.list = list;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_item, null);
		}
		//
		MenuInformation msg = list.get(position);
		ImageView imageView = (ImageView) v.findViewById(R.id.icon);
		TextView tempView = (TextView) v.findViewById(R.id.temptext);
		TextView notenumView = (TextView) v.findViewById(R.id.notenumtext);
		// TextView bottomView = (TextView)v.findViewById(R.id.bottomtext);

		imageView.setImageResource(msg.getImageId());

		tempView.setText(msg.getName());
		if (!msg.getNoteNum().equals("")) {
			notenumView.setText("有" + msg.getNoteNum() + "条");
		}else{
			notenumView.setText(msg.getNoteNum());
		}
		return v;
	}

}
