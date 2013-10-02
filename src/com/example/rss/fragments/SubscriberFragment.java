package com.example.rss.fragments;


import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.rss.R;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

public class SubscriberFragment extends SherlockFragment {
	
/*	@Override
	public void onStart(){
		super.onStart();
		Button button = (Button) getView().findViewById(R.id.subscribe_ok_button);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//hide keyboard
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getView().findViewById(R.id.subscribe_url_input).getWindowToken(), 0);
			}
		});

	}*/
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.subscribe_to_feed_fragment, container, false);
    }
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.removeItem(R.id.action_subscribe);
    }
}
