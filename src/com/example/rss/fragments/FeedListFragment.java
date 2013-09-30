package com.example.rss.fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.rss.FeedLink;
import com.example.rss.FeedLoaderService;
import com.example.rss.SubscribedFeedManager;
import com.example.rss.R;

public class FeedListFragment extends ListFragment {
	
	private final static String STATE_KEY = "curPos";
	private final static String FEED_COMMUNICATOR = "receive feeds";
	private final static String FEED_MESSAGE = "feed";
	private int lastPosition;
	private SubscribedFeedManager feedmanager;
	private BroadcastReceiver feedReceiver;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		 System.out.println("FeedList-Fragment created");
		 
		 feedmanager= SubscribedFeedManager.getInstance();
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
		 
		 ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1);
		 
		 for(FeedLink f: feedmanager.getFeedlist()){
			 adapter.add(f.getFeedurl());
		 }
		 
		 setListAdapter(adapter);
		 
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
