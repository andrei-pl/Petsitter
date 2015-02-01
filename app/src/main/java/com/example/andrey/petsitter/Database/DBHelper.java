package com.example.andrey.petsitter.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	protected static final String DB_NAME = "Petsitter";
	protected static final int DB_VERSION = 1;
	protected SQLiteDatabase userDb;
	
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table Classifieds (_id nvarchar2 not null primary key, name nvarchar2 not null, address nvarchar2, title nvarchar2, description nvarchar2, phone nvarchar2, animal nvarchar2, picture blob, latitude number, longitude number);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

	public SQLiteDatabase open(){
		return getWritableDatabase();
	}
	
	public void close (){
		close();
	}
}
