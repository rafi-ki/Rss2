package com.example.rss.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedDatabaseHelper extends SQLiteOpenHelper{
	
	//Database Information
	private static final String DATABASE_NAME = "feeds";
	private static final int DATABASE_VERSION = 1;
	
	public FeedDatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		RssFeedTable.onCreate(db);
		FeedItemTable.onCreate(db);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		FeedItemTable.onUpgrade(db, oldVersion, newVersion);
		RssFeedTable.onUpgrade(db, oldVersion, newVersion);
	}
	
	public void closeDB()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}
}
