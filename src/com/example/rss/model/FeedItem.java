package com.example.rss.model;

import java.io.Serializable;

/**
 * This class is an represents a Feed Item.
 * It is used as a DataHolder.
 */
public class FeedItem implements Serializable
{
	private static final long serialVersionUID = 1709487133434245708L;
	
	private long id;
	private long rssFeedId;
	private String title;
    private String description;
    private String link;
    private String author;
    private boolean starred;
    private boolean read;

    public FeedItem()
    {
        this("", "", "", "", "");
    }

    public FeedItem(String title, String description, String link, String feedId, String author)
    {
        this.title = title;
        this.description = description;
        this.link = link;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString()
    {
        return "FeedItem: [Title:" + this.title + ", Author:" + this.author
                + ", Description:" + this.description + ", Link:" + this.link
                + "]";
    }
    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRssFeedId() {
		return rssFeedId;
	}

	public void setRssFeedId(long rssFeedId) {
		this.rssFeedId = rssFeedId;
	}

	public boolean isStarred() {
		return starred;
	}

	public void setStarred(boolean starred) {
		this.starred = starred;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
}