package com.apareek.rnhvidyoscheduler;

public class Action {
	private char type;
	private int rid;
	private int status;
	private String info;
	private String payload;
	
	public Action() {
		this('\0', 0, 0, null, null);
	}

	public Action(final char t, final int id, final int s, final String info,
			final String p) {
		this.type = t;
		this.rid = id;
		this.status = s;
		this.info = info;
		this.payload = p;
	}
	
	public char getType() {
		return this.type;
	}
	
	public int getRid() {
		return rid;
	}
	
	public  int getStatus() {
		return this.status;
	}
	
	public  String getInfo() {
		return info;
	}
	
	public  String getPayload() {
		return this.payload;
	}
	
	public void setType(char t) {
		this.type = t;
	}
	
	public void setRid(int r) {
		this.rid = r;
	}
	
	public void setInfo(String i) {
		this.info = i;
	}
	
	public void setPayload(String p) {
		this.payload = p;
	}	
	
	public void setStatus(int s) {
		this.status = s;
	}
}
