package com.example.rss.fragments;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.example.rss.FeedLoaderService;
import com.example.rss.R;
import com.example.rss.model.RssFeed;
import com.example.rss.persistance.FeedManager;
import com.example.rss.persistance.RssDefines;

public class FeedListFragment extends SherlockListFragment {
	
	private final static String STATE_KEY = "curPos";
	private final static String FEED_COMMUNICATOR = "receive feeds";
	private final static String FEED_MESSAGE = "feed";
	private int lastPosition;
	private FeedManager feedmanager;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		 System.out.println("FeedList-Fragment created");
		 
		 feedmanager= FeedManager.getInstance();
	}
		
	 @Override
	 public void onActivityCreated(Bundle savedInstanceState) 
	 {
		 super.onActivityCreated(savedInstanceState);
		 
		 System.out.println("feedlist on activity created");
		 if (savedInstanceState != null) // save state
			 lastPosition = savedInstanceState.getInt(STATE_KEY, 0);
		 
		 List<Map<String, String>> list = getFeedData();
		 SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
					R.layout.subscribed_lv_item, new String[] { "title",
							"link" }, new int[] { R.id.subscribed_lv_item_title, R.id.subscribed_lv_item_link }
			);
		 
		 setListAdapter(adapter);
		 
	 }
	 
	 private List<Map<String, String>> getFeedData()
	 {
		 List<Map<String, String>> returnedList = new ArrayList<Map<String, String>>();
		 Map<String, String> map = new HashMap<String, String>();
		 
		 Map<String, RssFeed> feedMap = feedmanager.getFeedMap();
		 Set<String> keys = feedMap.keySet();
		 RssFeed feed = null;
		 for (String key : keys)
		 {
			 feed = feedMap.get(key);
			 map.put("title", feed.getTitle());
			 map.put("link", key);
			 returnedList.add(map);
		 }
		
		 return returnedList;
	 }
	 
	 @Override
	 public void onResume()
	 {
		 super.onResume();
		 Intent intent = new Intent(getActivity(), FeedLoaderService.class);
		 getActivity().startService(intent);
	 }
	 
	 
	 @Override
	 public void onSaveInstanceState(Bundle state)
	 {
		 super.onSaveInstanceState(state);
		 state.putInt(STATE_KEY, lastPosition);
	 }
	 
	 @Override
	 public void onListItemClick(ListView lv, View v, int position, long id)
	 {
		 RelativeLayout layout = (RelativeLayout) v;
		 TextView linkview = (TextView) layout.getChildAt(1); // get textview of link
		 String link = linkview.getText().toString();
		 Intent intent = new Intent(RssDefines.OPEN_DETAIL_FRAGMENT);
		 
		 // put the link as extra data so the main activity knows feed
		 intent.putExtra(RssDefines.EXTRA_DATA_DETAILS_LINK, link);
		 LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
	 }
	 
	 @Override
	 public void onDestroy()
	 {
		 super.onDestroy();
	 }
}
