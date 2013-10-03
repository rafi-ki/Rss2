package com.example.rss;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.rss.fragments.DetailList;
import com.example.rss.fragments.FeedListFragment;
import com.example.rss.fragments.SubscriberFragment;
import com.example.rss.persistance.FeedManager;
import com.example.rss.persistance.RssDefines;
import com.example.rss.services.ValidateRssService;

public class MainActivity extends SherlockFragmentActivity {

	private FeedManager feedmanager;
	private FragmentDistributorReceiver distributorReceiver;
	private UpdateFeedListReceiver updateReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getSupportActionBar();
		distributorReceiver = new FragmentDistributorReceiver();
		updateReceiver = new UpdateFeedListReceiver();
		
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
		System.out.println("main onResume()");
		
		IntentFilter filter = new IntentFilter(RssDefines.OPEN_FEED_LIST_FRAGMENT);
		filter.addAction(RssDefines.OPEN_DETAIL_FRAGMENT);
		filter.addAction(RssDefines.OPEN_SUBSCRIBE_FRAGMENT);
		LocalBroadcastManager.getInstance(this).registerReceiver(distributorReceiver, filter);
		
		filter = new IntentFilter(RssDefines.REFRESH_FEED_LIST);
		filter.addAction(RssDefines.VALIDATE_RSS);
		LocalBroadcastManager.getInstance(this).registerReceiver(updateReceiver, filter);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		System.out.println("main onPause()");
		feedmanager.storeSubscribedFeeds(this); // store current feeds
		
		// unregister receiver
		LocalBroadcastManager.getInstance(this).unregisterReceiver(distributorReceiver); 
		LocalBroadcastManager.getInstance(this).unregisterReceiver(updateReceiver);
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
      //hide keyboard
		InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(findViewById(R.id.subscribe_url_input).getWindowToken(), 0);
        EditText urlinput = (EditText) findViewById(R.id.subscribe_url_input);
        String feedurlstring = urlinput.getEditableText().toString();
        System.out.println("Url: "+urlinput.getEditableText());
        
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_activity);
		TextView textView = (TextView) f.getView().findViewById(R.id.subscribe_tv_message);
		textView.setText("validating! please wait ...");
		
		Intent validateIntent = new Intent(this, ValidateRssService.class);
        validateIntent.putExtra(RssDefines.EXTRA_VALIDATE_RSS_URL, feedurlstring);
        startService(validateIntent);
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
	
	private class UpdateFeedListReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			FragmentManager fragmentManager = getSupportFragmentManager();
			if (action.equals(RssDefines.REFRESH_FEED_LIST))
			{
				System.out.println("Received update feed list message");
				Fragment fra = fragmentManager.findFragmentById(R.id.main_activity);
				if (fra instanceof FeedListFragment)
				{
					FeedListFragment feedListFragment = (FeedListFragment) fra;
					feedListFragment.setFeedMapToListView();
				}
			}
			if (action.equals(RssDefines.VALIDATE_RSS))
			{
				System.out.println("recieved validate rss result");
				if (intent.getBooleanExtra(RssDefines.EXTRA_VALIDATE_RSS_RESULT, false))
				{
					fragmentManager.popBackStack();
				}
				else
				{
					Fragment f = fragmentManager.findFragmentById(R.id.main_activity);
					TextView textView = (TextView) f.getView().findViewById(R.id.subscribe_tv_message);
					textView.setTextColor(Color.RED);
					textView.setText("wrong url, please enter a valid one");
				}
			}
		}
	}
}
