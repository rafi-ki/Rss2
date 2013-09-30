package com.example.rss;

import java.util.LinkedList;


public class SubscribedFeedManager {
	public static SubscribedFeedManager instance;
	private LinkedList <FeedLink> feedlist;
	
	protected SubscribedFeedManager(){
		instance=this;
		feedlist=new LinkedList<FeedLink>();
	}
	
	public static SubscribedFeedManager getInstance(){
		if (instance != null){
			return instance;
		}
		else{
			return new SubscribedFeedManager();
		}
	}
	
	public boolean addFeed(FeedLink feedtoadd){
		feedlist.add(feedtoadd);
		return true;
	}
	
	public boolean delFeed(){
		//TODO implement function
		return false;
	}

	public LinkedList <FeedLink> getFeedlist() {
		return feedlist;
	}
	
	public boolean storeSubscribedFeeds()
	{
		//TODO implement function, think about how to save  them
		return false;
	}
	
	public boolean restoreSubscribedFeeds()
	{
		//TODO implement function, vice-versa to storeSubscibedFeeds()
		return false;
	}

}
