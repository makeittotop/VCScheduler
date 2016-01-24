package com.apareek.rnhvidyoscheduler.db.tables;

public class Schedule {

	//
	public static final String ID = "_id";
	
	// 
	public static final String USER_ID = "user_id";
	
	//
	public static final String LOCATION_ID = "location_id";
	
	public static final String MEETING_NAME = "meeting_name";

	//
	public static final String CONF_ROOM_ID = "room_id";
	
	public static final String VIDYO_ACCOUNT_ID = "vidyo_account";
	
	public static final String START_TIME = "start_time";
	
	public static final String END_TIME = "end_time";
	
	public static final String SCHED_ID = "scheduler_id";
		
	public static final String STATUS = "status";
	
	public static final String PARTICIPANTS = "participants_user_id";

	public static final String TABLE_NAME = "schedule";

	public static final String createCommand = "CREATE TABLE " 
			+ TABLE_NAME +
			" ( " +
				ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				USER_ID + " INTEGER NOT NULL, " +
				LOCATION_ID + " INTEGER NOT NULL, " +
				MEETING_NAME + " TEXT NOT NULL, " +
				CONF_ROOM_ID + " INTEGER NOT NULL, " +
				VIDYO_ACCOUNT_ID + " INTEGER NOT NULL, " +
				START_TIME + " INTEGER NOT NULL, " +
				END_TIME + " INTEGER NOT NULL, " +
				SCHED_ID + " INTEGER NOT NULL, " +
				STATUS + " INTEGER NOT NULL, " +
				PARTICIPANTS + " TEXT NOT NULL" +
			");";
}
