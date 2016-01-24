package com.apareek.rnhvidyoscheduler.db.tables;

public class ConferenceRooms {

	// P.K.
	public static final String ID = "_id";
	
	// Room names
	public static final String NAME = "room_name";
	
	// Location
	public static final String LOC_ID = "location_id";
	
	// Table name
	public static final String TABLE_NAME = "conferencerooms";
	
	// Create Query
	public static final String createCommand = "CREATE TABLE " + 
			TABLE_NAME +
			" (" +
				ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				NAME + " TEXT NOT NULL, " +
				LOC_ID + " INTEGER NOT NULL" +
			" );";
}
