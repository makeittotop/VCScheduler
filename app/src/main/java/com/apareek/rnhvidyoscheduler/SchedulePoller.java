package com.apareek.rnhvidyoscheduler;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.apareek.rnhvidyoscheduler.db.tables.ConferenceRooms;
import com.apareek.rnhvidyoscheduler.db.tables.Employee;
import com.apareek.rnhvidyoscheduler.db.tables.PTSLocations;
import com.apareek.rnhvidyoscheduler.db.tables.Schedule;
import com.apareek.rnhvidyoscheduler.db.tables.VidyoAccounts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SchedulePoller implements Runnable {
	private static Context context;
	private static Handler handler;
	private static MySQLiteHelper mySQLiteHelper;
	private static Object lock = new Object();
	private static SchedulePoller instance;
	private boolean on;
	private boolean wakeup;

	
	private SchedulePoller(Context c) {
		context = c;
		handler = null;
		
		mySQLiteHelper = new MySQLiteHelper(context);
		
		this.on = queryNetwork();
		this.wakeup = false;
	}

	public static SchedulePoller getInstance(Context c) {
		if(instance == null)
			instance = new SchedulePoller(c);
		
		return instance;
	}
	
	@Override
	public void run() {
		while(true) {
			if(on || wakeup) {
				ArrayList<Integer> schedIdList = new ArrayList<Integer>();
				
				Cursor c = mySQLiteHelper.getSchedulerData("PENDING");
				boolean dbTransaction = true;
				while(c.moveToNext()) {
					Log.v("schedule", String.valueOf(c.getInt(c.getColumnIndex(Schedule.ID))));
					
					int rowId = c.getInt(c.getColumnIndex(Schedule.ID));
					
					int userId = c.getInt(c.getColumnIndex(Schedule.USER_ID));
					Cursor c2 = mySQLiteHelper.getSomeData(Employee.TABLE_NAME, Employee.USER_NAME,
							String.valueOf(userId), Employee.USER_ID);
					
					String strUser = null;
					while(c2.moveToNext()) {
						strUser = c2.getString(c2.getColumnIndex(
								Employee.USER_NAME));
					}
							
					int locationId = c.getInt(c.getColumnIndex(Schedule.LOCATION_ID));
					c2 = mySQLiteHelper.getSomeData(PTSLocations.TABLE_NAME, PTSLocations.NAME, 
							String.valueOf(locationId), PTSLocations.ID);
					
					String strLocation = null;
					while(c2.moveToNext()) {
						strLocation = c2.getString(c2.getColumnIndex(
								PTSLocations.NAME));
					}					
					
					String meetingName = c.getString(c.getColumnIndex(Schedule.MEETING_NAME));
					
					int confRoomId = c.getInt(c.getColumnIndex(Schedule.CONF_ROOM_ID));
					c2 = mySQLiteHelper.getSomeData(ConferenceRooms.TABLE_NAME, ConferenceRooms.NAME, 
							String.valueOf(confRoomId), ConferenceRooms.ID);
					
					String strConfRoom = null;
					while(c2.moveToNext()) {
						strConfRoom = c2.getString(c2.getColumnIndex(
								ConferenceRooms.NAME));
					}				
					
					int vidyoRoomId = c.getInt(c.getColumnIndex(Schedule.VIDYO_ACCOUNT_ID));
					c2 = mySQLiteHelper.getSomeData(VidyoAccounts.TABLE_NAME, VidyoAccounts.NAME, 
							String.valueOf(vidyoRoomId), VidyoAccounts.ID);
					
					String strVidyoRoom = null;
					while(c2.moveToNext()) {
						strVidyoRoom = c2.getString(c2.getColumnIndex(
								VidyoAccounts.NAME));
					}					
					
					long startTime = c.getLong(c.getColumnIndex(Schedule.START_TIME));
					long endTime = c.getLong(c.getColumnIndex(Schedule.END_TIME));
					String participants =c.getString(c.getColumnIndex(Schedule.PARTICIPANTS));
					
					Vidyo v = new Vidyo();
					v.getUser().setName(strUser);
					v.getUser().setEmail(strUser);
					
					v.getTime().setStime(startTime);
					v.getTime().setEtime(endTime);
					
					v.getVc().setRoom(strConfRoom);
					v.getVc().setVidyoRoom(strVidyoRoom);
					v.getVc().setMeetingName(meetingName);
					
					v.getType().setType("A");
					
					v.getNotify().setParticipants(participants);
					v.getNotify().setSnotify(1);
					
					v.getOptions().setAudio(1);
					v.getOptions().setVideo(0);
					v.getOptions().setForce(0);
					
					XMLHandler xmlHandler = new XMLHandler(v);
					String xml = xmlHandler.vidyoToXml();

					ClientAsyncTask client = new ClientAsyncTask();
					client.execute(xml);
					
					String serverXml = null;
					try {
						serverXml = client.get();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					
					if(serverXml == null) {
						dbTransaction = false;
						continue;
					}
					
					Log.v("scheduler", serverXml);
						
					v = new XMLHandler().xmlToVidyo(serverXml);
					int schedId = v.getAction().getRid();
						
					schedIdList.add(schedId);
						
					SQLiteDatabase db = mySQLiteHelper.getReadableDatabase();
					mySQLiteHelper.populateScheduleStatus(db, rowId, schedId);
				}
				wakeup = false;
				
				Message msg = new Message();
				if(schedIdList.size() != 0) {
					Bundle b = new Bundle();
					b.putIntegerArrayList("schedulerIdList", schedIdList);
					
					msg.what = 22;
					msg.setData(b);
					
				}
				else if(dbTransaction == false) {
					// No network!?
					msg.what = 101;
				}
				else {
					// Nothing to send
					msg.what = 100;
				}
				
				handler.sendMessage(msg);
			}
			
			synchronized (lock) {
				try {
					Log.v("scheduler", "going to wait for 30 seconds");
					lock.wait(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// Get the network status
				on = queryNetwork();
			}
		}
	}
	
	public void awaken() {
		Log.v("scheduler", "waking up the thread");
		wakeup = true;
		synchronized (lock) {
			lock.notify();
		}
	}

	public void setHandler(Handler handler2) {
		handler = 	handler2;
	}
	
	private boolean queryNetwork() {
		boolean status = NetworkStatus.getInstance(context).getNetworkState();
		return status;
	}
}
