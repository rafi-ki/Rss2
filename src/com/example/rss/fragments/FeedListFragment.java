package com.example.rss.fragments;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.example.rss.R;
import com.example.rss.model.RssFeed;
import com.example.rss.persistance.FeedManager;
import com.example.rss.persistance.RssDefines;
import com.example.rss.services.FeedLoaderService;

public class FeedListFragment extends SherlockListFragment {
	
	private static final String TITLE_REF = "title";
	private static final String LINK_REF = "link";	
	
	private FeedManager feedmanager;
	private RefreshFeedListReceiver receiver;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		System.out.println("FeedList-Fragment created");
		 
		feedmanager= FeedManager.getInstance();
		receiver = new RefreshFeedListReceiver();
	}
		
	 @Override
	 public void onActivityCreated(Bundle savedInstanceState) 
	 {
		 super.onActivityCreated(savedInstanceState);
		 
		 setFeedMapToListView();
	 }
	 
	 public void setFeedMapToListView()
	 {
		 List<Map<String, String>> items = getFeedData();
		 SimpleAdapter adapter = new SimpleAdapter(getActivity(), items,
				 	R.layout.subscribed_lv_item, new String[] { TITLE_REF, LINK_REF },
				 	new int[] { R.id.subscribed_lv_item_title, R.id.subscribed_lv_item_link }
		 );
		 setListAdapter(adapter);
	 }
	 
	 private List<Map<String, String>> getFeedData()
	 {
		 List<Map<String, String>> returnedList = new ArrayList<Map<String, String>>();
		 Map<String, RssFeed> feedMap = feedmanager.getFeedMap();
		 HashMap<String, String> map = null;
		 Set<String> keys = feedMap.keySet();
		 RssFeed feed = null;
		 for (String key : keys)
		 {
			 map = new HashMap<String, String>();
			 feed = feedMap.get(key);
			 map.put(TITLE_REF, feed.getTitle());
			 map.put(LINK_REF, key);
			 returnedList.add(map);
		 }
		 return returnedList;
	 }
	 
	 @Override
	 public void onResume()
	 {
		 super.onResume();
		 
		 //define receiver for refreshing feed list
		 IntentFilter filter = new IntentFilter(RssDefines.REFRESH_FEED_LIST);
		 LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
		 
		 Intent intent = new Intent(getActivity(), FeedLoaderService.class);
		 getActivity().startService(intent);
	 }
	 
	 @Override
	 public void onPause()
	 {
		 super.onPause();
		 LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
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
	 
	 private class RefreshFeedListReceiver extends BroadcastReceiver
	 {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(RssDefines.REFRESH_FEED_LIST))
				setFeedMapToListView(); // refresh feed list
		}
	 }
}
