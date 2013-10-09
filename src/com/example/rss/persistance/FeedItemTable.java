package com.example.rss.persistance;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FeedItemTable {
	
	//Table name
	public static final String TABLE_FEED_ITEM = "feed_item";
	
	//Table columns
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_LINK = "link";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_RSSFEED_ID = "rssfeed_id";
	public static final String COLUMN_FEED_ID = "feedId";
	public static final String COLUMN_AUTHOR = "author";
	public static final String COLUMN_READ_STATE = "read_state";
	public static final String COLUMN_STARRED_STATE = "starred_state";
	
	// create script of the table
	private static final String CREATE_FEEDITEM = "CREATE TABLE"
			+ TABLE_FEED_ITEM + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ COLUMN_FEED_ID + " TEXT, "
			+ COLUMN_TITLE + " TEXT, "
			+ COLUMN_LINK + " TEXT, "
			+ COLUMN_DESCRIPTION + " TEXT, "
			+ COLUMN_RSSFEED_ID + " INTEGER, "
			+ COLUMN_AUTHOR + " TEXT, "
			+ COLUMN_READ_STATE + " INTEGER, "
			+ COLUMN_STARRED_STATE + " INTEGER)";
	
	public static void onCreate(SQLiteDatabase database)
	{
		database.execSQL(CREATE_FEEDITEM);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
		Log.w(FeedItemTable.class.getName(), "Upgrading database from version "
		        + oldVersion + " to " + newVersion
		        + ", which will destroy all old data");
		    database.execSQL("DROP TABLE IF EXISTS " + TABLE_FEED_ITEM);
		    onCreate(database);
	}
}
