package com.apareek.rnhvidyoscheduler;

import android.os.Parcel;
import android.os.Parcelable;

public class Vc implements Parcelable {
	private String room;
	private String vidyoRoom;
	private String meetingName;
	private int locationId;
	
	public Vc() {
		this(null, null, null, 0);
	}
	
	public Vc(String r, String vr, String m, int l) {
		this.room=r;
		this.vidyoRoom=vr;
		this.meetingName=m;
		this.locationId=l;
	}
	
	private Vc(Parcel in) {
		this.room = in.readString();
		this.vidyoRoom = in.readString();
		this.meetingName = in.readString();
		this.locationId = in.readInt();
	}

	public String getRoom() {
		return room;
	}
	
	public String getVidyoRoom() {
		return vidyoRoom;
	}
	
	public String getMeetingName() {
		return meetingName;
	}
	
	public int getLocationId() {
		return locationId;
	}
	
	public void setRoom(String r) {
		this.room = r;
	}
	
	public void setVidyoRoom(String vr) {
		this.vidyoRoom = vr;
	}

	public void setMeetingName(String m) {
		this.meetingName = m;
	}
	
	public void setLocationId(int l) {
		this.locationId = l;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Vc> CREATOR
	    = new Parcelable.Creator<Vc>() {
	    public  Vc createFromParcel(Parcel in) {
	        return new Vc(in);
	    }
	    public Vc[] newArray(int size) {
	        return new Vc[size];
	    }
	};
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(room);
		dest.writeString(vidyoRoom);
		dest.writeString(meetingName);
		dest.writeInt(locationId);
	}
}