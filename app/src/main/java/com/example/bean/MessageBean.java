package com.example.bean;

import java.io.Serializable;

import com.example.R;


public class MessageBean implements Serializable{
	private int imageId;
	private String messagebody;
	private String phonenumber;
	private String time;
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public MessageBean(){
		
	}
	
	public MessageBean(int id,String phonenumber,String messagebody,String time){
		this.id = id;
		this.imageId= R.drawable.cl;
		this.messagebody = messagebody;
		this.phonenumber = phonenumber;
		this.time = time;
	}
	
	public MessageBean(String phonenumber,String messagebody,String time){
		this.id = -1;
		this.imageId= R.drawable.cl;
		this.messagebody = messagebody;
		this.phonenumber = phonenumber;
		this.time = time;
	}
	
	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getMessagebody() {
		return messagebody;
	}

	public void setMessagebody(String messagebody) {
		this.messagebody = messagebody;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
}
