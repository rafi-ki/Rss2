package com.example.rss.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.rss.R;
import com.example.rss.persistance.RssDefines;

public class DetailList extends SherlockFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		TextView te = (TextView) getView().findViewById(R.id.detail_fragment_view);
		Bundle b = getArguments();
		String link =b.getString(RssDefines.EXTRA_DATA_DETAILS_LINK);
		te.setText(link);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.detail_fragment, container, false);
    }
}
