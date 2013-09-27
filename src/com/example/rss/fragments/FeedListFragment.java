package com.example.rss.fragments;


import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FeedListFragment extends ListFragment {
	
	private final static String STATE_KEY = "curPos";
	private int lastPosition;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		 System.out.println("FeedList-Fragment created");
	}
		
	 @Override
	 public void onActivityCreated(Bundle savedInstanceState) 
	 {
		 super.onActivityCreated(savedInstanceState);
		 
		 if (savedInstanceState != null) // save state
			 lastPosition = savedInstanceState.getInt(STATE_KEY, 0);
		 
		 ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1);
//		 RssReader reader = new RssReader();
//		 RssFeed feed = reader.readFeed("http://rss.nytimes.com/services/xml/rss/nyt/Sports.xml");
//		 System.out.println("read rss feed");
//		 adapter.add(feed.getTitle());
//		 adapter.add(feed.getLanguage());
//		 FeedItem item = new FeedItem();
		 
		 
		 adapter.add("click to go to details");
		 adapter.add("fancy listitem");
		 adapter.add("cant add real feed-item :(");
		 setListAdapter(adapter);
		 
		 //TODO set 
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
	 }
}
