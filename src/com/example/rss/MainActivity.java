package com.example.rss;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.rss.fragments.FeedListFragment;
import com.example.rss.fragments.SubscriberFragment;

public class MainActivity extends SherlockFragmentActivity {

	private SubscribedFeedManager feedmanager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActionBar bar = getSupportActionBar();
		
		
		feedmanager= SubscribedFeedManager.getInstance(); //gets instance of the feedmanager (singelton)
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		 FragmentTransaction transaction = fragmentManager.beginTransaction();
		 transaction.add(R.id.main_activity, new FeedListFragment());
		 transaction.commit();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		//TODO stop service here, if necessary
	}

	@Override 
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) { 
		MenuInflater i = new MenuInflater(this); 
		i.inflate(R.id.action_subscribe, menu); 
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
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
		}

	}

	//Button method to subscribe to the feed with the url from the edittextview of the same fragment
	public void subscribeButtonClick(View v) {
        System.out.println("button was clicked");
        
        EditText urlinput = (EditText) findViewById(R.id.subscribe_url_input);
        System.out.println("Url: "+urlinput.getEditableText());
        
        String feedurlstring = urlinput.getEditableText().toString();
        if(feedurlstring!=""){
        	feedmanager.addFeed(new FeedLink(feedurlstring));
        }
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
      // FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.replace(R.id.main_activity, new FeedListFragment());
       // transaction.commit();
    }
	
}
