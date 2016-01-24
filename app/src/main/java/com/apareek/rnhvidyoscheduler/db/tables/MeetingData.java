package com.apareek.rnhvidyoscheduler.db.tables;

public class MeetingData {
	public static final String ID = "_id";

	public static final String MEETING_ID = "meeting_id";
	
	public static final String MUTE_AUDIO = "mute_audio";
	
	public static final String MUTE_VIDEO = "mute_video";
	
	public static final String FORCE_OPTION = "force_option";

	public static final String MY_NOTES = "my_notes";

	public static final String ENABLE_SMS = "enable_sms";

	public static final String TABLE_NAME = "meetingdata";

	public static final String createCommand = "CREATE TABLE " + 
			TABLE_NAME +
			" ( " +
				ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				MEETING_ID + " INTEGER NOT NULL, " +
				MUTE_AUDIO + " INTEGER NOT NULL, " +
				MUTE_VIDEO + " INTEGER NOT NULL, " +
				FORCE_OPTION + " INTEGER NOT NULL, " +
				MY_NOTES + " TEXT NOT NULL, " +
				ENABLE_SMS + " INTEGER NOT NULL" +
			" );";	
	
}
