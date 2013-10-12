package com.example.rss.persistance;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.rss.model.FeedItem;
import com.example.rss.model.RssFeed;

public class FeedDatabase {
	
	public static int deleteRssFeedById(Context c, long id)
	{
		ContentResolver cr = c.getContentResolver();
		return cr.delete(FeedContentProvider.CONTENT_URI_RSS, RssFeedTable.COLUMN_ID + "=" + id, null);
	}
	
	public static int deleteFeedItemById(Context c, long id)
	{
		ContentResolver cr = c.getContentResolver();
		return cr.delete(FeedContentProvider.CONTENT_URI_FEED_ITEM, FeedItemTable.COLUMN_ID + "=" + id, null);
	}
	
	public static int updateFeedItem(Context c, long id, FeedItem item)
	{
		ContentResolver cr = c.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(FeedItemTable.COLUMN_ID, item.getId());
		values.put(FeedItemTable.COLUMN_RSSFEED_ID, item.getRssFeedId());
		values.put(FeedItemTable.COLUMN_TITLE, item.getTitle());
		values.put(FeedItemTable.COLUMN_AUTHOR, item.getAuthor());
		values.put(FeedItemTable.COLUMN_DESCRIPTION, item.getDescription());
		values.put(FeedItemTable.COLUMN_LINK, item.getLink());
		values.put(FeedItemTable.COLUMN_READ_STATE, toInteger(item.isRead())); // no booleans in sqlite
		values.put(FeedItemTable.COLUMN_STARRED_STATE, toInteger(item.isStarred())); // no booleans in sqlite
		
		return cr.update(FeedContentProvider.CONTENT_URI_FEED_ITEM, values, FeedItemTable.COLUMN_ID + "=" + id, null);
	}
	
	public static int updateRssFeed(Context c, long id, RssFeed feed)
	{
		ContentResolver cr = c.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(RssFeedTable.COLUMN_TITLE, feed.getTitle());
		values.put(RssFeedTable.COLUMN_LINK, feed.getLink());
		values.put(RssFeedTable.COLUMN_DESCRIPTION, feed.getDescription());
		values.put(RssFeedTable.COLUMN_FEED_URL, feed.getFeedUrl());
		values.put(RssFeedTable.COLUMN_DATE, feed.getDate());
		
		return cr.update(FeedContentProvider.CONTENT_URI_RSS, values, RssFeedTable.COLUMN_ID + "=" + id, null);
	}
	
	public static Uri insertRssFeed(Context c, RssFeed feed)
	{
		ContentResolver cr = c.getContentResolver();
		ContentValues values = new ContentValues();
		
		values.put(RssFeedTable.COLUMN_TITLE, feed.getTitle());
		values.put(RssFeedTable.COLUMN_DESCRIPTION, feed.getDescription());
		values.put(RssFeedTable.COLUMN_LINK, feed.getLink());
		values.put(RssFeedTable.COLUMN_DATE, feed.getDate());
		values.put(RssFeedTable.COLUMN_FEED_URL, feed.getFeedUrl());
		
		return cr.insert(FeedContentProvider.CONTENT_URI_RSS, values);
	}
	
	public static Uri insertFeedItem(Context c, FeedItem item)
	{
		ContentResolver cr = c.getContentResolver();
		ContentValues values = new ContentValues();
		
		values.put(FeedItemTable.COLUMN_RSSFEED_ID, item.getRssFeedId());
		values.put(FeedItemTable.COLUMN_TITLE, item.getTitle());
		values.put(FeedItemTable.COLUMN_AUTHOR, item.getAuthor());
		values.put(FeedItemTable.COLUMN_DESCRIPTION, item.getDescription());
		values.put(FeedItemTable.COLUMN_LINK, item.getLink());
		values.put(FeedItemTable.COLUMN_READ_STATE, toInteger(item.isRead()));
		values.put(FeedItemTable.COLUMN_STARRED_STATE, toInteger(item.isStarred()));
		
		return cr.insert(FeedContentProvider.CONTENT_URI_FEED_ITEM, values);
	}
	
	public static RssFeed getRssFeedById(Context c, long id)
	{
		ContentResolver cr = c.getContentResolver();
		Cursor cursor = cr.query(FeedContentProvider.CONTENT_URI_RSS, RssFeedTable.ALL_COLUMNS, RssFeedTable.COLUMN_ID + "=" + id, null, null);
		cursor.moveToFirst();
		return cursorToRssFeed(cursor);
	}
	
	public static RssFeed getRssFeedByUrl(Context c, String url)
	{
		ContentResolver cr = c.getContentResolver();
		Cursor cursor = cr.query(FeedContentProvider.CONTENT_URI_RSS, RssFeedTable.ALL_COLUMNS, RssFeedTable.COLUMN_FEED_URL + "=" + url, null, null);
		cursor.moveToFirst();
		return cursorToRssFeed(cursor);
	}
	
	public static List<RssFeed> getRssFeeds(Context c)
	{
		ContentResolver cr = c.getContentResolver();
		Cursor cursor = cr.query(FeedContentProvider.CONTENT_URI_RSS, RssFeedTable.ALL_COLUMNS, null, null, null);
		List<RssFeed> feedList = new ArrayList<RssFeed>();
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			feedList.add(cursorToRssFeed(cursor));
			cursor.moveToNext();
		}
		
		return feedList;
	}
	
	private static RssFeed cursorToRssFeed(Cursor cursor)
	{
		RssFeed feed = new RssFeed();
		feed.setTitle(cursor.getString(cursor.getColumnIndex(RssFeedTable.COLUMN_TITLE)));
		feed.setDescription(cursor.getString(cursor.getColumnIndex(RssFeedTable.COLUMN_DESCRIPTION)));
		feed.setLink(cursor.getString(cursor.getColumnIndex(RssFeedTable.COLUMN_LINK)));
		feed.setDate(cursor.getString(cursor.getColumnIndex(RssFeedTable.COLUMN_DATE)));
		feed.setFeedUrl(cursor.getString(cursor.getColumnIndex(RssFeedTable.COLUMN_FEED_URL)));
		feed.setId(cursor.getLong(cursor.getColumnIndex(RssFeedTable.COLUMN_ID)));
		
		return feed;
	}
	
	private static int toInteger(boolean value)
	{
		return value ? 1 : 0;
	}
}
