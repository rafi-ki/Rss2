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
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.rss.R;
import com.example.rss.observer.FeedItemObserver;
import com.example.rss.persistance.FeedContentProvider;
import com.example.rss.persistance.FeedItemTable;
import com.example.rss.persistance.RssDefines;

public class DetailList extends SherlockListFragment
	implements LoaderManager.LoaderCallbacks<Cursor> {	
	
	private SimpleCursorAdapter adapter;
	private long rssFeedId;
	private ContentObserver detailListObserver;
	private RefreshDetailListReceiver receiver;
	
	private ActionMode mActionMode;
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback(){

		@Override
	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	        // Inflate a menu resource providing context menu items
	        MenuInflater inflater = mode.getMenuInflater();
	        inflater.inflate(R.menu.detail_list_action_menu, menu);
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
            case R.id.action_detail_list_delete:
            	Toast.makeText(getActivity(), "DELETE BITCH!!!", Toast.LENGTH_SHORT).show();
                mActionMode.finish();
            	return true;
            	
            case R.id.action_detail_list_read:
            	Toast.makeText(getActivity(), "IT GONNA BE READ BITCH!!!", Toast.LENGTH_SHORT).show();
                mActionMode.finish();
            	return true;
            	
            case R.id.action_detail_list_star:
            	Toast.makeText(getActivity(), "OH YEAH A STAR BITCH!!!", Toast.LENGTH_SHORT).show();
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
		
		//get up button in action bar for this fragment
		 getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
		 
		 detailListObserver = new FeedItemObserver(new Handler(), this.getActivity());
		 receiver = new RefreshDetailListReceiver();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		Bundle b = getArguments();
		rssFeedId = b.getLong(RssDefines.EXTRA_DATA_DETAILS_ID);
		
		String[] from = {FeedItemTable.COLUMN_TITLE, FeedItemTable.COLUMN_LINK, FeedItemTable.COLUMN_DESCRIPTION, FeedItemTable.COLUMN_STARRED_STATE, FeedItemTable.COLUMN_READ_STATE};
		int [] to = {R.id.detail_lv_item_title, R.id.detail_lv_item_link, R.id.detail_lv_item_description, R.id.detail_lv_item_starred, R.id.detail_lv_item_background};
		adapter = new SimpleCursorAdapter(getActivity(), R.layout.detail_lv_item, null, from, to, 0);
		setListAdapter(adapter);
		
		adapter.setViewBinder(new ViewBinder(){
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				switch(view.getId())
				{
				case R.id.detail_lv_item_starred:
				{
					// use starred textview for indicating starred state
					TextView tv = (TextView) view;
					if (cursor.getInt(columnIndex) == 0) // 0 => not starred, 1 => starred
						tv.setText("not starred");
					else
						tv.setText("starred");
					return true;
				}
				case R.id.detail_lv_item_background:
					// use background for indicating read state
					if (cursor.getInt(columnIndex) == 0) // 0 => not read, 1 => read
						view.setBackgroundResource(R.color.gray);
					return true;
				default:
					return false; // not handled within this method
				}
			}
		});
		
		getLoaderManager().initLoader(1, null, this);
		
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterview, View view,
					int position, long id) {
				
			
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
	
	public void onResume()
	{
		super.onResume();
		
		//define receiver for refreshing feed list
		IntentFilter filter = new IntentFilter(RssDefines.REFRESH_DETAIL_LIST);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
	}
	
	public void onPause()
	{
		super.onPause();
		
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
	}
	
	public void refreshDetailListFromDatabase()
	{
		getLoaderManager().restartLoader(1, null, this);
	}
	 
	 @Override
	 public void onListItemClick(ListView lv, View v, int position, long id)
	 {
		 RelativeLayout layout = (RelativeLayout) v;
		 TextView linkview = (TextView) layout.getChildAt(1); // get textview of link
		 String link = linkview.getText().toString();
		 Intent intent = new Intent(Intent.ACTION_VIEW);
		 intent.setData(Uri.parse(link));
		 startActivity(intent);
	 }
	 
	 private class RefreshDetailListReceiver extends BroadcastReceiver
	 {
		 @Override
		 public void onReceive(Context context, Intent intent) {
			 if (intent.getAction().equals(RssDefines.REFRESH_DETAIL_LIST))
				 refreshDetailListFromDatabase();
		 }
	 }

	 @Override
	 public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		 Uri baseUri = FeedContentProvider.CONTENT_URI_FEED_ITEM;
		 return new CursorLoader(getActivity(), baseUri, new String[]{FeedItemTable.COLUMN_ID, FeedItemTable.COLUMN_TITLE, FeedItemTable.COLUMN_LINK,
			 FeedItemTable.COLUMN_DESCRIPTION, FeedItemTable.COLUMN_READ_STATE, FeedItemTable.COLUMN_STARRED_STATE},
			 FeedItemTable.COLUMN_RSSFEED_ID + "=" + rssFeedId, null, null);
	 }

	 @Override
	 public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		 adapter.swapCursor(cursor);
	 }

	 @Override
	 public void onLoaderReset(Loader<Cursor> loader) {
		 adapter.swapCursor(null);
	 }
}
