package com.apareek.rnhvidyoscheduler.db.tables;

public class PTSLocations {
	// P.K.
	public static final String ID = "_id";
	
	// Location 
	public static final String NAME = "location_name";
	
	// Status
	public static final String STATUS = "status";
	
	// Name of the table
	public static final String TABLE_NAME = "ptslocations";
	
	public static final String createCommand = "CREATE TABLE " 
			+ TABLE_NAME +
			"( " +
				ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				NAME + " TEXT NOT NULL, " +
				STATUS + " INTEGER NOT NULL" +
			" );";
}
