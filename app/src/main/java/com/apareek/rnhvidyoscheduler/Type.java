package com.apareek.rnhvidyoscheduler;

import android.os.Parcel;
import android.os.Parcelable;

public class Type implements Parcelable {
	private String request;
	
	public Type() {
		this((String)null);
	}
	
	public Type(String t) {
		this.request=t;
	}

	private Type(Parcel in) {
		this.request = in.readString();
	}
	
	public String getType() {
		return request;
	}
	
	public void setType(String t) {
		this.request = t;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Type> CREATOR
	    = new Parcelable.Creator<Type>() {
	    public  Type createFromParcel(Parcel in) {
	        return new Type(in);
	    }
	    public Type[] newArray(int size) {
	        return new Type[size];
	    }
	};
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(request);
	}
}
