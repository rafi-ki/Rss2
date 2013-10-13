package com.example.rss.observer;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;

import com.example.rss.persistance.RssDefines;

public class FeedItemObserver extends ContentObserver {
	private Context c;
	public FeedItemObserver(Handler handler, Context c) {
		super(handler);
		this.c = c;
	}
	
	@Override
	public void onChange(boolean selfChange)
	{
		Intent updateUIIntent = new Intent(RssDefines.REFRESH_DETAIL_LIST);
		LocalBroadcastManager.getInstance(c).sendBroadcast(updateUIIntent);
		System.out.println("FeedItem has changed");
	}

}
