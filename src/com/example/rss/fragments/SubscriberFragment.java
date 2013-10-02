package com.example.rss.fragments;


import com.actionbarsherlock.app.SherlockFragment;
import com.example.rss.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SubscriberFragment extends SherlockFragment {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		
		
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.subscribe_to_feed_fragment, container, false);
    }

}
