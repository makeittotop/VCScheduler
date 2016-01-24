package com.apareek.rnhvidyoscheduler;

import android.os.Parcel;
import android.os.Parcelable;

public class Vidyo implements Parcelable {
	private User user;
	private Type type;
	private Vc vc;
	private Time time;
	private Notify notify;
	private Options options;
	
	private Action action;

	public static final Parcelable.Creator<Vidyo> CREATOR
	    = new Parcelable.Creator<Vidyo>() {
	    public Vidyo createFromParcel(Parcel in) {
	        return new Vidyo(in);
	    }
	    public Vidyo[] newArray(int size) {
	        return new Vidyo[size];
	    }
	};
	
	private Vidyo(Parcel in) {
		user = in.readParcelable(getClass().getClassLoader());
		type = in.readParcelable(getClass().getClassLoader());
		vc = in.readParcelable(getClass().getClassLoader());
		time = in.readParcelable(getClass().getClassLoader());
		notify = in.readParcelable(getClass().getClassLoader());
		options = in.readParcelable(getClass().getClassLoader());
		
	}

	public Vidyo() {
		user = new User();
		type = new Type();
		vc = new Vc();
		time = new Time();
		options = new Options();
		notify = new Notify();
		action = new Action();
	}


	public User getUser() {
		return this.user;
	}
	
	
	public Type getType() {
		return this.type;
	}
	
	
	public Vc getVc() {
		return this.vc;
	}
	
	public Time getTime() {
		return this.time;
	}
	
	public Notify getNotify() {
		return this.notify;
	}
	
	public Options getOptions() {
		return this.options;
	}
	
	public Action getAction() {
		return this.action;
	}
	
	/*
	public void setUser() {
		
	}
	*/
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(user, flags);
		dest.writeParcelable(type, flags);
		dest.writeParcelable(vc, flags);
		dest.writeParcelable(time, flags);
		dest.writeParcelable(notify, flags);
		dest.writeParcelable(options, flags);
	}
}
