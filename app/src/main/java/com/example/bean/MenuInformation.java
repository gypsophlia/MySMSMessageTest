package com.example.bean;



public class MenuInformation {
	private String name;
	private int imageId;
	private String noteNum;
	public String getNoteNum() {
		return noteNum;
	}

	public void setNoteNum(String noteNum) {
		this.noteNum = noteNum;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImage(int imageId) {
		this.imageId = imageId;
	}

	public MenuInformation(){
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}



	public MenuInformation(String name, int imageId, String noteNum) {
		this.name = name;
		this.imageId = imageId;
		this.noteNum = noteNum;
	}
	
}
