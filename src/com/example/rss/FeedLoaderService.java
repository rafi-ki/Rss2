package com.example.rss;

import java.util.Map;
import java.util.Set;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.rss.model.RssFeed;
import com.example.rss.parser.CustomRssReader;
import com.example.rss.persistance.FeedManager;
import com.example.rss.persistance.RssDefines;

public class FeedLoaderService extends IntentService {
	private FeedManager feedManager;
	
	public FeedLoaderService() {
		super("FeedLoaderService");
		feedManager = FeedManager.getInstance();
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    System.out.println("loading feeds");
	    return super.onStartCommand(intent,flags,startId);
	}
	
	@Override
	public void onDestroy() {
		System.out.println("feeds load destroyed");
		super.onDestroy();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Map<String, RssFeed> feedMap = feedManager.getFeedMap();
		Set<String> keys = feedMap.keySet();
		//the key is the link
		for (String key : keys)
		{
			try{
				CustomRssReader reader = CustomRssReader.getInstance();
				RssFeed rss = reader.read(key);
				feedMap.put(key, rss); //override existing -> update
			}
            catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("ERROR: "+ex.getMessage());
            }
		}
		
		Intent refreshIntent = new Intent(RssDefines.REFRESH_FEED_LIST);
		LocalBroadcastManager.getInstance(this).sendBroadcast(refreshIntent);
	}
}
