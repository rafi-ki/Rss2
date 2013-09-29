package com.example.rss;

import java.net.URL;
import java.util.LinkedList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class FeedLoaderService extends Service {
	
	FeedManager feedManager;

	@Override
	public void onCreate() {
		feedManager = FeedManager.getInstance();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		loadFeeds();
		return START_STICKY;
	}
	
	public void loadFeeds()
	{
		Thread t = new Thread()
		{
			public void run()
			{
				feedManager.restoreSubscribedFeeds();
				LinkedList<Feed> feeds = feedManager.getFeedlist();
				for (Feed feed : feeds)
				{
					try{
						//TODO do it the right way
						URL feedUrl = new URL(feed.getFeedurl());
						SyndFeedInput input = new SyndFeedInput();
		                SyndFeed synFeed = input.build(new XmlReader(feedUrl));
		                
//		                Intent intent = new Intent("receive feeds");
//		                intent.putExtra("feed", synFeed.getLink());
//		                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent); // send broadcast
					}
	                catch (Exception ex) {
		                ex.printStackTrace();
		                System.out.println("ERROR: "+ex.getMessage());
		            }
	            }
			}
		};
		t.start();
	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}
	
	
}
