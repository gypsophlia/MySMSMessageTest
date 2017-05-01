package com.example.bean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

	public static final String TB_NAME = "receivertable";
	public static final String ID = "_id";  
	public static final String PHONENUMBER = "phonenumber";  
	public static final String MESSAGEBODY = "messagebody"; 
	private final static int DATABASE_VERSION=2;
    private final static String DATABASE_NAME="message_db";
    private final static String MESSAGETYPE="messagetype";
    private final static String TIME = "time";

	
	public MyDBHelper(Context context) 
	{ 
		super(context, DATABASE_NAME,null, DATABASE_VERSION);

    } 

	//@Override
/*	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}*/

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_NAME + 
				" ("  + ID + " INTEGER PRIMARY KEY,"  + PHONENUMBER +
				" VARCHAR,"  + MESSAGEBODY + " VARCHAR," + MESSAGETYPE + " VARCHAR," + TIME + " VARCHAR)"); 
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS "+TB_NAME);  
		onCreate(db);  
	}

    public Cursor select(String type)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(TB_NAME, null, MESSAGETYPE+"='"+type +"'", null, null, null,  " _id desc");
        return cursor;
    }
    
    public Cursor selectGroupByPhone(String type){
    	SQLiteDatabase db=this.getReadableDatabase();
    	Cursor cursor=db.query(TB_NAME, new String[]{PHONENUMBER}, MESSAGETYPE+"='"+type +"'", null, 
              PHONENUMBER, null,  " _id desc");
    	return cursor;
    }
    
    public Cursor selectByPhone(String type,String phone){
    	SQLiteDatabase db=this.getReadableDatabase();
    	Cursor cursor=db.query(TB_NAME, new String[]{ID,PHONENUMBER,MESSAGEBODY,TIME}, MESSAGETYPE+"='"+type +"' AND " + 
    			PHONENUMBER + "='" + phone +"'", null,  null, null,  " _id desc");
    	return cursor;    	
    }
    
    public long insert(String phonenumber,String messagebody,String type,String time)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues(); 
        cv.put(PHONENUMBER, phonenumber);
        cv.put(MESSAGEBODY, messagebody);
        cv.put(MESSAGETYPE, type);
        cv.put(TIME, time);
        long row=db.insert(TB_NAME, null, cv);
        return row;
    }
    
    public void delete(int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String where=ID+"=?";
        String[] whereValue={Integer.toString(id)};
        db.delete(TB_NAME, where, whereValue);
    }
    
}
