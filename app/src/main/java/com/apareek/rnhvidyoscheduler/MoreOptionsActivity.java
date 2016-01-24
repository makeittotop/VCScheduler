package com.apareek.rnhvidyoscheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import android.R.bool;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MoreOptionsActivity extends Activity implements OnClickListener{
	private Vidyo vidyo;
	
	private EditText chairEditText;
	// 
	private AutoCompleteTextView chairAutoCompleteTextView;
	private MultiAutoCompleteTextView inviteesMultiAutoCompleteTextView;
	
	private EditText inviteesEditText;
	private EditText contactNumbersEditText;
	private EditText emailAliasesEditText;
	
	private CheckBox smsCheckButton;
	
	private CheckBox videoCheckBox;
	private CheckBox audioCheckBox;
	
	private Button goButton;
	private Button cancelButton;
	
	private String strConfRoom;
	private String strVidyoRoom;
	private String strMeetingName;
	private String type;
	private long sTime;
	private long eTime;
	
	private String strChair;
	private String strInvitees;
	private String strContactNumbers;
	private String strEmailAliases;
	
	private int isSms;
	
	private int isVideo;
	private int isAudio;
	private int isForce = 0;
	private String strNotes = "My notes";
	private String strVidyoToXml;
	private ProgressDialog dialog;

	private MySQLiteHelper mySQLiteHelper;
	private SchedulePoller poller;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			if(msg.what == SchedulerConstants.CONNECT_TO_SERVER) {
				Log.v("handler", "in handle message connect to server");
				dialog.dismiss();
				String serverXml = msg.getData().getString(SchedulerConstants.FROM_SERVER_KEY);
				
				Intent data = new Intent();
				if(serverXml.equalsIgnoreCase("") || serverXml == null) {
					data.putExtra("rid", "Error: No reply from the server");
					setResult(RESULT_CANCELED, data);
				}
				else {
					data.putExtra("rid", serverXml);
					data.putExtra(SchedulerConstants.VIDYO_DATA_KEY, vidyo);
					setResult(RESULT_OK, data);
				}
				finish();
			}
			else if(msg.what == SchedulerConstants.DISPLAY_PROGRESS_DIALOG) {
				dialog = new ProgressDialog(MoreOptionsActivity.this);
				dialog.setTitle("Connecting...");
				dialog.setMessage("Please wait");
				dialog.show();
			}
			else if(msg.what == 22) {
				dialog.dismiss();
				
				ArrayList<Integer> schedIdList = msg.getData()
						.getIntegerArrayList("schedulerIdList");
				
				Intent data = new Intent();
				data.putExtra("schedIdList", schedIdList);
				
				setResult(22, data);
				finish();
			}
			else if(msg.what == 100) {
				if(dialog.isShowing()) {
					dialog.dismiss();
				}
				
				Toast.makeText(getApplicationContext(), 
						"No new data found in 'PENDING' state to send to the server",
						Toast.LENGTH_LONG).show();
				
				setResult(100);
				finish();
			}
			else if(msg.what == 101) {
				if(dialog.isShowing()) {
					dialog.dismiss();
				}
				
				Toast.makeText(getApplicationContext(), 
						"Network unavailable for some reason. Retrying in 30 seconds",
						Toast.LENGTH_LONG).show();
				
				setResult(101);
				finish();
			}
		}	
	};
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);

		actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.steelblue));
		
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		
		setContentView(R.layout.vc_more_options_activity);
				
		init();
		listen();
	}

	private void init() {
		mySQLiteHelper = new MySQLiteHelper(getApplicationContext());
		
		//chairEditText = (EditText) findViewById(R.id.ch);
		
		// 
		String[] employeeNameSuggestionList = mySQLiteHelper.getEmployeeData("t");

		chairAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.chairAutoCompleteTextView);
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getBaseContext(),
				android.R.layout.simple_list_item_1, employeeNameSuggestionList);
		chairAutoCompleteTextView.setAdapter(arrayAdapter);
		chairAutoCompleteTextView.setThreshold(1);
		
		inviteesMultiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.inviteesAutoCompleteTextView);
		
		ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getBaseContext(),
				android.R.layout.simple_list_item_1, employeeNameSuggestionList);
		inviteesMultiAutoCompleteTextView.setAdapter(arrayAdapter2);
		inviteesMultiAutoCompleteTextView.setThreshold(1);
		inviteesMultiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		inviteesMultiAutoCompleteTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
