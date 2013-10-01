package com.example.rss.parser;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.example.rss.model.FeedItem;
import com.example.rss.model.RssFeed;


public class CustomRssReader {
	private static CustomRssReader instance = null;

	private CustomRssReader() {
	}

	public static CustomRssReader getInstance() {
		if (instance == null)
			instance = new CustomRssReader();
		return instance;
	}

	public RssFeed read(String url) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			URL u = new URL(url); // your feed
			Document doc = builder.parse(u.openStream());
			
			// get the feed-information
			NodeList feedTitle = doc.getElementsByTagName("title");
			NodeList feedDescription = doc.getElementsByTagName("description");
			NodeList feedLink = doc.getElementsByTagName("link");
			NodeList feedPubDate = doc.getElementsByTagName("pubDate");
			Element titleElement = (Element) feedTitle.item(0);
			Element descriptionElement = (Element) feedDescription.item(0);
			Element linkElement = (Element) feedLink.item(0);
			Element pubDateElement = (Element) feedPubDate.item(0);
			String strTitle = titleElement.getTextContent();
			String strDescription = descriptionElement.getTextContent();
			String strLink = linkElement.getTextContent();
			String strPubDate = pubDateElement.getTextContent();
			
			RssFeed feed = new RssFeed(strTitle, strLink, strDescription, strPubDate);
			
			// get the feed items
			NodeList nodes = doc.getElementsByTagName("item");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				FeedItem item = new FeedItem();
				item.setTitle(getElementValue(element, "title"));
				item.setAuthor(getElementValue(element, "author"));
				item.setLink(getElementValue(element, "link"));
				item.setDescription(getElementValue(element, "description"));
				item.setFeedId(getElementValue(element, "guid"));
				
				feed.addFeedItem(item);
			}
			
			return feed;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}

	private String getCharacterDataFromElement(Element e) {
		try {
			Node child = e.getFirstChild();
			if (child instanceof CharacterData) {
				CharacterData cd = (CharacterData) child;
				return cd.getData();
			}
		} catch (Exception ex) {

		}
		return "";
	}

	protected float getFloat(String value) {
		if (value != null && !value.equals(""))
			return Float.parseFloat(value);
		else
			return 0;
	}

	protected String getElementValue(Element parent, String label) {
		return getCharacterDataFromElement((Element) parent
				.getElementsByTagName(label).item(0));
	}
}
