package com.example.rss.persistance;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

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
		int uriType = uriMatcher.match(uri);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		String tableName = "";
		Uri contentUri = null;
		switch(uriType)
		{
		case RSSFEEDS:
			tableName = RssFeedTable.TABLE_RSSFEED;
			contentUri = CONTENT_URI_RSS;
			break;
		case FEED_ITEMS:
			tableName = FeedItemTable.TABLE_FEED_ITEM;
			contentUri = CONTENT_URI_FEED_ITEM;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		// insert add get id
		long id = db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		
		Uri _uri = ContentUris.withAppendedId(contentUri, id);
		
		// notify observer
		getContext().getContentResolver().notifyChange(_uri, null);
		
		db.close();
		return _uri;
	}
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		int uriType = uriMatcher.match(uri);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String tableName = "";
		String id_column = "";
		Uri contentUri = null;
		int rowsDeleted = 0;
		
		// selecting tablename and column id
		switch(uriType)
		{
		case RSSFEEDS:
		case RSSFEED_ID:
			tableName = RssFeedTable.TABLE_RSSFEED;
			id_column = RssFeedTable.COLUMN_ID;
			contentUri = CONTENT_URI_RSS;
			break;
		case FEED_ITEMS:
		case FEED_ITEM_ID:
			tableName = FeedItemTable.TABLE_FEED_ITEM;
			id_column = FeedItemTable.COLUMN_ID;
			contentUri = CONTENT_URI_FEED_ITEM;
			break;
		default:
			 throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		
		// check to delete per id or with where clause
		if (uriType == RSSFEEDS || uriType == FEED_ITEMS)
			rowsDeleted = db.delete(tableName, where, whereArgs);
		else
		{
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(where))
				rowsDeleted = db.delete(tableName, id_column + "=" + id, null);
			else
				rowsDeleted = db.delete(tableName,  id_column + "=" + id + " AND " + where, whereArgs);
		}
		
		getContext().getContentResolver().notifyChange(contentUri, null);
		db.close();
		return rowsDeleted;
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