//		inviteesEditText = (EditText) findViewById(R.id.inviteesEditText);
		contactNumbersEditText = (EditText) findViewById(R.id.contactNumEditText);
		emailAliasesEditText = (EditText) findViewById(R.id.emailAliasesEditText);
		
		smsCheckButton = (CheckBox) findViewById(R.id.smsCheckBox);
		
		videoCheckBox = (CheckBox) findViewById(R.id.videoCheckBox);
		audioCheckBox = (CheckBox) findViewById(R.id.audioCheckBox);
		
		goButton = (Button) findViewById(R.id.goButton);
		cancelButton = (Button) findViewById(R.id.cancelButton);
		
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		vidyo = extras.getParcelable(SchedulerConstants.VIDYO_DATA_KEY);
				
		debug(vidyo);
	}
	
	private void listen() {
		goButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.goButton:
				collectData();
				
				strVidyoToXml = convertToXML();
				Log.v("xml", strVidyoToXml);
			
				LayoutInflater inflater = (LayoutInflater) this
						.getSystemService(LAYOUT_INFLATER_SERVICE);

				View view = inflater.inflate(R.layout.meetingsummary, null, false);
				TextView summaryMeetingTV = (TextView) view.findViewById(R.id.sumMeetingtextView);
				TextView summaryConfRoomTextView = (TextView) view.findViewById(R.id.summaryConfRoomTextView);
				TextView summaryVidyoRoomTV = (TextView) view.findViewById(R.id.summaryVidyoRoomTextView);
				TextView summaryStartTimeTV = (TextView) view.findViewById(R.id.summaryMeetingStartTextView);
				TextView summaryEndTimeTV = (TextView) view.findViewById(R.id.summaryMeetingEndTextView);
				TextView summaryRepeatTextView = (TextView) view.findViewById(R.id.sumRepeatTextView);
				TextView sumChairTextView = (TextView) view.findViewById(R.id.sumChairTextView);
				TextView sumInviteesTextView = (TextView) view.findViewById(R.id.sumInviteesTextView);
				TextView sumConNumTextView = (TextView) view.findViewById(R.id.sumConNumTextView);
				TextView sumEmailTextView = (TextView) view.findViewById(R.id.sumEmailTextView);
				CheckBox sumSmsCheckBox = (CheckBox) view.findViewById(R.id.sumsmsCheckBox);
				CheckBox sumAudioCheckBox = (CheckBox) view.findViewById(R.id.sumAudiocheckBox);
				CheckBox sumVideoCheckBox = (CheckBox) view.findViewById(R.id.sumVideoCheckBox);
		        
				summaryMeetingTV.setText(strMeetingName);
				summaryConfRoomTextView.setText(strConfRoom);
				summaryVidyoRoomTV.setText(strVidyoRoom);
				summaryStartTimeTV.setText(EpochHandler.getFormattedDate(sTime));
				summaryEndTimeTV.setText(EpochHandler.getFormattedDate(eTime));
				summaryRepeatTextView.setText("One-Time Event");
				sumChairTextView.setText(strChair);
				sumInviteesTextView.setText(strInvitees);
				sumConNumTextView.setText(strContactNumbers);
				sumEmailTextView.setText(strEmailAliases);
				sumSmsCheckBox.setEnabled(false);
				sumAudioCheckBox.setEnabled(false);
				sumVideoCheckBox.setEnabled(false);
				sumSmsCheckBox.setChecked(isSms == 1);
				sumAudioCheckBox.setChecked(isAudio == 1);
				sumVideoCheckBox.setChecked(isVideo == 1);	
								
				Builder d = new Builder(this).setTitle("Summary").setView(view)
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {			
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						})
						.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								//handler.sendEmptyMessage(SchedulerConstants.CONNECT_TO_SERVER);	
								connectToServer(strVidyoToXml);
							}
						});
				d.show();
				break;
			case R.id.cancelButton:
				setResult(RESULT_CANCELED);
				finish();
				break;
		}
	}
	
	private void connectToServer(String xml) {
		dialog = new ProgressDialog(MoreOptionsActivity.this);
		dialog.setTitle("Connecting to server...");
		dialog.setMessage("Please wait");
		dialog.show();
		
/*		
		MyQueue queue = MyQueue.getInstance();
		MyPacket myPack = new MyPacket();
		myPack.setHandler(handler);
		myPack.setXml(xml);
		
		try {
			queue.put(myPack);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
*/		
		// Pre request insertion into the local database - schedule table
		dbInsertSchedule();
		
//		TestClient client = TestClient.getInstance();
//		client.setInfo(xml, handler);
				
/*
		new MyAsyncTask().execute(xml);		
		try {
			serverXml = myAsyncTask.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}		
		return serverXml;
 
*/
	}
	
	private void dbInsertSchedule() {
		MySQLiteHelper helper = new MySQLiteHelper(getApplicationContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		helper.populateSchedulerTable(db, vidyo);
		
		poller = SchedulePoller.getInstance(this);
		poller.setHandler(handler);
		poller.awaken();
	}

	private void collectData() {
		strChair = chairAutoCompleteTextView.getText().toString();
		strInvitees = inviteesMultiAutoCompleteTextView.getText().toString();
		strContactNumbers = contactNumbersEditText.getText().toString();
		strEmailAliases = emailAliasesEditText.getText().toString();
		isSms = smsCheckButton.isChecked() ? 1 : 0;
		isVideo = videoCheckBox.isChecked() ? 1 : 0;
		isAudio = audioCheckBox.isChecked() ? 1 : 0;
		
		eTime = vidyo.getTime().getEtime();
		sTime = vidyo.getTime().getStime();
		
		strConfRoom = vidyo.getVc().getRoom();
		strVidyoRoom = vidyo.getVc().getVidyoRoom();
		strMeetingName = vidyo.getVc().getMeetingName();
		
		type = vidyo.getType().getType();
		
		vidyo.getUser().setName(strChair);
		vidyo.getUser().setEmail(strEmailAliases);
		
		vidyo.getNotify().setParticipants(strInvitees);
		vidyo.getNotify().setPhonenos(strContactNumbers);
		vidyo.getNotify().setSnotify(isSms);
		
		vidyo.getOptions().setVideo(isVideo);
		vidyo.getOptions().setAudio(isAudio);
		vidyo.getOptions().setForce(isForce);
		vidyo.getOptions().setNotes(strNotes);
		
		return;
	}
	
	private String convertToXML() {
		XMLHandler xmlHandler = new XMLHandler(vidyo);
		return xmlHandler.vidyoToXml();
	}
	
	public void debug(Vidyo v) {
		eTime = v.getTime().getEtime();
		sTime = v.getTime().getStime();
		
		strConfRoom = v.getVc().getRoom();
		strVidyoRoom = v.getVc().getVidyoRoom();
		strMeetingName = v.getVc().getMeetingName();
		
		type = v.getType().getType();
		
		Log.v("meeting", String.valueOf(strMeetingName));
		Log.v("stime", String.valueOf(sTime));
		Log.v("etime", String.valueOf(eTime));
		Log.v("room", strConfRoom);
		Log.v("vidyo room", strVidyoRoom);
		
		Log.v("type", type);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
				Log.v("MoreOptionsActivity", "bar");
				Intent intent = new Intent(this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
