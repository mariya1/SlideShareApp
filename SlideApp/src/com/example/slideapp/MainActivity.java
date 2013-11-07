package com.example.slideapp;

import java.util.Arrays;
import java.util.Calendar;

import com.example.weatherapp.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	TextView date;
	public final static String USER_SYMBOL = "com.example.myfirstapp.user";
	private SharedPreferences userEntered;
	private TableLayout userScrollView;
	private EditText userEditText;
	Button findB;
	Button deleteB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		date= (TextView) findViewById(R.id.dateTextView);	
		Calendar c = Calendar.getInstance();
	    String sDate = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH)+ 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
		date.setText(sDate);
		
		userEntered = getSharedPreferences("user", MODE_PRIVATE);
		userScrollView = (TableLayout) findViewById(R.id.userTableScrollView);
		userEditText = (EditText) findViewById(R.id.enterEditText);
		findB = (Button) findViewById(R.id.find);
	    findB.setOnClickListener(findButtonListener);
	    deleteB=(Button) findViewById(R.id.deleteButton);
	    deleteB.setOnClickListener(deleteButtonListener);
		update(null);
	}
	
	private void update (String newuser){
		String[] users = userEntered.getAll().keySet().toArray(new String[0]);
		Arrays.sort(users, String.CASE_INSENSITIVE_ORDER);
		if(newuser != null){
			insertInScrollView(newuser, Arrays.binarySearch(users, newuser));
			
		} else {
			
			for(int i = 0; i < users.length; ++i){				
			insertInScrollView(users[i], i);				
			}			
		}		
	}
	
	private void saveuser (String newuser){
		String userNew = userEntered.getString(newuser, null);
		SharedPreferences.Editor preferencesEditor = userEntered.edit();
		preferencesEditor.putString(newuser, newuser);
		preferencesEditor.apply();
		
		if(userNew == null){
			update(newuser);
		}
		
	}
	
	private void insertInScrollView(String loc, int arrayIndex){
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View newRow = inflater.inflate(R.layout.user_row, null);
		TextView newuserTextView = (TextView) newRow.findViewById(R.id.userId);
		newuserTextView.setText(loc);		
		Button checkButton = (Button) newRow.findViewById(R.id.check);
	    checkButton.setOnClickListener(getCheckListener);		
		// Button deleteButton = (Button) newRow.findViewById(R.id.delete);
	    // deleteButton.setOnClickListener(getDeleteListener);		
		userScrollView.addView(newRow, arrayIndex);
		
	}
	
	public OnClickListener findButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(userEditText.getText().length() > 0){
				saveuser(userEditText.getText().toString());
				
				userEditText.setText(""); 
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(userEditText.getWindowToken(), 0);
			} 
			else {				
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("The entry is empty");				
				builder.setPositiveButton("OK", null);
				builder.setMessage("Please try again!");
				AlertDialog theAlertDialog = builder.create();
				theAlertDialog.show();				
			}
		}			
	};
	
	private void deleteAllStocks(){
		userScrollView.removeAllViews();
		
	}
	
	public OnClickListener deleteButtonListener = new OnClickListener(){
		
		public void onClick(View v) {			
			deleteAllStocks();			
			SharedPreferences.Editor preferencesEditor = userEntered.edit();
			preferencesEditor.clear();
			preferencesEditor.apply();
			
		}
		
	};
	
	public OnClickListener getCheckListener = new OnClickListener () {

		@Override
		public void onClick(View v) {
			TableRow tableRow = (TableRow) v.getParent();
			TextView userTextView = (TextView)tableRow.findViewById(R.id.userId);
			String userSymbol = userTextView.getText().toString();
			Intent intent = new Intent (MainActivity.this, UserInfoActivity.class);
			intent.putExtra (USER_SYMBOL, userSymbol);
			startActivity(intent);
			
		}};
	
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
	
	}

}
