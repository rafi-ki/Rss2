package com.example.rss.persistance;

import com.example.rss.model.FeedItem;
import com.example.rss.model.RssFeed;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

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
		values.put(FeedItemTable.COLUMN_READ_STATE, item.isRead() ? 1 : 0); // no booleans in sqlite
		values.put(FeedItemTable.COLUMN_STARRED_STATE, item.isStarred() ? 1 : 0); // no booleans in sqlite
		
		return cr.update(FeedContentProvider.CONTENT_URI_FEED_ITEM, values, FeedItemTable.COLUMN_ID + "=" + id, null);
	}
	
	public static int updateRssFeed(Context c, long id, RssFeed feed)
	{
		ContentResolver cr = c.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(RssFeedTable.COLUMN_ID, feed.getId());
		values.put(RssFeedTable.COLUMN_TITLE, feed.getTitle());
		values.put(RssFeedTable.COLUMN_LINK, feed.getLink());
		values.put(RssFeedTable.COLUMN_DESCRIPTION, feed.getDescription());
		values.put(RssFeedTable.COLUMN_DATE, feed.getDate());
		
		return cr.update(FeedContentProvider.CONTENT_URI_FEED_ITEM, values, RssFeedTable.COLUMN_ID + "=" + id, null);
	}
}
