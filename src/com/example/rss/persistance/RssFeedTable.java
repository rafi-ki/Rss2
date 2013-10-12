package com.example.rss.persistance;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RssFeedTable {
	
	// table name
	public static final String TABLE_RSSFEED = "rssfeed";
	
	// table columns
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_LINK = "link";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_DATE = "date";
	
	public static final String [] ALL_COLUMNS = {
		COLUMN_ID, 
		COLUMN_TITLE,
		COLUMN_LINK,
		COLUMN_DESCRIPTION,
		COLUMN_DATE
	};
	

	// create script for table
	private static final String CREATE_RSSFEED = "CREATE TABLE "
			+ TABLE_RSSFEED + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ COLUMN_TITLE + " TEXT, " 
			+ COLUMN_LINK + " TEXT UNIQUE, "
			+ COLUMN_DESCRIPTION + " TEXT, " 
			+ COLUMN_DATE + " TEXT);";
	
	public static void onCreate(SQLiteDatabase database)
	{
		database.execSQL(CREATE_RSSFEED);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
		Log.w(RssFeedTable.class.getName(), "Upgrading database from version "
		        + oldVersion + " to " + newVersion
		        + ", which will destroy all old data");
		    database.execSQL("DROP TABLE IF EXISTS " + TABLE_RSSFEED);
		    onCreate(database);
	}
}
