package com.example.rss.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedDatabaseHelper extends SQLiteOpenHelper{
	
	private static final String LOG = "feeds_database_helper";
	
	private static final String DATABASE_NAME = "feeds";
	private static final int DATABASE_VERSION = 1;
	
	//Table Names
	private static final String TABLE_RSSFEED = "rssfeed";
	private static final String TABLE_FEEDITEM = "feeditem";
	
	//common column names
	private static final String KEY_ID = "id";
	private static final String COL_TITLE = "title";
	private static final String COL_LINK = "link";
	private static final String COL_DESCRIPTION = "description";
	
	//RssFeed Table
	private static final String COL_DATE = "date";
	
	//FeedItem Table
	private static final String COL_RSSFEED_ID = "rssfeed_id";
	private static final String COL_FEED_ID = "feedId";
	private static final String COL_AUTHOR = "author";
	private static final String COL_READ_STATE = "read_state";
	private static final String COL_STARRED_STATE = "starred_state";
	
	
	//Creating Statements of the Tables
	private static final String CREATE_RSSFEED = "CREATE TABLE"
			+ TABLE_RSSFEED + "(" + KEY_ID + " INTEGER PRIMARY KEY," 
			+ COL_TITLE + " TEXT, " 
			+ COL_LINK + " TEXT, "
			+ COL_DESCRIPTION + " TEXT, " 
			+ COL_DATE + " TEXT)";
	
	private static final String CREATE_FEEDITEM = "CREATE TABLE"
			+ TABLE_FEEDITEM + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ COL_FEED_ID + " TEXT, "
			+ COL_TITLE + " TEXT, "
			+ COL_LINK + " TEXT, "
			+ COL_DESCRIPTION + " TEXT, "
			+ COL_RSSFEED_ID + " INTEGER, "
			+ COL_AUTHOR + " TEXT, "
			+ COL_READ_STATE + " INTEGER, "
			+ COL_STARRED_STATE + " INTEGER)";
	
	public FeedDatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CREATE_RSSFEED);
		db.execSQL(CREATE_FEEDITEM);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDITEM);
		db.execSQL("DROP TABLE IF EXIStS " + TABLE_RSSFEED);
		
		onCreate(db);
	}
	
	public void closeDB()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}
}
