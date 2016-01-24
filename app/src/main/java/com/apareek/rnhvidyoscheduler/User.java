package com.apareek.rnhvidyoscheduler;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
	private String name;
	private String password;
	private String fName;
	private String lName;
	private String department;
	private int userId;
	private String email;
	
	public User() {
		this(null, null, null, null
				, null, null, 0);
	}
	
	public User(String n, String f, String l, String e
			, String p, String d, int id) {
		this.name=n;
		this.fName = f;
		this.lName = l;
		this.email=e;
		this.password = p;
		this.userId = id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getFName() {
		return this.fName;
	}

	public String getLName() {
		return this.lName;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getDepartment() {
		return this.department;
	}
	
	public int getUserId() {
		return this.userId;
	}
	
	public void setName(String n) {
		this.name = n;
	}
	
	public void setEmail(String e) {
		this.email = e;
	}
	
	public void setFName(String f) {
		this.fName = f;
	}

	public void setLName(String l) {
		this.lName = l;
	}
	public void setPassword(String p) {
		this.password = p;
	}
	
	public void setDepartment(String d) {
		this.department = d;
	}
	
	public void setUserId(int id) {
		this.userId = id;
	}
	
	private User(Parcel in) {
		this.name = in.readString();
		this.email = in.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(email);
	}
	
	public static final Parcelable.Creator<User> CREATOR
	    = new Parcelable.Creator<User>() {
	    public User createFromParcel(Parcel in) {
	        return new User(in);
	    }
	    public User[] newArray(int size) {
	        return new User[size];
	    }
	};
	
	@Override
	public String toString() {
		return String.format("%s - %s, %s - %s", "Name", name, "Email", email);
	}
}
