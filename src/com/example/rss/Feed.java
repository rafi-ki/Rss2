package com.example.rss;

public class Feed {
	private String feedurl;
	
	public Feed(String url){
		setFeedurl(url);
	}

	public String getFeedurl() {
		return feedurl;
	}

	public void setFeedurl(String feedurl) {
		this.feedurl = feedurl;
	}
}
