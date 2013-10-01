package com.example.rss;


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
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.rss.fragments.DetailList;
import com.example.rss.fragments.FeedListFragment;
import com.example.rss.fragments.SubscriberFragment;
import com.example.rss.model.RssFeed;
import com.example.rss.persistance.FeedManager;
import com.example.rss.persistance.RssDefines;

public class MainActivity extends SherlockFragmentActivity {

	private FeedManager feedmanager;
	private FragmentDistributorReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActionBar bar = getSupportActionBar();
		receiver = new FragmentDistributorReceiver();
		
		feedmanager = FeedManager.getInstance(); //gets instance of the feedmanager (singelton)
		feedmanager.restoreSubscribedFeeds(this);
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		 FragmentTransaction transaction = fragmentManager.beginTransaction();
		 transaction.add(R.id.main_activity, new FeedListFragment());
		 transaction.commit();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		//TODO add local broadcast receiver
		IntentFilter filter = new IntentFilter(RssDefines.OPEN_FEED_LIST_FRAGMENT);
		filter.addAction(RssDefines.OPEN_DETAIL_FRAGMENT);
		filter.addAction(RssDefines.OPEN_SUBSCRIBE_FRAGMENT);
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
		//TODO stop service here, if necessary
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		feedmanager.storeSubscribedFeeds(this);
	}

	@Override 
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) { 
		MenuInflater i = new MenuInflater(this); 
		i.inflate(R.menu.main, menu); 
		return super.onCreateOptionsMenu(menu); 
	} 
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    // Handle item selection
        if(item.getItemId() ==  R.id.action_subscribe){
        	FragmentManager fragmentManager = getSupportFragmentManager();
        	FragmentTransaction transaction = fragmentManager.beginTransaction();
        	transaction.addToBackStack(null);
        	transaction.replace(R.id.main_activity, new SubscriberFragment());
        	transaction.commit();
        }
        return super.onOptionsItemSelected(item);
	}

	//Button method to subscribe to the feed with the url from the edittextview of the same fragment
	public void subscribeButtonClick(View v) {
        System.out.println("button was clicked");
        
        EditText urlinput = (EditText) findViewById(R.id.subscribe_url_input);
        System.out.println("Url: "+urlinput.getEditableText());
        
        //TODO load just one feed with another service and add it to map
        
        String feedurlstring = urlinput.getEditableText().toString();
        if(feedurlstring!=""){
        	feedmanager.addRssFeed(feedurlstring, new RssFeed("Title", feedurlstring, "bla desc", "date"));
        }
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
      // FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.replace(R.id.main_activity, new FeedListFragment());
       // transaction.commit();
    }
	
	private class FragmentDistributorReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			if (action.equals(RssDefines.OPEN_DETAIL_FRAGMENT))
			{
				String link = intent.getExtras().getString(RssDefines.EXTRA_DATA_DETAILS_LINK);
				 transaction.addToBackStack(null);
				 DetailList detailList = new DetailList();
				 Bundle bundle = new Bundle();
				 bundle.putString(RssDefines.EXTRA_DATA_DETAILS_LINK, link);
				 detailList.setArguments(bundle);
				 transaction.replace(R.id.main_activity, detailList);
				 transaction.commit();
			}
			else if (action.equals(RssDefines.OPEN_FEED_LIST_FRAGMENT))
			{
				transaction.addToBackStack(null);
				transaction.replace(R.id.main_activity, new FeedListFragment());
				transaction.commit();
			}
			else if (action.equals(RssDefines.OPEN_SUBSCRIBE_FRAGMENT))
			{
				transaction.addToBackStack(null);
				transaction.replace(R.id.main_activity, new SubscriberFragment());
				transaction.commit();
			}
		}
	}
}
