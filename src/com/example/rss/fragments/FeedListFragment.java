package com.example.rss.fragments;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockListFragment;
import com.example.rss.FeedLoaderService;
import com.example.rss.R;
import com.example.rss.FeedManager;

public class FeedListFragment extends SherlockListFragment {
	
	private final static String STATE_KEY = "curPos";
	private final static String FEED_COMMUNICATOR = "receive feeds";
	private final static String FEED_MESSAGE = "feed";
	private int lastPosition;
	private FeedManager feedmanager;
	private BroadcastReceiver feedReceiver;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		 System.out.println("FeedList-Fragment created");
		 
		 feedmanager= FeedManager.getInstance();
		 feedReceiver = new FeedReceiver();
		 
		 IntentFilter filter = new IntentFilter(FEED_COMMUNICATOR);
		 LocalBroadcastManager.getInstance(getActivity()).registerReceiver(feedReceiver, filter);
	}
		
	 @Override
	 public void onActivityCreated(Bundle savedInstanceState) 
	 {
		 super.onActivityCreated(savedInstanceState);
		 
		 System.out.println("feedlist on activity created");
		 if (savedInstanceState != null) // save state
			 lastPosition = savedInstanceState.getInt(STATE_KEY, 0);
		 
//		 ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1);
		 List<Map<String, String>> list = getFeedData();
		 SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
					R.layout.subscribed_lv_item, new String[] { "title", "link",
							"date" }, new int[] { R.id.subscribed_lv_item_title,
							R.id.subscribed_lv_item_link, R.id.subscribed_lv_item_date }
			);
		 
		 setListAdapter(adapter);
		 
	 }
	 
	 //TODO return empty list if ready
	 private List<Map<String, String>> getFeedData()
	 {
		 List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		 Map map = new HashMap<String, String>();
		 map.put("title", "Feed Title1");
		 map.put("link", "http://blablaliej.com");
		 map.put("date", "12.02.2013");
		 list.add(map);
		 map = new HashMap<String, String>();
		 map.put("title", "Feed Title2");
		 map.put("link", "http:///hurra.com");
		 map.put("date", "30.01.2011");
		 list.add(map);
		 return list;
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
		 System.out.println("item clicked: " + position + ", " + id);
		 
		 FragmentManager fragmentManager = getFragmentManager();
		 FragmentTransaction transaction = fragmentManager.beginTransaction();
		 transaction.addToBackStack(null);
		 transaction.replace(R.id.main_activity, new DetailList());
		 transaction.commit();
	 }
	 
	 
	 private class FeedReceiver extends BroadcastReceiver
	 {
		@Override
		public void onReceive(Context context, Intent intent) {
			String feed = intent.getStringExtra(FEED_MESSAGE);
			ArrayAdapter<String> adapter = (ArrayAdapter) getListAdapter();
			adapter.add(feed);
		}
	 }
	 
	 @Override
	 public void onDestroy()
	 {
		 LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(feedReceiver);
		 super.onDestroy();
	 }
}
