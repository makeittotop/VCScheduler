package com.apareek.rnhvidyoscheduler;

import android.os.Parcel;
import android.os.Parcelable;

public class Notify implements Parcelable {
	private int snotify;
	private String phonenos;
	private String participants;
	
	public Notify() {
		this(0, null, null);
	}

	public Notify(int s, String phones, String part) {
		this.snotify=s;
		this.phonenos=phones;
		this.participants=part;
	}

	private Notify(Parcel in) {
		this.snotify = in.readInt();
		this.phonenos = in.readString();
		this.participants = in.readString();
	}
	
	public int getSnotify() {
		return this.snotify;
	}
	
	public String getPhonenos() {
		return this.phonenos;
	}

	public String getParticipants() {
		return this.participants;
	}
	
	public void setSnotify(int s) {
		this.snotify = s;
	}
	
	public void setPhonenos(String ph) {
		this.phonenos = ph;
	}
	
	public void setParticipants(String pa) {
		this.participants=pa;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Notify> CREATOR
	    = new Parcelable.Creator<Notify>() {
	    public  Notify createFromParcel(Parcel in) {
	        return new Notify(in);
	    }
	    public Notify[] newArray(int size) {
	        return new Notify[size];
	    }
	};
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(snotify);
		dest.writeString(phonenos);
		dest.writeString(participants);
	}
}
