package com.example.rss;

public class FeedLink {
	private String feedurl;
	
	public FeedLink(String url){
		setFeedurl(url);
	}

	public String getFeedurl() {
		return feedurl;
	}

	public void setFeedurl(String feedurl) {
		this.feedurl = feedurl;
	}
}
