package com.apareek.rnhvidyoscheduler;

import android.os.Handler;

public class MyPacket {
	String xml;
	Handler handler;
	
	public MyPacket() {
		this(null, null);
	}
	
	public MyPacket(String s, Handler h) {
		this.xml=s;
		this.handler=h;
	}
	
	public String getXml() {
		return this.xml;
	}

	public Handler getHandler() {
		return this.handler;
	}
	
	public void setXml(String s) {
		this.xml=s;
	}
	
	public void setHandler(Handler h) {
		this.handler=h;
	}
}
