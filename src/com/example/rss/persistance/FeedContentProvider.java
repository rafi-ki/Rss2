package com.example.rss.persistance;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String [] validColumns = null;
		String tableName = "";
		
		// which table will be used ...
		if (uriMatcher.match(uri) == RSSFEEDS)
		{
			validColumns = RssFeedTable.ALL_COLUMNS;
			tableName = RssFeedTable.TABLE_RSSFEED;
		}
		else if (uriMatcher.match(uri) == FEED_ITEMS)
		{
			validColumns = FeedItemTable.ALL_COLUMNS;
			tableName = FeedItemTable.TABLE_FEED_ITEM;
		}
		
		checkColumns(projection, validColumns);
		
		return db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		String tableName = "";
		String path = "";
		if (uriMatcher.match(uri) == RSSFEEDS)
		{
			tableName = RssFeedTable.TABLE_RSSFEED;
			path = BASE_PATH_RSS;
		}
		else if (uriMatcher.match(uri) == FEED_ITEMS)
		{
			tableName = FeedItemTable.TABLE_FEED_ITEM;
			path = BASE_PATH_FEED_ITEM;
		}
		
		long id = db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		getContext().getContentResolver().notifyChange(uri, null);
		
		db.close();
		return Uri.parse(path + "/" + id);
	}
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null; // is not needed yet
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * checks if the columns are valid
	 * @param columns the columns to check
	 * @param available the columns available
	 */
	private void checkColumns(String [] columns, String [] available)
	{
		if (columns == null && available == null)
			return;
		
		HashSet<String> requstedColumns = new HashSet<String>(Arrays.asList(columns));
		HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
		if (!availableColumns.containsAll(requstedColumns))
			throw new IllegalArgumentException("Unknown columns in projection");
	}

}
