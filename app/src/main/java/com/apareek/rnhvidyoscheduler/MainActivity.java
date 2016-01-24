package com.apareek.rnhvidyoscheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.apareek.rnhvidyoscheduler.SchedulerConstants.*;


public class MainActivity extends FragmentActivity {

	SectionsPagerAdapter mSectionsPagerAdapter;
	private SharedPreferences userPrefs;
	private Intent i;
	private User u;
	private TestClient testClient;
	private NetworkConnectionManager netConMan;
	private ActionBar actionBar;
	private SchedulePoller poller;
	private Thread schedulePollerThread;
	
	private Handler h = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch(msg.what) {
				case SchedulerConstants.NO_NETWORK:
					Toast.makeText(getApplicationContext(), "No Network found", Toast.LENGTH_LONG).show();
					break;
				case SchedulerConstants.FOUND_NETWORK:
					Toast.makeText(getApplicationContext(), "Network found", Toast.LENGTH_LONG).show();
					break;
			}
		}
	};
	
	ViewPager mViewPager;
	private static final int[] layouts = {R.layout.fragment_schedule_vc_form_v_6, 
		R.layout.my_vc_page, R.layout.all_vc_page};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
/*		
		SQLiteOpenHelper mySqLiteOpenHelper = new MySQLiteHelper(this);
		SQLiteDatabase mySqLiteDatabase = mySqLiteOpenHelper.getWritableDatabase();
*/
		
		poller = SchedulePoller.getInstance(this);
		schedulePollerThread = new Thread(poller);
		schedulePollerThread.start();

		//Connection status?
	//	netConMan = NetworkConnectionManager.getInstance(this, h);
	//	netConMan.setName("NetConMan");
	//	netConMan.start();
		
		// Start the client
	//	testClient = TestClient.getInstance();
	//	testClient.setName("Client");
	//	testClient.start();
		
		// Start the queue
		//queue = MyQueue.getInstance();
		
		//Action Bar!
		// Don't overlay
		//getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		actionBar = getActionBar();
		Log.v("Action Bar",  String.valueOf(actionBar.isShowing()));

		actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.steelblue));
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		String[] locations = getResources().getStringArray(R.array.locations);
		ArrayList<String> locationArrayList = new ArrayList<String>();
		locationArrayList.addAll(Arrays.asList(locations));
		
		ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, locationArrayList);
		
		u = new User();
		
		/*
		 * Launch SignInActivity if no prefs are found
		 */
		readPrefs();
		
		if(u.getName().length() == 0) {
			i = new Intent(MainActivity.this, SignInActivity.class);
			startActivityForResult(i, 1);
		}
		else {
			init();
		}
	}

	private void init() {
		setContentView(R.layout.main);
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}

	private void readPrefs() {
		userPrefs = getSharedPreferences(SchedulerConstants.PREFS_NAME, MODE_PRIVATE);
		
		u.setName(userPrefs.getString(SchedulerConstants.USERNAME, ""));
	}

	private void createPrefs(User u) {
		userPrefs = getSharedPreferences(SchedulerConstants.PREFS_NAME, MODE_PRIVATE);
		SharedPreferences.Editor editor = userPrefs.edit();
		
		editor.putString(SchedulerConstants.USERNAME, u.getName());
		editor.putString(SchedulerConstants.PASSWORD, u.getPassword());
		editor.putString(SchedulerConstants.FNAME, u.getFName());
		editor.putString(SchedulerConstants.LNAME, u.getLName());
		editor.putString(SchedulerConstants.EMAIL, u.getEmail());
		editor.putString(SchedulerConstants.DEPARTMENT, u.getDepartment());
		editor.putInt(SchedulerConstants.USERID, u.getUserId());

		editor.commit();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 1) {
			if(resultCode == RESULT_OK) {
				// User name
				u.setName(data.getExtras().getString("username"));
				// Email
				u.setEmail(u.getName());
				// Password
				u.setPassword(data.getExtras().getString("password"));
				
				/*
				 * Launch UserDetailsActivity
				 */
				i = new Intent(MainActivity.this, UserDetailsActivity.class);
				startActivityForResult(i, 2);	
			}
		}
		else if(requestCode == 2) {
			if(resultCode == RESULT_OK) {
				// First name
				u.setFName(data.getExtras().getString("fname"));
				// Last name
				u.setLName(data.getExtras().getString("lname"));
				// User ID
				u.setUserId(data.getExtras().getInt("userid"));
				// Department
				u.setDepartment(data.getExtras().getString("department"));
				
				Toast.makeText(this.getBaseContext(), 
						"From UserDetailsActivity - Success", Toast.LENGTH_LONG)
						.show();
			}
			else if(resultCode == RESULT_CANCELED) {
				Toast.makeText(this.getBaseContext(), 
						"From UserDetailsActivity - Skipped", Toast.LENGTH_LONG)
						.show();				
			}
			
			createPrefs(u);
			
			init();
		}
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			
			int layout = 0;
			Fragment fragment = null;
			Bundle args = new Bundle();
			switch(position) {
				case 0:
					layout = layouts[position];
					fragment = new MyVCSchedulerFragment();
					args.putInt(MyVCSchedulerFragment.fragmentLayout, layout);
					fragment.setArguments(args);
					break;
				case 1:
					layout = layouts[position];
					fragment = new MyVCListFragment();
					args.putInt(MyVCSchedulerFragment.fragmentLayout, layout);
					fragment.setArguments(args);
					break;
				case 2:
					layout = layouts[position];
					fragment = new AllVCListFragment();
					args.putInt(MyVCSchedulerFragment.fragmentLayout, layout);
					fragment.setArguments(args);
					break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		switch(item.getItemId()) {
		case R.id.action_refresh:
			Toast.makeText(getApplicationContext(), "refresh", Toast.LENGTH_LONG).show();
			break;
		case R.id.action_settings:
			break;
		case R.id.clear_prefs:
			userPrefs.edit().clear().commit();
			Toast.makeText(getApplicationContext(), "Preferences cleared", Toast.LENGTH_LONG).show();
			break;	
		}
		return true;
	}
	
	
}
