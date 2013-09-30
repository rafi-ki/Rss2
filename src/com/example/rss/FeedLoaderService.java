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

import com.example.rss.parser.RssReader;

public class FeedLoaderService extends IntentService {
	
	private HttpServiceAssister assister;
	private SubscribedFeedManager feedManager;
	
	public FeedLoaderService() {
		super("FeedLoaderService");
		feedManager = SubscribedFeedManager.getInstance();
		
		assister = new HttpServiceAssister(this);
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
		LinkedList<FeedLink> feeds = feedManager.getFeedlist();
		for (FeedLink feed : feeds)
		{
			try{
				//TODO do it the right way
				URL feedUrl = new URL(feed.getFeedurl());
				assister.runWebRequest(new RssHandler(), createGetRssRequest(feedUrl), new RssReader());
			}
            catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("ERROR: "+ex.getMessage());
            }
        }
	}
	
	private final class RssHandler extends Handler {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == RssReader.ID) {
				if (msg.arg1 == ServiceProcessor.RETURN_MESSAGE_OK) {
					String[] items = (String[]) msg.obj;
					for (String item : items)
						System.out.println(item);
				}
			}
		}
	}
	
	private WebRequest createGetRssRequest(URL url) {
		WebRequest wr = new WebRequest();
		wr.setUrl(url);
		wr.setProcessorId(RssReader.ID);

		// This is the important part, telling HttpService how long a WebRequest
		// will be saved. Since RssProcessor extends XMLProcessor, which extends
		// DataProcessor, the WebRequest's data will be cached automatically,
		// provided that cacheTime is not CACHE_NO.
		wr.setCacheTime(5000);

		// Enables offline caching. usually, cache data is deleted on retrieval
		// if it has expired even if the device is not online. If this flag is
		// set to true, cache data will not be removed if it has expired as long
		// as the device was offline during the request
		wr.setUseOfflineCache(true);
		return wr;
	}
	
}
