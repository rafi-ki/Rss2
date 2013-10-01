package com.example.rss;

import java.net.URL;
import java.util.LinkedList;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import at.diamonddogs.data.dataobjects.WebRequest;
import at.diamonddogs.service.net.HttpServiceAssister;
import at.diamonddogs.service.processor.ServiceProcessor;

import com.example.rss.model.RssFeed;
import com.example.rss.parser.CustomRssReader;
import com.example.rss.parser.RssReader;

public class FeedLoaderService extends IntentService {
	
	private HttpServiceAssister assister;
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
		LinkedList<FeedLink> feeds = feedManager.getFeedlist();
		System.out.println("loading feeds " + feeds.size());
		for (FeedLink feed : feeds)
		{
			try{
				//custom rss reader
				CustomRssReader reader = CustomRssReader.getInstance();
				RssFeed rss = reader.read(feed.getFeedurl());
				System.out.println(rss.toString());
			}
            catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("ERROR: "+ex.getMessage());
            }
        }
	}
}
