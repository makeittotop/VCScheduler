package com.apareek.rnhvidyoscheduler.db.tables;

public class Owner {
	
	public static final String ID = "_id";
	
	public static final String USER_ID = "user_id";
	
	public static final String DEVICE_IMEI = "imei_number";

	public static final String MAC_ADDRESS = "mac_address";

	public static final String SIM_ID = "sim_id";

	public static final String PHONE_NUMBER = "phone_number";
	
	public static final String TABLE_NAME = "owner";

	public static final String createCommand = "CREATE TABLE " +
			TABLE_NAME +
			" ( " + 
				ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				USER_ID + " INTEGER NOT NULL, " +
				DEVICE_IMEI + " INTEGER NOT NULL, " + 
				MAC_ADDRESS + " TEXT NOT NULL, " +
				SIM_ID + " TEXT NOT NULL," +
				PHONE_NUMBER + " INTEGER NOT NULL" +
			" );";
				
}
