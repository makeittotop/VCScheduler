package com.apareek.rnhvidyoscheduler;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.apareek.rnhvidyoscheduler.db.tables.*;

public class MySQLiteHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "vidyo2.db";
	private static final int DATABASE_VERSION = 1;
	private Context context;
	
	public MySQLiteHelper(Context c) {
		super(c, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = c;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Employee table
		Log.v("CREATE ", Employee.createCommand);

		db.execSQL(Employee.createCommand);
		populateEmployeesTable(db);

		// PTS locations
		Log.v("CREATE ", PTSLocations.createCommand);

		db.execSQL(PTSLocations.createCommand);
		populatePTSLocationsTable(db);
		
		// Vidyo account
		Log.v("CREATE ", VidyoAccounts.createCommand);

		db.execSQL(VidyoAccounts.createCommand);
		populateVidyoAccountsTable(db);
		
		// Conference rooms
		Log.v("CREATE ", ConferenceRooms.createCommand);

		db.execSQL(ConferenceRooms.createCommand);
		populateConferenceRoomsTable(db);
	
		// Owner
		Log.v("CREATE ", Owner.createCommand);

		db.execSQL(Owner.createCommand);
		populateOwnerTable(db);
		
		// Schedule
		Log.v("CREATE ", Schedule.createCommand);

		db.execSQL(Schedule.createCommand);
//		populateOwnerTable(db);
		
		Log.v("path", db.getPath());
		
		for ( String database : context.databaseList() ) {
			Log.v("database list", database);
		}
	}

	private void populateOwnerTable(SQLiteDatabase db) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		String deviceImei = telephonyManager.getDeviceId();
		String phoneNumber = telephonyManager.getLine1Number();
		String simId = telephonyManager.getSimSerialNumber();
		
		String macAddress = wifiManager.getConnectionInfo().getMacAddress();
		
		ContentValues cv = new ContentValues();
		cv.put(Owner.DEVICE_IMEI, deviceImei);
		cv.put(Owner.MAC_ADDRESS, macAddress);
		cv.put(Owner.SIM_ID, simId);
		cv.put(Owner.PHONE_NUMBER, phoneNumber);
		cv.put(Owner.USER_ID, 3059);
		
		if(db.insert(Owner.TABLE_NAME, Owner.TABLE_NAME, cv) == -1) {
			Log.e(Thread.currentThread().getName(), 
					Owner.TABLE_NAME + "(" + 
					 ")" + " misfired");		
		}
	}

	private void populateConferenceRoomsTable(SQLiteDatabase db) {
		String[] hydConfRooms = context.getResources().getStringArray(R.array.hyd_conference_room);
		String[] mumConfRooms = context.getResources().getStringArray(R.array.mum_conference_room);
		
		// HYD
		for (String confRoom : hydConfRooms) {
			ContentValues cv = new ContentValues();
			cv.put(ConferenceRooms.NAME, confRoom);
			cv.put(ConferenceRooms.LOC_ID, 1);
			
			if(db.insert(ConferenceRooms.TABLE_NAME, 
					ConferenceRooms.TABLE_NAME, cv) == -1) {
				Log.e(Thread.currentThread().getName(), 
						ConferenceRooms.TABLE_NAME + "(" + 
						confRoom + ")" + " misfired");			
			}
		}
		
		// MUM
		for (String confRoom : mumConfRooms) {
			ContentValues cv = new ContentValues();
			cv.put(ConferenceRooms.NAME, confRoom);
			cv.put(ConferenceRooms.LOC_ID, 2);
			
			if(db.insert(ConferenceRooms.TABLE_NAME, 
					ConferenceRooms.TABLE_NAME, cv) == -1) {
				Log.e(Thread.currentThread().getName(), 
						ConferenceRooms.TABLE_NAME + "(" + 
						confRoom + ")" + " misfired");			
			}
		}		

	}

	private void populatePTSLocationsTable(SQLiteDatabase db) {
		String[] locations = context.getResources().getStringArray(R.array.locations);
		
		for(String loc : locations) {
			ContentValues cv = new ContentValues();
			cv.put(PTSLocations.NAME, loc);
			cv.put(PTSLocations.STATUS, 1);
			
			if(db.insert(PTSLocations.TABLE_NAME, 
					PTSLocations.TABLE_NAME, cv) == -1) {
				Log.e(Thread.currentThread().getName(), 
						PTSLocations.TABLE_NAME + "(" + 
						loc + ")" + " misfired");
			}
		}
	}

	private void populateVidyoAccountsTable(SQLiteDatabase db) {
		String[] vidyoAccounts = context.getResources().getStringArray(R.array.vidyo_room);
		
		for(String account : vidyoAccounts) {
			ContentValues cv = new ContentValues();
			cv.put(VidyoAccounts.NAME, account);
			cv.put(VidyoAccounts.STATUS, 1);
			
			if(db.insert(VidyoAccounts.TABLE_NAME, 
					VidyoAccounts.TABLE_NAME, cv) == -1) {
				Log.e(Thread.currentThread().getName(), 
						VidyoAccounts.TABLE_NAME + "(" + 
						account + ")" + " misfired");				
			}
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		Log.w("LOG_TAG", "Upgrading database from version "+ 
				oldVer + " to " +
				newVer + ", which will destroy all old data");
		
        // KILL PREVIOUS TABLES IF UPGRADED
        db.execSQL("DROP TABLE IF EXISTS " + Employee.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PTSLocations.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VidyoAccounts.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ConferenceRooms.TABLE_NAME);

        // CREATE NEW INSTANCE OF SCHEMA
        onCreate(db);		
	}
	
	public void populateEmployeesTable(SQLiteDatabase db) {
		String[] employeeData = context.getResources().getStringArray(R.array.employee);
		
		for (String employee : employeeData) {
			Log.v("employee data", employee);

			if(addEmployeeRow(db, employee) == -1) {
				Log.e(Thread.currentThread().getName(),
						Employee.TABLE_NAME + "(" + 
						employee + ")" + " misfired");
			}
		}

		return;
	}

	private long addEmployeeRow(SQLiteDatabase sd, String employee) {
		
		String[]employeeFields = employee.split(",");
		
		ContentValues cv = new ContentValues();
		cv.put(Employee.USER_ID, employeeFields[0]);
		cv.put(Employee.FIRST_NAME, employeeFields[1]);
		cv.put(Employee.LAST_NAME, employeeFields[2]);
		cv.put(Employee.USER_NAME, employeeFields[3]);
		cv.put(Employee.DEPT_ID, employeeFields[4]);
		cv.put(Employee.STATUS, employeeFields[5]);
		cv.put(Employee.LOC_ID, employeeFields[6]);
		cv.put(Employee.SUPER_USER, 0);

		Log.v("employee data", employeeFields[0]);
		Log.v("employee data", employeeFields[1]);
		Log.v("employee data", employeeFields[2]);
		Log.v("employee data", employeeFields[3]);
		Log.v("employee data", employeeFields[4]);
		Log.v("employee data", employeeFields[5]);
		Log.v("employee data", employeeFields[6]);
		
		return sd.insert(Employee.TABLE_NAME, Employee.TABLE_NAME, cv);
	}
	
	public String[] getConferenceRoomData(int locId) {
		SQLiteDatabase db = getReadableDatabase();
		
		String[] cols = new String[] {ConferenceRooms.NAME};
		String[] selectionArgs = new String[] {String.valueOf(locId)};
		
		Cursor c = db.query(ConferenceRooms.TABLE_NAME, cols,
				ConferenceRooms.LOC_ID + " = ?", selectionArgs, null, null, null);
		
		ArrayList<String> roomList = new ArrayList<String>();
		while(c.moveToNext()) {
			String room = c.getString(c.getColumnIndex(ConferenceRooms.NAME));
			roomList.add(room);
		}
		
		return roomList.toArray(
			new String[roomList.size()]
		);
	}
	
	public String[] getVidyoRoomData() {
		SQLiteDatabase db = getReadableDatabase();
		
		String[] cols = new String[] {VidyoAccounts.NAME};
//		String[] selectionArgs = new String[] {};
		
		Cursor c = db.query(VidyoAccounts.TABLE_NAME, cols,
				null, null, null, null, null);
		
		List<String> vidyoAccountList = new ArrayList<String>();
		while(c.moveToNext()) {
			String account = c.getString(c.getColumnIndex(VidyoAccounts.NAME));
			vidyoAccountList.add(account);
		}
		
		return vidyoAccountList.toArray(
			new String[vidyoAccountList.size()]
		);
	}
	
	public String[] getEmployeeData(String sel) {
		SQLiteDatabase db = getReadableDatabase();
		
		//String[] col = new String[] {Employee.FIRST_NAME, Employee.LAST_NAME, Employee.USER_NAME};
		String[] col = new String[] {Employee.USER_NAME};
		String[] selStrings = new String[] {sel};
		
		Cursor c = db.query(Employee.TABLE_NAME, col,
				Employee.STATUS + " = ?", selStrings, null, null, null);
		
		String[] employeeNameData = new String[c.getCount()/* * 3 */];
		int count = 0;
		while(c.moveToNext()) {
			/*
			employeeNameData[count++] = c.getString(c.getColumnIndex(Employee.FIRST_NAME));
			employeeNameData[count++] = c.getString(c.getColumnIndex(Employee.LAST_NAME));
			*/
			employeeNameData[count++] = c.getString(c.getColumnIndex(Employee.USER_NAME));
		}
		
		return employeeNameData;
	}
	/*
	public void getOwnerData() {
		SQLiteDatabase db = getReadableDatabase();
		
		//String[] col = new String[] {String.valueOf(Owner.USER_ID)};
				
		Cursor c = db.query(Owner.TABLE_NAME, null, null, null, null, null, null);
		while(c.moveToNext()) {
			int userId = c.getInt(c.getColumnIndex(Owner.USER_ID));
			String deviceImei = c.getString(c.getColumnIndex(Owner.DEVICE_IMEI));
			String deviceMacAdress = c.getString(c.getColumnIndex(Owner.MAC_ADDRESS));
			String deviceSimId = c.getString(c.getColumnIndex(Owner.SIM_ID));
			int devicePhoneNumber = c.getInt(c.getColumnIndex(Owner.PHONE_NUMBER));
		}
		
		//String[] col = new String() {};
		String[] selectionArgs = new String[] {userId};
		c = db.query(Employee.TABLE_NAME, null,
				Employee.USER_ID + " = ?", selectionArgs, null, null, null);
		while(c.moveToNext()) {
			String fName = c.getString(c.getColumnIndex(Employee.FIRST_NAME));
			String lName = c.getString(c.getColumnIndex(Employee.LAST_NAME));
			String uName = c.getString(c.getColumnIndex(Employee.USER_NAME));
			int deptId = c.getInt(c.getColumnIndex(Employee.DEPT_ID));
			int locId = c.getInt(c.getColumnIndex(Employee.LOC_ID));
			int status = c.getInt(c.getColumnIndex(Employee.STATUS));
			int superUser = c.getInt(c.getColumnIndex(Employee.SUPER_USER));
		}
		
		
		selectionArgs = new String[] {locId};
		c = db.query(PTSLocations.TABLE_NAME, (new String[] {PTSLocations.NAME}),
				PTSLocations.ID + " = ?", selectionArgs, null, null, null);
		
	}
	*/
	
	public Cursor getSomeData(String tableName, String column2Return, String selectionArg, String column2Match) {
		SQLiteDatabase db = getReadableDatabase();
		
		String[] col = new String[] {column2Return};
		
		String[] selArgs = new String[] {selectionArg};
		
		return db.query(tableName, col, column2Match + " = ? ", selArgs, null, null, null);
	}

	public void populateSchedulerTable(SQLiteDatabase db, Vidyo v) {
		int userId = 0;
		Cursor c = db.rawQuery("SELECT " + Owner.USER_ID + " from " + Owner.TABLE_NAME, null);
		while(c.moveToNext()) {
			userId = c.getInt(c.getColumnIndex(Owner.USER_ID));
			Log.v("user id", String.valueOf(userId));
		}
		
		int locationId = v.getVc().getLocationId();
		
		String meetingName = v.getVc().getMeetingName().trim();
		
		// Server wants these as strings
		String vidyoAccount = v.getVc().getVidyoRoom();
		c = getSomeData(VidyoAccounts.TABLE_NAME, VidyoAccounts.ID, vidyoAccount, VidyoAccounts.NAME);
		
		int vidyoAccountId = 0;
		while(c.moveToNext()) {
			vidyoAccountId = c.getInt(c.getColumnIndex(VidyoAccounts.ID));
			Log.v("vidyo", String.valueOf(vidyoAccountId));
		}
		 
		String confRoom = v.getVc().getRoom().trim();
		c = getSomeData(ConferenceRooms.TABLE_NAME, ConferenceRooms.ID, confRoom, ConferenceRooms.NAME);
		
		int confRoomId = 0;
		while(c.moveToNext()) {
			confRoomId = c.getInt(c.getColumnIndex(ConferenceRooms.ID));
			Log.v("vidyo", String.valueOf(confRoomId));
		}
		
		long startTime = v.getTime().getStime();
		long endTime = v.getTime().getEtime();
		int schedId = 0;
		String status = "PENDING";
		String participants = v.getNotify().getParticipants().trim();
		participants = "apareek,rahmed";
		
		ContentValues cv = new ContentValues();
		cv.put(Schedule.USER_ID, userId);
		cv.put(Schedule.LOCATION_ID, locationId);
		cv.put(Schedule.VIDYO_ACCOUNT_ID, vidyoAccountId);
		cv.put(Schedule.CONF_ROOM_ID, confRoomId);
		cv.put(Schedule.MEETING_NAME, meetingName);
		cv.put(Schedule.START_TIME, startTime);
		cv.put(Schedule.END_TIME, endTime);
		cv.put(Schedule.SCHED_ID, schedId);
		cv.put(Schedule.STATUS, status);
		cv.put(Schedule.PARTICIPANTS, participants);
		
		if(db.insert(Schedule.TABLE_NAME,
				Schedule.TABLE_NAME, cv) == -1) {
			Log.e("Schedule table", cv.toString() + " misfired");
		}
		
		// Pass this along to the client from here
		/*
		XMLHandler xmlHandler = new XMLHandler(vidyo);
		return xmlHandler.vidyoToXml();
		*/
	}
	
	public Cursor getSchedulerData(String status) {
		SQLiteDatabase db = getReadableDatabase();
		
		String[] col = new String[] {};
		String[] selArgs = new String[] {status};
		
		//Cursor c = db.rawQuery("Select * from " + Schedule.TABLE_NAME + " where " + Schedule.STATUS + " = ?",
		//		selArgs);
		
		Cursor c = db.query(Schedule.TABLE_NAME, null,
				Schedule.STATUS + " = ? ", selArgs, null, null, null);

		return c;
	}
	
	public void populateScheduleStatus(SQLiteDatabase db, int rowId, int schedId) {
		ContentValues cv = new ContentValues();
		cv.put(Schedule.STATUS, "UPDATED");
		cv.put(Schedule.SCHED_ID, schedId);
		
		String[] selArgs = new String[] {String.valueOf(rowId), "PENDING"};
		db.update(Schedule.TABLE_NAME, cv, Schedule.ID + " = ? AND " + Schedule.STATUS + " = ? ", selArgs);
	}
}
