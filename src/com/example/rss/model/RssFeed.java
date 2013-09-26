package com.example.rss.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an RSS-Feed.
 * It is used as a DataHolder.
 */
public class RssFeed
{
    private String title;
    private String link;
    private String description;
    private String language;
    private String date;

    private List<FeedItem> feedItems = new ArrayList<FeedItem>();

    public RssFeed(String title, String link, String description, String language, String date)
    {
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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
}