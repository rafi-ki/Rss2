package com.example.rss;

import java.util.LinkedList;


public class FeedManager {
	public static FeedManager instance;
	private LinkedList <Feed> feedlist;
	
	protected FeedManager(){
		instance=this;
		feedlist=new LinkedList<Feed>();
	}
	
	public static FeedManager getInstance(){
		if (instance != null){
			return instance;
		}
		else{
			return new FeedManager();
		}
	}
	
	public boolean addFeed(Feed feedtoadd){
		feedlist.add(feedtoadd);
		return true;
	}
	
	public boolean delFeed(){
		//TODO implement function
		return false;
	}

	public LinkedList <Feed> getFeedlist() {
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
