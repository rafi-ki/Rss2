package com.example.rss;

import java.net.URL;
import java.util.LinkedList;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class FeedLoaderService extends IntentService {
	
	
	FeedManager feedManager;
	
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
		feedManager.restoreSubscribedFeeds();
		LinkedList<Feed> feeds = feedManager.getFeedlist();
		for (Feed feed : feeds)
		{
			try{
				//TODO do it the right way
				URL feedUrl = new URL(feed.getFeedurl());
				SyndFeedInput input = new SyndFeedInput();
                SyndFeed synFeed = input.build(new XmlReader(feedUrl));
                
//                Intent intent = new Intent("receive feeds");
//                intent.putExtra("feed", synFeed.getLink());
//                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent); // send broadcast
			}
            catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("ERROR: "+ex.getMessage());
            }
        }
	}
	
	
}
