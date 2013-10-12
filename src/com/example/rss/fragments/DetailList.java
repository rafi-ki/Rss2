package com.example.rss.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.example.rss.R;
import com.example.rss.persistance.FeedContentProvider;
import com.example.rss.persistance.FeedItemTable;
import com.example.rss.persistance.RssDefines;

public class DetailList extends SherlockListFragment
	implements LoaderManager.LoaderCallbacks<Cursor> {	
	
	private SimpleCursorAdapter adapter;
	private long rssFeedId;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//get up button in action bar for this fragment
		 getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		Bundle b = getArguments();
		rssFeedId = b.getLong(RssDefines.EXTRA_DATA_DETAILS_ID);
		
		String[] from = {FeedItemTable.COLUMN_TITLE, FeedItemTable.COLUMN_LINK, FeedItemTable.COLUMN_DESCRIPTION};
		int [] to = {R.id.detail_lv_item_title, R.id.detail_lv_item_link, R.id.detail_lv_item_description};
		adapter = new SimpleCursorAdapter(getActivity(), R.layout.detail_lv_item, null, from, to, 0);
		setListAdapter(adapter);
		
		getLoaderManager().initLoader(1, null, this);
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
