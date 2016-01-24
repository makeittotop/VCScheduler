package com.apareek.rnhvidyoscheduler;

import java.util.ArrayList;

public final class SchedulerConstants {
	public static final String VIDYO_DATA_KEY = "vidyo_data";
	
	public static final String SERVER = "192.168.29.45";
	public static final String ALT_SERVER = "enceladus.riyan.co.in";
	public static final String BOGUS_SERVER = "foobar.in";

	public static final int PORT = 10000;
	
	public static final String  SELECT_ROOM = "Select a room";
	
	public static final String PREFS_NAME = "userPrefs";
	
	public static final String USERNAME = "username";
	public static final String EMAIL = "email";
	public static final String FNAME = "fname";
	public static final String LNAME = "lname";
	public static final String DEPARTMENT = "department";
	public static final String USERID = "userid";
	public static final String PASSWORD = "password";
	
	public static final int CONNECT_TO_SERVER = 0;
	public static final int DISPLAY_PROGRESS_DIALOG = 1;
	public static final int GET_RESULT_FROM_SERVER = 2;
	
	public static final String FROM_SERVER_KEY = "from_server_key";

	public static final int NO_NETWORK = 0;

	public static final int FOUND_NETWORK = 1;
	public static final long MILLI2SLEEP = 10000;
	
	public enum PROTOCOL {
		CONNECT_TO_SERVER
				
	}


}
