package com.example.rss.observer;

import android.database.ContentObserver;
import android.os.Handler;

public class RssFeedObserver extends ContentObserver {

	public RssFeedObserver(Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onChange(boolean selfChange)
	{
		System.out.println("RssFeeds has changed");
	}
	
}
