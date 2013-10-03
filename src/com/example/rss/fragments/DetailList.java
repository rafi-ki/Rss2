package com.example.rss.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockListFragment;
import com.example.rss.R;
import com.example.rss.model.FeedItem;
import com.example.rss.model.RssFeed;
import com.example.rss.persistance.FeedManager;
import com.example.rss.persistance.RssDefines;

public class DetailList extends SherlockListFragment {
	
	private final static String TITLE_REF = "title";
	private final static String LINK_REF = "link";
	private final static String DESCRIPTION_REF = "description";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		Bundle b = getArguments();
		String link =b.getString(RssDefines.EXTRA_DATA_DETAILS_LINK);
		setDetailsToListView(link);
	}
	
//	@Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.detail_fragment, container, false);
//    }
	
	public void setDetailsToListView(String url)
	 {
		 List<Map<String, String>> items = getDetailsData(url);
		 SimpleAdapter adapter = new SimpleAdapter(getActivity(), items,
				 	R.layout.detail_lv_item, new String[] { TITLE_REF, LINK_REF, DESCRIPTION_REF },
				 	new int[] { R.id.detail_lv_item_title, R.id.detail_lv_item_link, R.id.detail_lv_item_description }
		 );
		 setListAdapter(adapter);
	 }
	 
	 private List<Map<String, String>> getDetailsData(String url)
	 {
		 List<Map<String, String>> returnedList = new ArrayList<Map<String, String>>();
		 Map<String, RssFeed> feedMap = FeedManager.getInstance().getFeedMap();
		 Map<String, String> map = new HashMap<String, String>();
		 RssFeed rssFeed = feedMap.get(url);
		 for (FeedItem feedItem : rssFeed.getFeedItems())
		 {
			 map = new HashMap<String, String>();
			 map.put(TITLE_REF, feedItem.getTitle());
			 map.put(LINK_REF, feedItem.getLink());
			 map.put(DESCRIPTION_REF, feedItem.getDescription());
			 returnedList.add(map);
		 }
		 return returnedList;
	 }
}
