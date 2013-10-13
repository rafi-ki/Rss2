package com.example.rss.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SimpleCursorAdapter;


import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.rss.R;

import com.example.rss.observer.RssFeedObserver;
import com.example.rss.persistance.FeedContentProvider;
import com.example.rss.persistance.FeedManager;
import com.example.rss.persistance.RssDefines;
import com.example.rss.persistance.RssFeedTable;

public class FeedListFragment extends SherlockListFragment 
	implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private FeedManager feedmanager;
	private RefreshFeedListReceiver receiver;
	private ContentObserver feedListObserver;
	
	private SimpleCursorAdapter adapter;
	
	private String itemlinktodelete;
	private ActionMode mActionMode;
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback(){

		@Override
	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	        // Inflate a menu resource providing context menu items
	        MenuInflater inflater = mode.getMenuInflater();
	        inflater.inflate(R.menu.feed_list_action_menu, menu);
	        return true;
	    }


		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			 // Return false if nothing is done
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
            case R.id.action_feed_list_delete:
            	
            	feedmanager.delFeed(itemlinktodelete);
	        	   
	        	Intent in = new Intent(RssDefines.REFRESH_FEED_LIST);
	        	LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(in);
            	
            	Toast.makeText(getActivity(), "DELETE BITCH!!!", Toast.LENGTH_SHORT).show();
                mActionMode.finish();
            	return true;
            default:
                return false;
        }

		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode=null;
			
		}};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		System.out.println("FeedList-Fragment created");
		
		
		feedmanager= FeedManager.getInstance();
		receiver = new RefreshFeedListReceiver();
		feedListObserver = new RssFeedObserver(new Handler(), this.getActivity());
		
		
		//define receiver for refreshing feed list
		IntentFilter filter = new IntentFilter(RssDefines.REFRESH_FEED_LIST);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
		 
	}
		
	 @Override
	 public void onActivityCreated(Bundle savedInstanceState) 
	 {
		 super.onActivityCreated(savedInstanceState);
		 
		 String[] from = {RssFeedTable.COLUMN_TITLE, RssFeedTable.COLUMN_LINK};
		 int[] to = {R.id.subscribed_lv_item_title, R.id.subscribed_lv_item_link};
		 adapter = new SimpleCursorAdapter(getActivity(), R.layout.subscribed_lv_item, null, from, to, 0); 
		 setListAdapter(adapter);
		 
		 getLoaderManager().initLoader(0, null, this);
		 
		 getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> adapterview, View view,
						int position, long id) {
					
					// Toast.makeText(getActivity(), "Long press!", Toast.LENGTH_SHORT).show();
					RelativeLayout layout = (RelativeLayout) view;
					 TextView linkview = (TextView) layout.getChildAt(1); // get textview of link
					 itemlinktodelete = linkview.getText().toString();
				
					 if(mActionMode != null){
						 mActionMode.finish();
					 }
					 
					 if(mActionMode == null){
				        // Start the CAB using the ActionMode.Callback defined above
				        mActionMode = getSherlockActivity().startActionMode( mActionModeCallback);
				        view.setSelected(true);  
					 }
					 return true;
				}
		    });
	 }
	 
	 @Override
	 public void onResume()
	 {
		 super.onResume();
		 
		//disable up button in action bar for this fragment 
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		
		// refresh list on anytime feed list gets back focus
		refreshFeedListFromDatabase();
		
		// register content observer
		getActivity().getContentResolver().registerContentObserver(FeedContentProvider.CONTENT_URI_RSS, true, feedListObserver);
		 
		//define receiver for refreshing feed list
		IntentFilter filter = new IntentFilter(RssDefines.REFRESH_FEED_LIST);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
	 }
	 
	 @Override
	 public void onPause()
	 {
		 super.onPause();
		 LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
		 
		 // unregister content observer
		 getActivity().getContentResolver().unregisterContentObserver(feedListObserver);
	 }
	 
	 @Override
	 public void onListItemClick(ListView lv, View v, int position, long id)
	 {
		 Intent intent = new Intent(RssDefines.OPEN_DETAIL_FRAGMENT);
		 
		 // put the link as extra data so the main activity knows feed
		 intent.putExtra(RssDefines.EXTRA_DATA_DETAILS_ID, id);
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
				 refreshFeedListFromDatabase();
		 }
	 }
	 
	 public void refreshFeedListFromDatabase()
	 {
		 getLoaderManager().restartLoader(0, null, this);
	 }
	 
	 public Loader<Cursor> onCreateLoader(int id, Bundle args) {
	        Uri baseUri = FeedContentProvider.CONTENT_URI_RSS;
	        return new CursorLoader(getActivity(), baseUri, new String[]{RssFeedTable.COLUMN_ID, RssFeedTable.COLUMN_TITLE, RssFeedTable.COLUMN_LINK},
	        		null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
