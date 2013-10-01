package com.example.rss.persistance;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.example.rss.model.RssFeed;


public class FeedManager {
	private static final String STORAGE_FILENAME = "feed_storage";
	
	private static FeedManager instance;
	private Map<String, RssFeed> feedMap;
	
	protected FeedManager(){
		feedMap = new HashMap<String, RssFeed>();
	}
	
	public static FeedManager getInstance(){
		if (instance == null)
			instance = new FeedManager();
		return instance;
	}
	
	public boolean addRssFeed(String link, RssFeed feed){
		feedMap.put(link, feed);
		return true;
	}
	
	public boolean delFeed(String link){
		feedMap.remove(link);
		return true;
	}

	public Map<String, RssFeed> getFeedMap()
	{
		return feedMap;
	}
	
	/**
	 * writes to internal storage
	 * @param c
	 * @return
	 */
	public boolean storeSubscribedFeeds(Context c)
	{
		System.out.println("Store feeds");
		ObjectOutputStream oos = null;
		try {
			FileOutputStream fos = c.openFileOutput(STORAGE_FILENAME, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(feedMap);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally{
			try {
				if (oos != null)
					oos.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		System.out.println("successfully stored feeds");
		return true;
	}
	
	/**
	 * reads from internal storage
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean restoreSubscribedFeeds(Context c)
	{
		System.out.println("restore feeds");
		ObjectInputStream ois = null;
		try{
			FileInputStream fis = c.openFileInput(STORAGE_FILENAME);
			ois = new ObjectInputStream(fis);
			this.feedMap = (Map<String, RssFeed>) ois.readObject();;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			System.out.println("File does not exist yet");
			return false;
		} finally {
			try {
				if (ois != null)
					ois.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		System.out.println("successfully restored feeds");
		return true;
	}

}
