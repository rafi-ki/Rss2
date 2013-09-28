package com.example.rss.fragments;


import com.example.rss.FeedManager;
import com.example.rss.R;
import com.example.rss.Feed;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FeedListFragment extends ListFragment {
	
	private final static String STATE_KEY = "curPos";
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
		 
		 if (savedInstanceState != null) // save state
			 lastPosition = savedInstanceState.getInt(STATE_KEY, 0);
		 
		 ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1);
		 //TODO use underlying code on other thread than ui-thread
//		 try {
//             URL feedUrl = new URL("http://rss.nytimes.com/services/xml/rss/nyt/Sports.xml");
//
//             SyndFeedInput input = new SyndFeedInput();
//             SyndFeed feed = input.build(new XmlReader(feedUrl));
//             System.out.println(feed.toString());
//             adapter.add(feed.getAuthor() + " " + feed.getTitle());
//         }
//         catch (Exception ex) {
//             ex.printStackTrace();
//             System.out.println("ERROR: "+ex.getMessage());
//         }
		 
		 
		 for(Feed f: feedmanager.getFeedlist()){
			 adapter.add(f.getFeedurl());
		 }
		 
		 
		 setListAdapter(adapter);
		 
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
}
