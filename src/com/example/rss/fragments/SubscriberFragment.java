package com.example.rss.fragments;


import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.rss.R;
import com.example.rss.persistance.RssDefines;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SubscriberFragment extends SherlockFragment {
	
	@Override
	public void onStart(){
		super.onStart();
		
		//get up button in action bar for this fragment
		 getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Button button = (Button) getView().findViewById(R.id.subscribe_ok_button);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//hide keyboard
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getView().findViewById(R.id.subscribe_url_input).getWindowToken(), 0);
				
				EditText edtxt=(EditText) getView().findViewById(R.id.subscribe_url_input);
				String urlstring = edtxt.getEditableText().toString();
				Intent in = new Intent(RssDefines.ADD_RSS_FEED);
				in.putExtra("urlstring", urlstring);
				System.out.println("ADD RSS FEED build");
				LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(in);
				System.out.println("ADD RSS FEED sent");
			}
		});

	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		EditText inputText = (EditText) getActivity().findViewById(R.id.subscribe_url_input);
		//remove text from message and text if EditText gets clicked
		inputText.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				TextView message = (TextView) getActivity().findViewById(R.id.subscribe_tv_message);
				message.setText("");
			}
		});
	}
	
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
        menu.removeItem(R.id.action_refresh);
    }
	
	
}
