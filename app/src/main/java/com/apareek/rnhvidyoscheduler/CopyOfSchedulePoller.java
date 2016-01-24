package com.apareek.rnhvidyoscheduler;

import com.apareek.rnhvidyoscheduler.db.tables.ConferenceRooms;
import com.apareek.rnhvidyoscheduler.db.tables.Employee;
import com.apareek.rnhvidyoscheduler.db.tables.PTSLocations;
import com.apareek.rnhvidyoscheduler.db.tables.Schedule;
import com.apareek.rnhvidyoscheduler.db.tables.VidyoAccounts;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class CopyOfSchedulePoller implements Runnable {
	private static Context context;
	private static MySQLiteHelper mySQLiteHelper;
	private static Object lock = new Object();
	private static CopyOfSchedulePoller instance;
	private boolean on;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what) {
				case SchedulerConstants.CONNECT_TO_SERVER:
					String serverXml = msg.getData().getString(SchedulerConstants.FROM_SERVER_KEY);
					Log.v("scheduler", serverXml);
					break;
				case 1:
					
					break;
			}
		}
	};
	
	private CopyOfSchedulePoller(Context c) {
		context = c;
		mySQLiteHelper = new MySQLiteHelper(context);
		this.on = false;
	}

	public static CopyOfSchedulePoller getInstance(Context c) {
		if(instance == null)
			instance = new CopyOfSchedulePoller(c);
		
		return instance;
	}
	
	@Override
	public void run() {
		while(true) {
			if(on) {
				Cursor c = mySQLiteHelper.getSchedulerData("PENDING");
				while(c.moveToNext()) {
					Log.v("schedule", String.valueOf(c.getInt(c.getColumnIndex(Schedule.ID))));
					
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
					
					Client client = new Client(xml, handler);
					client.setName("Client!");
					client.start();
					
					Log.v("scheduler", xml);
					
					try {
						client.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				on = false;
			}
			
			synchronized (lock) {
				try {
					Log.v("scheduler", "going to wait for 30 seconds");
					lock.wait(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void awaken() {
		Log.v("scheduler", "waking up the thread");
		on = true;
		synchronized (lock) {
			lock.notify();
		}
	}
	
}
