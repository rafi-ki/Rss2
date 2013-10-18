package com.example.rss.fragments;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.rss.R;
import com.example.rss.observer.FeedItemObserver;
import com.example.rss.persistance.FeedContentProvider;
import com.example.rss.persistance.FeedDatabase;
import com.example.rss.persistance.FeedItemTable;
import com.example.rss.persistance.RssDefines;

public class DetailList extends SherlockListFragment
	implements LoaderManager.LoaderCallbacks<Cursor> {	
	
	private SimpleCursorAdapter adapter;
	private long rssFeedId;
	private ContentObserver detailListObserver;
	private RefreshDetailListReceiver receiver;
	
	private ListView listView;
	
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
               return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode.finish();
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
		
		listView = this.getListView();
		
		String[] from = {FeedItemTable.COLUMN_TITLE, FeedItemTable.COLUMN_LINK, FeedItemTable.COLUMN_DESCRIPTION, FeedItemTable.COLUMN_STARRED_STATE, FeedItemTable.COLUMN_READ_STATE};
		int [] to = {R.id.detail_lv_item_title, R.id.detail_lv_item_link, R.id.detail_lv_item_description, R.id.detail_lv_item_starred, R.id.detail_lv_item_read};
		adapter = new SimpleCursorAdapter(getActivity(), R.layout.detail_lv_item, null, from, to, 0);
		setListAdapter(adapter);
		listView.setAdapter(adapter);
		
		adapter.setViewBinder(new ViewBinder(){
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				switch(view.getId())
				{
				case R.id.detail_lv_item_starred:
				{
					if (cursor.getInt(columnIndex) == 0) // 0 => not starred, 1 => starred
						view.setVisibility(View.INVISIBLE);
					else
						view.setVisibility(View.VISIBLE);
					return true;
				}
				case R.id.detail_lv_item_read:
					if (cursor.getInt(columnIndex) == 1) // 0 => not read, 1 => read
						view.setVisibility(View.VISIBLE);
					else
						view.setVisibility(View.INVISIBLE);
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
		
		enableMultiSelect();
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void enableMultiSelect() {
		 final List<Integer> idlist= new ArrayList<Integer>();
		 listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		 listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

			@Override
			public boolean onActionItemClicked(android.view.ActionMode mode,
					android.view.MenuItem item) {
				long ids[];
				switch (item.getItemId()) {
            	
	            case R.id.action_detail_list_read:
	            	ids = listView.getCheckedItemIds();
	            	for (int i=0; i<ids.length; i++)
	            		FeedDatabase.markFeedItemAsRead(getActivity(), ids[i]);
//	            	Toast.makeText(getActivity(), "READ BITCH: "+ listView.getCheckedItemCount(), Toast.LENGTH_SHORT).show();
	            	mode.finish();
	            	return true;
	            	
	            case R.id.action_detail_list_star:
	            	ids = listView.getCheckedItemIds();
	            	for (int i=0; i<ids.length; i++)
	            		FeedDatabase.markFeedItemAsStarred(getActivity(), ids[i]);
//	            	Toast.makeText(getActivity(), "STAR BITCH "+ listView.getCheckedItemCount(), Toast.LENGTH_SHORT).show();
	            	mode.finish();
	            	return true;
	            case R.id.action_detail_list_unread:
	            	ids = listView.getCheckedItemIds();
	            	for (int i=0; i<ids.length; i++)
	            		FeedDatabase.markFeedItemAsUnread(getActivity(), ids[i]);
	            	mode.finish();
	            	return true;
	            default:
	            	return false;
				}
			}

			@Override
			public boolean onCreateActionMode(android.view.ActionMode mode,
					android.view.Menu menu) {
				android.view.MenuInflater inflater = mode.getMenuInflater();
		        inflater.inflate(R.menu.detail_list_action_menu, menu);
		        return true;
			}

			@Override
			public void onDestroyActionMode(android.view.ActionMode mode) {
////				for(int pos: idlist){
//            		View v = getListView().getChildAt(pos);
//					v.setBackgroundColor(0x00000000);
//            	}
         	idlist.clear();
			}

			@Override
			public boolean onPrepareActionMode(android.view.ActionMode mode,
					android.view.Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onItemCheckedStateChanged(android.view.ActionMode mode,
					int position, long id, boolean checked) {
				int checkedCount = getListView().getCheckedItemCount();
				if (checkedCount != 0)
					mode.setTitle(checkedCount + "");
				
				if(checked)
				{	
					System.out.println("Position in List selected: "+position);
					System.out.println("Id in List selected: " + id);
					idlist.add(Integer.valueOf(position));
				}
				else{
					System.out.println("Position in List deselected: "+position);
					idlist.remove(Integer.valueOf(position));
				}
			}
		 });
	}

	
	public void onResume()
	{
		super.onResume();
		
		// register content observer
		getActivity().getContentResolver().registerContentObserver(FeedContentProvider.CONTENT_URI_FEED_ITEM, true, detailListObserver);
		
		//define receiver for refreshing feed list
		IntentFilter filter = new IntentFilter(RssDefines.REFRESH_DETAIL_LIST);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
	}
	
	public void onPause()
	{
		super.onPause();
		
		getActivity().getContentResolver().unregisterContentObserver(detailListObserver);
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
