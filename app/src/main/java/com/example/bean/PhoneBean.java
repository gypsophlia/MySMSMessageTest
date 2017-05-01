package com.example.bean;

import com.example.R;

public class PhoneBean {
	private int imageId;
	private String phonenumber;
	private int notenum;
	
	public int getNotenum() {
		return notenum;
	}

	public void setNotenum(int notenum) {
		this.notenum = notenum;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}



	public PhoneBean(){
		phonenumber = "0";
	}
	
	public PhoneBean(String phonenumber,int notenum){
		
		this.imageId =R.drawable.cl;
		this.phonenumber = phonenumber;
		this.notenum = notenum;
		
	}
	
	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
}
