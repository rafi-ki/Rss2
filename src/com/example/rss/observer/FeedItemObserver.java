package com.example.rss.observer;

import android.database.ContentObserver;
import android.os.Handler;

public class FeedItemObserver extends ContentObserver {

	public FeedItemObserver(Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onChange(boolean selfChange)
	{
		System.out.println("FeedItem has changed");
	}

}
