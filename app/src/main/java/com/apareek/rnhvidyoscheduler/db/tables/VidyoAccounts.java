package com.apareek.rnhvidyoscheduler.db.tables;

public class VidyoAccounts {

	// P.K.
	public static final String ID = "_id";
	
	// Name
	public static final String NAME = "vidyo_account_name";
	
	// Status
	public static final String STATUS = "account_status";
	
	// Table Name
	public static final String TABLE_NAME = "vidyoaccounts";
	
	public static final String createCommand = "CREATE TABLE " + 
			TABLE_NAME +
			" ( " +
				ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				NAME + " TEXT NOT NULL, " +
				STATUS + " INTEGER NOT NULL" +
			" );";
				
}
