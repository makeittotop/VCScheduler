package com.apareek.rnhvidyoscheduler.db.tables;

public class Employee {

	// P.K.
	public static final String ID = "_id";
	
	// User Id
	public static final String USER_ID = "user_id";
	
	// First Name
	public static final String FIRST_NAME = "first_name";
	
	// Last Name
	public static final String LAST_NAME = "last_name";
	
	// Dept Id
	public static final String DEPT_ID = "dept_id";
	
	// Status
	public static final String STATUS = "status";
	
	public static final String LOC_ID = "location_id";

	// Super user
	public static final String SUPER_USER = "super_user";
	
	// Table Name
	public static final String TABLE_NAME = "employee";

	public static final String USER_NAME = "user_name";
	
	// Create Table Query
	public static final String createCommand = "CREATE TABLE " 
			+ TABLE_NAME 
			+ "(" 
				+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ USER_ID + " INTEGER UNIQUE, " 
				+ FIRST_NAME + " TEXT NOT NULL, " 
				+ LAST_NAME + " TEXT NOT NULL, "
				+ USER_NAME + " TEXT NOT NULL, "
				+ DEPT_ID + " INTEGER NOT NULL, " 
				+ STATUS + " TEXT NOT NULL, " 
				+ LOC_ID + " INTEGER NOT NULL, "
				+ SUPER_USER + " INTEGER NOT NULL" 
			+ ");";
	
}
