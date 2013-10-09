package com.example.rss.persistance;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class FeedContentProvider extends ContentProvider{

	private SQLiteOpenHelper dbHelper;
	
	private static final String AUTHORITY = "com.example.rss.persistance";
	
	//TODO check what this is all about
	private static final int RSSFEEDS = 10;
	private static final int RSSFEED_ID = 20;
	private static final int FEED_ITEMS = 30;
	private static final int FEED_ITEM_ID = 40;
	
	// define base paths
	private static final String BASE_PATH_RSS = "rssfeeds";
	private static final String BASE_PATH_FEED_ITEM = "feeditems";
	
	// define content uris 
	public static final Uri CONTENT_URI_RSS = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_RSS);
	public static final Uri CONTENT_URI_FEED_ITEM = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_FEED_ITEM);
	
	private static final UriMatcher uriMatcher;
	static
	{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, BASE_PATH_RSS, RSSFEEDS);
		uriMatcher.addURI(AUTHORITY, BASE_PATH_RSS + "/#", RSSFEED_ID);
		uriMatcher.addURI(AUTHORITY, BASE_PATH_FEED_ITEM, FEED_ITEMS);
		uriMatcher.addURI(AUTHORITY, BASE_PATH_FEED_ITEM + "/#", FEED_ITEM_ID);
	}
	
	@Override
	public boolean onCreate() {
		//create instance of FeedDatabaseHelper
		dbHelper = new FeedDatabaseHelper(this.getContext());
		
		return dbHelper != null;
	}
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

}
