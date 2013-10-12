package com.example.rss.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import com.example.rss.model.FeedItem;
import com.example.rss.model.RssFeed;
import com.example.rss.parser.CustomRssReader;
import com.example.rss.persistance.FeedDatabase;
import com.example.rss.persistance.RssDefines;

public class ValidateRssService extends IntentService {

	public ValidateRssService() {
		super("ValidateRssService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		String url = intent.getStringExtra(RssDefines.EXTRA_VALIDATE_RSS_URL); // the url to be validate
		System.out.println("started valdiate-service with url: " + url);
		
		CustomRssReader reader = CustomRssReader.getInstance();
		RssFeed feed = reader.read(url);
		LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
		Intent sendIntent = new Intent(RssDefines.VALIDATE_RSS);
		if (feed != null)
		{
			Uri insertUri = FeedDatabase.insertRssFeed(this, feed); // add to database
			String rssId = insertUri.getLastPathSegment();
			for (FeedItem item : feed.getFeedItems())
			{
				item.setRssFeedId(Long.parseLong(rssId));
				FeedDatabase.insertFeedItem(this, item);
			}
			sendIntent.putExtra(RssDefines.EXTRA_VALIDATE_RSS_RESULT, true);
			bManager.sendBroadcast(sendIntent);
		}
		else
		{
			// do not add to feedManager
			sendIntent.putExtra(RssDefines.EXTRA_VALIDATE_RSS_RESULT, false);
			bManager.sendBroadcast(sendIntent);
		}
	}

}
