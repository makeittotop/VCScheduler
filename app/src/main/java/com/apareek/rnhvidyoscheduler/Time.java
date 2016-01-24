package com.apareek.rnhvidyoscheduler;

import android.os.Parcel;
import android.os.Parcelable;

public class Time implements Parcelable {
	private long stime;
	private long etime;
	
	public Time() {
		this(0, 0);
	}
	
	public Time(long s, long e) {
		this.stime=s;
		this.etime=e;
	}

	private Time(Parcel in) {
		stime = in.readLong();
		etime = in.readLong();
	}
	
	public long getStime() {
		return this.stime;
	}
	
	public long getEtime() {
		return this.etime;
	}
	
	public void setStime(long s) {
		this.stime = s;
	}
	
	public void setEtime (long e) {
		this.etime = e;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Time> CREATOR
	    = new Parcelable.Creator<Time>() {
	    public  Time createFromParcel(Parcel in) {
	        return new Time(in);
	    }
	    public Time[] newArray(int size) {
	        return new Time[size];
	    }
	};
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(stime);
		dest.writeLong(etime);
	}
}
