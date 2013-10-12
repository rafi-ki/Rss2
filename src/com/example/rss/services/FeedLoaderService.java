package com.example.rss.services;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;

import com.example.rss.model.RssFeed;
import com.example.rss.parser.CustomRssReader;
import com.example.rss.persistance.FeedDatabase;

public class FeedLoaderService extends IntentService {
	
	public FeedLoaderService() {
		super("FeedLoaderService");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    return super.onStartCommand(intent,flags,startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onHandleIntent(Intent intent)
	{
		List<RssFeed> feeds = FeedDatabase.getRssFeeds(this);
		CustomRssReader reader = CustomRssReader.getInstance();
		for (RssFeed feed : feeds)
		{
			try{
				String link = feed.getLink();
				RssFeed updatedFeed = reader.read(link);
				if (updatedFeed != null)
				{
					FeedDatabase.updateRssFeed(this, feed.getId(), updatedFeed);
				}
			} catch (Exception ex)
			{
				ex.printStackTrace();
				System.out.println("ERROR: "+ex.getMessage());
			}
		}
		// TODO check for contentObserver, will it reseive the update?
	}
}
