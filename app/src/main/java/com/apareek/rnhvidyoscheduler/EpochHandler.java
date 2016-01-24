package com.apareek.rnhvidyoscheduler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EpochHandler {
	private long epochTime;
	private Calendar calendar = Calendar.getInstance();
	
	private static final int HOUR_CONV = 3600;
	private static final int MINUTE_CONV = 60;
	private static final int SECOND_CONV = 1;
	
	public EpochHandler(int y, int mo, int d, int mi, int h) {
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
		calendar.set(Calendar.YEAR, y);
		calendar.set(Calendar.MONTH, mo);
		calendar.set(Calendar.DAY_OF_MONTH, d);
		calendar.set(Calendar.MINUTE, mi);
		calendar.set(Calendar.HOUR, h);
	}
	
	public void setCalendar(Calendar c) {
		calendar = c;
	}
	
	public long getEpochTime() {
		epochTime = calendar.getTime().getTime()/1000;
		
		return epochTime;
	}
	
	public long addTime(int h, int m) {
		return (epochTime + h * HOUR_CONV + 
			m * MINUTE_CONV);
	}
	
	public long addHours(int h) {
		return (h * HOUR_CONV);
	}
	
	public long addMinutes(int m) {
		return (m * MINUTE_CONV);
	}
	
	public long addSeconds(int s) {
		return (epochTime + s * SECOND_CONV);
	}
	
	static String getFormattedDate(long t) {
        Date date = new Date(t * 1000);
        DateFormat format = new SimpleDateFormat("E, dd/MM/yyyy, HH:mm a");
        format.setTimeZone(TimeZone.getTimeZone("GMT+0530"));
        return format.format(date);
	}
}
