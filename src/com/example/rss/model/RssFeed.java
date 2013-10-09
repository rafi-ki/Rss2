package com.example.rss.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an RSS-Feed.
 * It is used as a DataHolder.
 */
public class RssFeed implements Serializable
{
	private static final long serialVersionUID = 2700137136490411934L;
	
	private int id;
	private String title;
    private String link;
    private String description;
    private String date;

    private List<FeedItem> feedItems = new ArrayList<FeedItem>();

    public RssFeed(String title, String link, String description, String date)
    {
        this.title = title;
        this.link = link;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<FeedItem> getFeedItems() {
        return feedItems;
    }

    public void setFeedItems(List<FeedItem> feedItems) {
        this.feedItems = feedItems;
    }

    public void addFeedItem(FeedItem feedItem)
    {
        this.feedItems.add(feedItem);
    }
    
    @Override
    public String toString()
    {
    	StringBuilder builder = new StringBuilder();
    	builder.append("RSS Feed\n");
    	builder.append("Title:" + this.title + "\n");
    	builder.append("Link:" + this.link + "\n");
    	builder.append("Description:" + this.description + "\n");
    	for(FeedItem item : feedItems)
    		builder.append(item.toString() + "\n");
    	
    	return builder.toString();
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}