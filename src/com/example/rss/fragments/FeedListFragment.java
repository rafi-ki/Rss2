package com.example.rss.fragments;




import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import com.actionbarsherlock.app.SherlockListFragment;
import com.example.rss.R;
import com.example.rss.persistance.FeedContentProvider;
import com.example.rss.persistance.FeedManager;
import com.example.rss.persistance.RssDefines;
import com.example.rss.persistance.RssFeedTable;
import com.example.rss.services.FeedLoaderService;

public class FeedListFragment extends SherlockListFragment 
	implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private FeedManager feedmanager;
	private RefreshFeedListReceiver receiver;
	
	private SimpleCursorAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		System.out.println("FeedList-Fragment created");
		
		
		feedmanager= FeedManager.getInstance();
		receiver = new RefreshFeedListReceiver();
		
		//define receiver for refreshing feed list
		IntentFilter filter = new IntentFilter(RssDefines.REFRESH_FEED_LIST);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
		 
		Intent intent = new Intent(getActivity(), FeedLoaderService.class);
		getActivity().startService(intent);
		 
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
					 final String itemlink = linkview.getText().toString();
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

					builder.setMessage(R.string.list_item_delet_confirm_message).setTitle(R.string.list_item_delet_confirm_title);
					builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   feedmanager.delFeed(itemlink);
				        	   
				        	   Intent in = new Intent(RssDefines.REFRESH_FEED_LIST);
				        	   LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(in);
				           }
				       });
					builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				               // User cancelled the dialog so do nothing
				           }
				       });

					AlertDialog dialog = builder.create();
					dialog.show();
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
		 
		//define receiver for refreshing feed list
		IntentFilter filter = new IntentFilter(RssDefines.REFRESH_FEED_LIST);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
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
