package com.apareek.rnhvidyoscheduler;

import android.os.Parcel;
import android.os.Parcelable;

public class Options implements Parcelable {
	private int video;
	private int audio;
	private int force;
	private String notes;
	
	public Options() {
		this(0, 0, 0, null);
	}
	
	public Options(int v, int a, int f, String n) {
		this.audio=a;
		this.video=v;
		this.force=f;
		this.notes=n;
	}

	private Options(Parcel in) {
		video = in.readInt();
		audio = in.readInt();
		force = in.readInt();
		this.notes = in.readString();
	}
	
	public int getAudio() {
		return this.audio;
	}

	public int getVideo() {
		return this.video;
	}
	
	public int getForce() {
		return this.force;
	}
	
	public String getNotes() {
		return this.notes;
	}
	
	public void setAudio(int a) {
		this.audio = a;
	}
	
	public void setVideo(int v) {
		this.video = v;
	}
	
	public void setForce(int f) {
		this.force = f;
	}
	
	public void setNotes(String n) {
		this.notes = n;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static final Parcelable.Creator<Options> CREATOR
	    = new Parcelable.Creator<Options>() {
	    public Options createFromParcel(Parcel in) {
	        return new Options(in);
	    }
	
	    public Options[] newArray(int size) {
	        return new Options[size];
	    }
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(video);
		dest.writeInt(audio);
		dest.writeInt(force);
		dest.writeString(notes);
	}
	
}
