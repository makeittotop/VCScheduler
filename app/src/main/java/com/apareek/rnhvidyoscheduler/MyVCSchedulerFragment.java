package com.apareek.rnhvidyoscheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.Inflater;

import com.thoughtworks.xstream.XStream;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.Settings.System;
import android.sax.RootElement;
import android.support.v4.app.Fragment;
import android.text.AutoText;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MyVCSchedulerFragment extends Fragment implements OnClickListener, OnItemSelectedListener {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String fragmentLayout = "fragmentLayout";
	
	private static final String[] strDaysOfTheWeek = {
		"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	
	ArrayAdapter<String> confDataAdapter = null;
	ArrayAdapter<String> vidyoDataAdapter = null;
	
	private Button dateButton;
	private Button timeButton;
	private Button calendarButton;
	private CheckBox nowCheckBox;
	private EditText meetingNameEditText;
	
	private Button hourButton;
	private Button minuteButton;
	
	private Button timeZoneButton;
	private Button repeatButton;
	
//	private Spinner confRoomSpinner;
	private Button confRoomButton;
	private Button vidyoRoomButton;
	
	private Button nextButton;
	private Button clearButton;
	
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private String dayOfTheWeek;
	
	private int curYear;
	private int curMonth;
	private int curDay;
	private int curHour;
	private int curMinute;
	private String curDayOfTheWeek;
	
	private StringBuilder am_pm;
	
	private String[] strHYDConfRooms;
	private String[] strMUMConfRooms;	
	private String[] strLAConfRooms;
	private String[] strVANConfRooms;
	private String[] strKAOConfRooms;
	private String[] strConfRooms;
	private String[] strVidyoRooms;
	private String[] strRepeatArray;
	private String[] strTimeZoneArray;
	
	private Calendar c = null;

	/*
	private String strConfRoomSpinner = 
			SchedulerConstants.SELECT_ROOM;
	*/
	private Resources res;
	
	private Drawable confRoomButtonDrawable;
	private Drawable vidyoRoomButtonDrawable;
	private String strConfRoomButton;
	private String strVidyoRoomButton;
	private String strRequestType;
	private long sTime;
	private long eTime;
	private int intHour;
	private int intMinute;
	private String strTimeZone;
	private String strRepeat;
	private boolean boolNowCheckBox;
	private String strMeetingName;
	
	private AlertDialog ad;
	
	// Search in vidyo room list view
	private EditText vidyoSearchRoomEditText;
	private ListView vidyoRoomListView;
	
	private List<String> listVidyoRooms;
	private List<String> originalListVidyoRooms;
	private List<String> listConfRooms;
	private ArrayList<String> searchResults = new ArrayList<String>();
	private ActionBar actionBar;
	
	private MySQLiteHelper mySqLiteOpenHelper;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		int layout = getArguments().getInt(fragmentLayout);
		View rootView = inflater.inflate(layout, container, false);
		
				meetingNameEditText = (EditText) rootView.findViewById(R.id.meetingNameEditText);
				calendarButton = (Button) rootView.findViewById(R.id.calendarButton);
				dateButton = (Button) rootView.findViewById(R.id.dateButton);
				timeButton = (Button) rootView.findViewById(R.id.timeButton);
				nowCheckBox = (CheckBox) rootView.findViewById(R.id.nowCheckBox);
				
				calendarButton.setOnClickListener(this);
				dateButton.setOnClickListener(this);
				timeButton.setOnClickListener(this);
				nowCheckBox.setOnClickListener(this);
				
				// duration
				hourButton = (Button) rootView.findViewById(R.id.hourButton);
				minuteButton = (Button) rootView.findViewById(R.id.minuteButton);
				
				hourButton.setOnClickListener(this);
				minuteButton.setOnClickListener(this);
				
				// Go Button!
				nextButton = (Button) rootView.findViewById(R.id.nextButton);
				clearButton = (Button) rootView.findViewById(R.id.clearButton);
				
				nextButton.setOnClickListener(this);
				clearButton.setOnClickListener(this);
				
				repeatButton = (Button) rootView.findViewById(R.id.repeatButton);
				repeatButton.setOnClickListener(this);
				
				timeZoneButton = (Button) rootView.findViewById(R.id.timeZoneButton);
				timeZoneButton.setOnClickListener(this);
				
				// spinner
				//confRoomSpinner = (Spinner) rootView.findViewById(R.id.confRoomSpinner);
				confRoomButton = (Button) rootView.findViewById(R.id.confRoomButton);
				vidyoRoomButton = (Button) rootView.findViewById(R.id.vidyoRoomButton);
				
				//confRoomSpinner.setOnItemSelectedListener(this);
				confRoomButton.setOnClickListener(this);
				vidyoRoomButton.setOnClickListener(this);
				//vidyoRoomSpinner.setOnItemSelectedListener(this);
				
				res = getResources();
				confRoomButtonDrawable = confRoomButton.getBackground();
				vidyoRoomButtonDrawable = vidyoRoomButton.getBackground();
				
				init();
				initUi();
				
				/*
				confDataAdapter = new ConfRoomAdapter(rootView.getContext(), 
						android.R.layout.simple_list_item_1, listConfRooms);
				
				
				confDataAdapter = new ArrayAdapter<String>(rootView.getContext(),
						R.layout.myvcs_confspinnerrow, listConfRooms);
				
				confDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				confRoomSpinner.setAdapter(confDataAdapter);
				*/
				
				//vidyoDataAdapter = new ArrayAdapter<String>(rootView.getContext(),
				//		android.R.layout.simple_list_item_1, listVidyoRooms);
				//vidyoDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				//vidyoRoomSpinner.setAdapter(vidyoDataAdapter);
				//vidyoRoomSpinner.setBackgroundColor(Color.parseColor("#CDCDCD"));

		return rootView;
	}
	
	private void init() {
		
		//strHYDConfRooms = res.getStringArray(R.array.hyd_conference_room);
		//strMUMConfRooms = res.getStringArray(R.array.mum_conference_room);
		// DB query for HYD
		mySqLiteOpenHelper = new MySQLiteHelper(getActivity());
		strHYDConfRooms = mySqLiteOpenHelper.getConferenceRoomData(1); 
		strConfRooms = strHYDConfRooms;
		
		actionBar = getActivity().getActionBar();
		
		String[] locations = getResources().getStringArray(R.array.locations);
		ArrayList<String> locationArrayList = new ArrayList<String>();
		locationArrayList.addAll(Arrays.asList(locations));
		
		ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext()
				, android.R.layout.simple_dropdown_item_1line, locationArrayList);
		
		actionBar.setListNavigationCallbacks(dropDownAdapter, new ActionBar.OnNavigationListener() {
			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {
				Log.v("fragment location id", String.valueOf(itemPosition));
				
				int locId = itemPosition + 1;
				switch(locId) {
					case 0:
						strHYDConfRooms = mySqLiteOpenHelper.getConferenceRoomData(locId); 
						strConfRooms = strHYDConfRooms;
						break;
					case 1:
						strMUMConfRooms = mySqLiteOpenHelper.getConferenceRoomData(locId); 
						strConfRooms = strMUMConfRooms;
						break;
					case 2:
						strConfRooms = mySqLiteOpenHelper.getConferenceRoomData(locId);
						break;
					case 3:
						strConfRooms = strVANConfRooms;
						break;
					case 4:
						strConfRooms = strKAOConfRooms;
						break;
					default:
						break;
				}

				return false;
			}
		});
		
		c = Calendar.getInstance();
		
		strMeetingName = "";
		meetingNameEditText.setText(strMeetingName);
		
		strConfRoomButton = SchedulerConstants.SELECT_ROOM;
		strVidyoRoomButton = SchedulerConstants.SELECT_ROOM;
		
		boolNowCheckBox = false;
		strRequestType = "A";
		
		intHour=1;
		intMinute=0;

		//strVidyoRooms = res.getStringArray(R.array.vidyo_room);
		strVidyoRooms = mySqLiteOpenHelper.getVidyoRoomData();
		strRepeatArray = res.getStringArray(R.array.repeat);
		strTimeZoneArray = res.getStringArray(R.array.timezone);
		
		strRepeat = strRepeatArray[0];
		strTimeZone = strTimeZoneArray[0];
		
		listConfRooms = new ArrayList<String>();
		
		originalListVidyoRooms = new ArrayList<String>();
		originalListVidyoRooms.addAll(Arrays.asList(strVidyoRooms));
		
		mySqLiteOpenHelper = new MySQLiteHelper(getActivity());
		
		initCurrentDateTime();
	}
	
	private void initUi() {

		confRoomButton.setBackground(confRoomButtonDrawable);
		vidyoRoomButton.setBackground(vidyoRoomButtonDrawable);
		confRoomButton.setText(strConfRoomButton);
		vidyoRoomButton.setText(strVidyoRoomButton);
		
		nowCheckBox.setChecked(boolNowCheckBox);
		
		timeButton.setEnabled(true);
		
		hourButton.setText(String.valueOf(intHour));
		minuteButton.setText(String.valueOf(intMinute));
		
		repeatButton.setText(strRepeat);
		timeZoneButton.setText(strTimeZone);
		
		// Date / Time				
		dateButton.setText(new DateTimeFormatter(
				curDayOfTheWeek, curDay, curMonth, curYear).getDate());
		
		timeButton.setText(new DateTimeFormatter(
				curHour, curMinute, am_pm).getTime());
	}
	
	private void reinit() {
		init();
		initUi();
		
		/*
		// Conf room spinner
		
		strConfRoomSpinner = SchedulerConstants.SELECT_ROOM;
		confRoomSpinner.setPrompt(strConfRoomSpinner);
		
		
		strConfRoomButton = SchedulerConstants.SELECT_ROOM;
		confRoomButton.setText(strConfRoomButton);
		
		// Vidyo room chooser button
		strVidyoRoomButton = SchedulerConstants.SELECT_ROOM;
		vidyoRoomButton.setText(strVidyoRoomButton);
		
		// Current date
		initCurrentDateTime();
		
		dateButton.setText(new DateTimeFormatter(
				curDayOfTheWeek, curDay, curMonth, curYear).getDate());
		
		// Current time
		timeButton.setText(new DateTimeFormatter
				(curHour, curMinute, am_pm).getTime());
		
		
		// Time Zone
		timeZoneButton.setText(strTimeZone);
		
		// Now ?
		strRequestType = "A";
		nowCheckBox.setChecked(false);
		timeButton.setEnabled(true);
		
		// Duration
		intHour = 1;
		intMinute = 0;
		hourButton.setText(String.valueOf(intHour));
		minuteButton.setText(String.valueOf(intMinute));
		
		// Repeat
		repeatButton.setText(strRepeat);
		*/
	}
	
	private void initCurrentDateTime() {
		getCurrentDate();

		year = curYear;
		month = curMonth;
		day = curDay;
		hour = curHour;
		minute = curMinute;
	}
	
	private void getCurrentDate() {
		c = Calendar.getInstance();
		
		c.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
		
		curYear = c.get(Calendar.YEAR);
		curMonth = c.get(Calendar.MONTH);
		curDay = c.get(Calendar.DAY_OF_MONTH);
		curHour = c.get(Calendar.HOUR);
		curMinute = c.get(Calendar.MINUTE);
		curDayOfTheWeek = strDaysOfTheWeek[c.get(Calendar.DAY_OF_WEEK) - 1];
		
		am_pm = c.get(Calendar.AM_PM) != 0 ? new StringBuilder().append(" PM") :
			new StringBuilder().append(" AM");
		
		return;
	}
	
	private long convertToEpoch() {
		Calendar c = Calendar.getInstance();
		
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR, hour);
		c.set(Calendar.MINUTE, minute);
		
		Log.v("epoch", String.valueOf(c.getTime().getTime()));
		return c.getTime().getTime();
	}
	
	private String convertFromEpoch() {
		Date date = new Date(c.getTime().getTime());
	    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    
	    format.setTimeZone(TimeZone.getTimeZone("GMT + 5:30"));
	    
	    return format.format(date);
	}
	
	@Override
	public void onClick(View v) {
		DateTimeDialogFragment dateTimepicker=null;
		NumberPickerCustomDialogFragment numberPicker=null;
		CalendarPickerCustomDialogFragment calendarPicker=null;
		
		switch(v.getId()) {
			case R.id.confRoomButton:
				new AlertDialog.Builder(getActivity())
				.setTitle("Conference room")
				.setSingleChoiceItems(strConfRooms, -1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						strConfRoomButton = strConfRooms[which];
						
						confRoomButton.setBackground(confRoomButtonDrawable);
						confRoomButton.setText(strConfRoomButton);
						dialog.dismiss();
					}
				})
				.show();
				break;
			case R.id.vidyoRoomButton:
				View vidyoRoomView = null;
				LayoutInflater layoutInflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				vidyoRoomView = layoutInflater.inflate(R.layout.vidyoroomlist, null);
				vidyoRoomView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
					}
				});
				
				vidyoSearchRoomEditText = (EditText) vidyoRoomView
						.findViewById(R.id.vidyoSearchRoomEditText);
				vidyoSearchRoomEditText.setOnClickListener(this);
				
				vidyoSearchRoomEditText.addTextChangedListener(new TextWatcher() {
					
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						
						int textLength = vidyoSearchRoomEditText.getText().length();
						String searchString = vidyoSearchRoomEditText.getText().toString();
						
						searchResults.clear();
						
						for(int i=0; i < strVidyoRooms.length; i++) {
							String vidyoRoom = strVidyoRooms[i];
							if(textLength < vidyoRoom.length()) {
								if(searchString.equalsIgnoreCase(
										vidyoRoom.substring(0, textLength))) {
									searchResults.add(vidyoRoom);
								}
							}
						}
						
						listVidyoRooms.clear();
						listVidyoRooms.addAll(searchResults);
						vidyoDataAdapter.notifyDataSetChanged();
				
					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub
						
					}
				});
				
				vidyoRoomListView = (ListView) vidyoRoomView
						.findViewById(R.id.vidyoRoomListView);

				listVidyoRooms = new ArrayList<String>(originalListVidyoRooms);
				vidyoDataAdapter = new ArrayAdapter<String>(vidyoRoomView.getContext(), 
						android.R.layout.simple_expandable_list_item_1, listVidyoRooms);
				
				vidyoRoomListView.setAdapter(vidyoDataAdapter);
				vidyoRoomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View child,
							int position, long id) {
						String room = listVidyoRooms.get(position);
						Log.v("Str", room);
						strVidyoRoomButton = room;
						
						vidyoRoomButton.setBackground(vidyoRoomButtonDrawable);
						vidyoRoomButton.setText(strVidyoRoomButton);
						
						if(!strVidyoRoomButton.equalsIgnoreCase(SchedulerConstants.SELECT_ROOM)) {
							//vidyoRoomButton.setBackground(null);
						}
						ad.dismiss();
					}
				});
								
				ad = new AlertDialog.Builder(getActivity())
				.setView(vidyoRoomView)
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Log.v("foo", "bar");				
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				})
				.show();
				break;
			case R.id.dateButton:
                dateTimepicker=new DateTimeDialogFragment("date", dateButton);
                dateTimepicker.show(getActivity().getFragmentManager(), "date");
			break;
			case R.id.timeButton:
				Log.v("foo", "here");
				dateTimepicker = new DateTimeDialogFragment("time", timeButton);
				dateTimepicker.show(getActivity().getFragmentManager(), "fromTime");
			break;
			case R.id.nowCheckBox:
				if(nowCheckBox.isChecked()) {
					timeButton.setClickable(false);
					timeButton.setEnabled(false);
					initCurrentDateTime();
					
		    		StringBuilder strTime = new DateTimeFormatter(curHour, curMinute, am_pm).getTime();
		    		timeButton.setText(strTime);
		    		strRequestType = "N";
				}
				else {
					timeButton.setClickable(true);
					timeButton.setEnabled(true);
					strRequestType = "A";
				}
			break;
			case R.id.hourButton:
				numberPicker = new NumberPickerCustomDialogFragment("Hour", hourButton, 0, 10);
				numberPicker.show(getActivity().getFragmentManager(), "hour");
				break;
			case R.id.minuteButton:
				numberPicker = new NumberPickerCustomDialogFragment("Minute", minuteButton, 0, 59);
				numberPicker.show(getActivity().getFragmentManager(), "minute");
				break;
			case R.id.calendarButton:
				calendarPicker = new CalendarPickerCustomDialogFragment(dateButton);
				calendarPicker.show(getActivity().getFragmentManager(), "Calendar");
				break;
			case R.id.nextButton:
				boolean brk = false;
				/*
				if(strConfRoomSpinner.equalsIgnoreCase(SchedulerConstants.SELECT_ROOM)) {
					confRoomSpinner.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.margin_border));
					brk = true;
				}
				*/
				
				if(strConfRoomButton.equalsIgnoreCase(SchedulerConstants.SELECT_ROOM)) {
					Toast.makeText(getActivity(), "Select a room", Toast.LENGTH_LONG).show();
					confRoomButton.setBackgroundColor(Color.RED);
					//confRoomButton.setTextColor(Color.BLACK);
					brk = true;
				}
				
				if(strVidyoRoomButton.equalsIgnoreCase(SchedulerConstants.SELECT_ROOM)) {
					Toast.makeText(getActivity(), "Select a room", Toast.LENGTH_LONG).show();
					vidyoRoomButton.setBackgroundColor(Color.RED);
					//vidyoRoomButton.setTextColor(Color.BLACK);
					brk = true;				
				}
					
				if(brk)
					break;
				
				Vidyo vidyoData = collectData();
				
				Intent i = new Intent(getActivity(), MoreOptionsActivity.class);
				i.putExtra(SchedulerConstants.VIDYO_DATA_KEY, vidyoData);
				startActivityForResult(i, 1);
				break;
			case R.id.clearButton:
				reinit();
				break;
			case R.id.repeatButton:
				ad = new AlertDialog.Builder(getActivity())
				.setTitle("Repeat")
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.setSingleChoiceItems(strRepeatArray, -1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						repeatButton.setText(strRepeatArray[which]);
						dialog.dismiss();
					}
				})
				.show();
				ad.getListView().setBackgroundColor(getResources().getColor(R.color.MyDarkGrey));
				break;
			case R.id.timeZoneButton:
				ad = new AlertDialog.Builder(getActivity())
				.setTitle("Time Zone")
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.setSingleChoiceItems(strTimeZoneArray, -1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						timeZoneButton.setText(strTimeZoneArray[which]);
						dialog.dismiss();
					}
				})
				.show();
				ad.getListView().setBackgroundColor(getResources().getColor(R.color.MyDarkGrey));
				break;				
			default:
			break;
		}
	}	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == 1) {
			if(resultCode == Activity.RESULT_OK) {
				String serverResponse = data.getStringExtra("rid");
				Vidyo v = data.getExtras().getParcelable(SchedulerConstants.VIDYO_DATA_KEY);
								
				Vidyo vid = new XMLHandler().xmlToVidyo(serverResponse);
				
				Log.v("vidyo", vid.toString());
				
				int responseId = vid.getAction().getRid();
				String info = vid.getAction().getInfo();
				
				Intent i = callCalendarService(v);

				startActivity(i);
				
				Toast.makeText(getActivity(), "Request " + String.valueOf(responseId) +
						" added successfully!", Toast.LENGTH_LONG).show();
				
				reinit();
			}
			else if(resultCode == Activity.RESULT_CANCELED) {
				String serverResponse = data.getStringExtra("rid");
				Toast.makeText(getActivity(), serverResponse +
						". Please try again later!", Toast.LENGTH_LONG).show();
			}
			else if(resultCode == 22) {
				ArrayList<Integer> schedulerIdList = data.getExtras()
						.getIntegerArrayList("schedIdList");
				
				StringBuilder builder = new StringBuilder();
				
				for(int i = 0; i < schedulerIdList.size(); i++) {
					if(builder.length() != 0) {
						builder.append(",");
					}
					builder.append(schedulerIdList.get(i));
				}
				
				Toast.makeText(getActivity(), "Request " + builder +
						" added successfully!", Toast.LENGTH_LONG).show();
				
				reinit();
			}
		}
	}

	private Intent callCalendarService(Vidyo v) {
		String strEventTitle = v.getVc().getMeetingName() + "at VC room: " + v.getVc().getVidyoRoom();
		String strEventDescription = "My Meeting";
		String strEventLocation = v.getVc().getRoom();
		long eventStartTime = v.getTime().getStime() * 1000;
		long eventEndTime = v.getTime().getEtime() * 1000;
		String strEventExtraEmail = v.getUser().getEmail() + "@rhythm.com";
		String strParticipants = v.getNotify().getParticipants();
		
		Intent i = new Intent(Intent.ACTION_INSERT)
				.setData(Events.CONTENT_URI)
				.putExtra(Events._ID, "300")
				.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, eventStartTime)
				.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, eventEndTime)
				.putExtra(Events.TITLE, strEventTitle)
				.putExtra(Events.DESCRIPTION, strEventDescription)
				.putExtra(Events.EVENT_LOCATION, strEventLocation)
				.putExtra(Intent.EXTRA_EMAIL, strEventExtraEmail)
				.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_TENTATIVE);
		
		return i;
	}

	private Vidyo collectData() {
		Vidyo v = new Vidyo();
		
		// Location Id
		v.getVc().setLocationId(actionBar.getSelectedNavigationIndex() + 1);
		strMeetingName = meetingNameEditText.getText().toString();
		v.getVc().setMeetingName(strMeetingName);
		v.getVc().setRoom(strConfRoomButton);
		v.getVc().setVidyoRoom(strVidyoRoomButton);
		
		v.getType().setType(strRequestType);
		
		EpochHandler epochHandler = new EpochHandler(year, month, 
				day, minute, hour);
		
		sTime = epochHandler.getEpochTime();
		eTime = sTime + epochHandler.addHours(intHour) + epochHandler.addMinutes(intMinute);
		
		Log.v("sTime", String.valueOf(sTime));
		Log.v("eTime", String.valueOf(eTime));
		
		v.getTime().setStime(sTime);
		v.getTime().setEtime(eTime);
		
		return v;
	}

	public class DateTimeDialogFragment extends DialogFragment implements OnDateSetListener, OnTimeSetListener {
		private Button btn;
		private String type;
		private Dialog dateTimeDialog;
		
		public DateTimeDialogFragment(String t, Button b)
        {
			this.btn = b;
			this.type = t;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
        	if(type.toLowerCase().equals("date")) {
        		dateTimeDialog =  new DatePickerDialog(getActivity(), this, year, 
        				month, day);
        		return dateTimeDialog;
        	}
        	else if(type.toLowerCase().equals("time")) {
        		dateTimeDialog = new TimePickerDialog(getActivity(), this, hour, 
	            		minute, DateFormat.is24HourFormat(getActivity()));
        		return dateTimeDialog;
        	}
        	return null;
        }
        
        public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			
			c.clear();
			c.set(Calendar.YEAR, selectedYear);
			c.set(Calendar.MONTH, selectedMonth);
			c.set(Calendar.DAY_OF_MONTH, selectedDay);
			
			dayOfTheWeek = strDaysOfTheWeek[c.get(Calendar.DAY_OF_WEEK) - 1];
			
			btn.setText(new DateTimeFormatter(dayOfTheWeek, day, month, year).getDate()); 
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int min) {
            if(hourOfDay <= 12) {
            	hour = hourOfDay;
            } else {
            	hour = hourOfDay - 12;
            }
            
            minute = min;
            	            
            am_pm = hourOfDay < 12 ? new StringBuilder().append(" AM") : new StringBuilder().append(" PM");
            
    		StringBuilder strTime = new DateTimeFormatter(hour, minute, am_pm).getTime();
    		btn.setText(strTime);
        }

    }
	
	public class NumberPickerCustomDialogFragment extends DialogFragment implements NumberPicker.OnValueChangeListener, Formatter{
		private String type;
		private Button btn;
		private int minValue;
		private int maxValue;
		private NumberPicker np = null;
		
		public NumberPickerCustomDialogFragment(String t, Button b, int min, int max) {
			this.type=t;
			this.btn=b;
			this.minValue=min;
			this.maxValue=max;
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
		    // make dialog object
		    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

	        builder.setTitle(type);

	        np = new NumberPicker(getActivity());
	        
	        String[] nums = new String[maxValue - minValue + 1];
	        for(int i=0; i<nums.length; i++)
	               nums[i] = Integer.toString(i);

	        np.setMinValue(minValue);
	        np.setMaxValue(maxValue);
	        np.setWrapSelectorWheel(true);
	        np.setDisplayedValues(nums);
	        np.setValue(1);

	        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int whichButton) {
	        		Log.v("numberpicker", String.valueOf(np.getValue()));
	        		if(type.equalsIgnoreCase("hour")) {
	        			intHour = np.getValue();
	        			btn.setText(String.valueOf(intHour));
	        		}
	        		else {
	        			intMinute = np.getValue();
	        			btn.setText(String.valueOf(intMinute));
	        		}
	        	}
	        });

	        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog, int whichButton) {
	        	  Log.v("numberpicker", String.valueOf(np.getValue()));
	          }
	        });

		    builder.setView(np);

		    // create the dialog from the builder then show
		    return builder.create();
		}

		@Override
		public String format(int value) {
			return null;
		}

		@Override
		public void onValueChange(NumberPicker picker, int oldVal,
				int newVal) {
			Log.v("numberpicker", String.valueOf(newVal));
		}
	}
	
	public class CalendarPickerCustomDialogFragment extends DialogFragment implements OnDateChangeListener {
		private CalendarView calView;
		private Button btn;
		
		public CalendarPickerCustomDialogFragment(Button b) {
			calView = null;
			btn = b;
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Calendar");
			builder.setMessage("Click to schedule or view events.");
			
			LayoutInflater inflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService
		              (Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout ll= (LinearLayout)inflater.inflate(R.layout.foo, null, false);
			calView = (CalendarView) ll.getChildAt(0);
			calView.setOnDateChangeListener(this);
			
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			
			builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			
			builder.setView(ll);
			return builder.create();
		}

		@Override
		public void onSelectedDayChange(CalendarView view, int year,
				int month, int dayOfMonth) {
			c.clear();
			c.set(Calendar.YEAR, year);
			c.set(Calendar.MONTH, month);
			c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			
			dayOfTheWeek = strDaysOfTheWeek[c.get(Calendar.DAY_OF_WEEK) - 1];
			
			btn.setText(new StringBuilder()
			.append(dayOfTheWeek).append("  ").append(day).append("/").append(month + 1)
			.append("/").append(year).append(" ")); 
		}
		
	}

	private class DateTimeFormatter  {

		private String dayOfTheWeek;
		private int day;
		private int month;
		private int year;
		private int hour;
		private int minute;
		private StringBuilder am_pm;
		
		public DateTimeFormatter(String dayOfTheWeek, int day, int month, int year) {
			this.dayOfTheWeek = dayOfTheWeek;
			this.day = day;
			this.month = month;
			this.year = year;
		}
		
		public DateTimeFormatter(int hour, int minute, StringBuilder am_pm) {
			this.hour = hour;
			this.minute = minute;
			this.am_pm = am_pm;
		}
		
		private StringBuilder getDate() {
			return new StringBuilder()
			.append(dayOfTheWeek).append("  ").append(day).append("/").append(month + 1).append("/")
			.append(year).append(" ");
		}
		
		private StringBuilder getTime() {
            String strMinute = String.format("%02d", minute);

			return new StringBuilder().append(hour).append(":")
					.append(strMinute).append(am_pm).append(" ");
		}
	}
	
	private class CustomAdapter extends ArrayAdapter<String> {

		public CustomAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			
		}
	}
	
	
	private class ConfRoomAdapter extends ArrayAdapter<String> {
		private List<String> rooms;
		private Context context;
		
		public ConfRoomAdapter(Context context, int resource,
				 List<String> rooms) {
			super(context, resource, rooms);
			this.rooms = rooms;
			this.context = context;
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			View row = convertView;
			
			if(row == null) {
				LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().
						getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = layoutInflater.inflate(R.layout.myvcs_confspinnerrow, parent, false);
			}
			
			TextView tv = (TextView) row.findViewById(R.id.rowTextView);
			tv.setText(rooms.get(position));
			
			ImageView iv = (ImageView) row.findViewById(R.id.rowImageView);
			iv.setImageDrawable(getResources().getDrawable(R.drawable.hyd_conf2));
			iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					TextView tv = (TextView) v.findViewById(R.id.rowTextView);
					
					LayoutInflater layoutInflater = (LayoutInflater) getContext().
							getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View rv = layoutInflater.inflate(R.layout.roomview, null);
					
					ad = new AlertDialog.Builder(getContext(), 
						android.R.style.Theme_Translucent_NoTitleBar)
						.setView(rv)
						.show();
					
					rv.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							ad.dismiss();						
						}
					});
				}
			});
			
			return row;
		}
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		switch(parent.getId()) {
		
			case R.id.confRoomSpinner:
				/*
				strConfRoomSpinner = parent.getItemAtPosition(position).toString();
				Log.v("conf room", strConfRoomSpinner);
				
				// Check for a valid selection
				if(!strConfRoomSpinner.equalsIgnoreCase(SchedulerConstants.SELECT_ROOM)) {
					confRoomSpinner.setBackground(null);
				}
				break;
				*/
			default:
				break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

	/*
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu, null);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		switch(item.getItemId()) {
		case R.id.action_refresh:
			Toast.makeText(getActivity(), "refresh from " + this.getClass().getSimpleName(), Toast.LENGTH_LONG).show();
			break;
		}
		return true;
	}
	*/

}
