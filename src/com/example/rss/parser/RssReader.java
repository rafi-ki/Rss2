package com.example.rss.parser;

import java.net.MalformedURLException;
import java.net.URL;

import com.example.rss.model.RssFeed;

public class RssReader
{
    private URL url;

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String LANGUAGE = "language";
    private static final String LINK = "link";
    private static final String AUTHOR = "author";
    private static final String ITEM = "item";
    private static final String DATE = "pubDate";
    private static final String FEED_ID = "guid";
    

    public RssFeed readFeed(String strURL)
    {
        if (strURL == null) //can not be parsed
            return null;

        try {
			this.url = new URL(strURL);
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		}
        RssFeed feed = null;
        
//        boolean isFeedHeader = true;
//        String author = "";
//        String title = "";
//        String language = "";
//        String description = "";
//        String feedId = "";
//        String link = "";
//        String date = "";
//
//        try
//        {
//            // First create a new XMLInputFactory
//            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//            // Setup a new eventReader
//            InputStream in = url.openStream();
//            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
//            // Read the XML document
//            while (eventReader.hasNext()) {
//                XMLEvent event = eventReader.nextEvent();
//                if (event.isStartElement())
//                {
//                    String localPart = event.asStartElement().getName().getLocalPart();
//                    if (localPart.equals(ITEM))
//                    {
//                    	if (isFeedHeader)
//                    	{
//	                    	isFeedHeader = false;
//	                    	feed = new RssFeed(title, link, description, language, date);
//                    	}
//                    } 
//                    else if (localPart.equals(TITLE))
//                    	title = getStringFromReader(event, eventReader);
//                    else if (localPart.equals(DESCRIPTION))
//                    	description = getStringFromReader(event, eventReader);
//                    else if (localPart.equals(LANGUAGE))
//                    	language = getStringFromReader(event, eventReader);
//                    else if (localPart.equals(LINK))
//                    	link = getStringFromReader(event, eventReader);
//                    else if (localPart.equals(AUTHOR))
//                    	author = getStringFromReader(event, eventReader);
//                    else if (localPart.equals(DATE))
//                    	date = getStringFromReader(event, eventReader);
//                    else if (localPart.equals(FEED_ID))
//                    	feedId = getStringFromReader(event, eventReader);
//                }
//                else if (event.isEndElement())
//                {
//                    if (event.asEndElement().getName().getLocalPart().equals(ITEM))
//                    {
//                        FeedItem feedItem = new FeedItem();
//                        feedItem.setAuthor(author);
//                        feedItem.setDescription(description);
//                        feedItem.setFeedId(feedId);
//                        feedItem.setLink(link);
//                        feedItem.setTitle(title);
//                        feed.addFeedItem(feedItem);
//                    }
//                }
//            }
//        } catch (XMLStreamException e)
//        {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//			throw new RuntimeException(e);
//		}

        return feed;
    }
    
//    private String getStringFromReader(XMLEvent event, XMLEventReader eventReader)
//    	      throws XMLStreamException {
//    	    String result = "";
//    	    event = eventReader.nextEvent();
//    	    if (event instanceof Characters) {
//    	      result = event.asCharacters().getData();
//    	    }
//    	    return result;
//    }
}