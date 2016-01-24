package com.apareek.rnhvidyoscheduler;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class CopyOfNetworkConnectionManager implements Runnable {
	private Context context;
	private Handler handler;
	private static final int MILLI2SLEEP = 10000;
	private boolean onlineStatus;
	private Object onlineStatusLock;
	private TestClient client;
	
	public CopyOfNetworkConnectionManager(Context c, Handler h) {
		this.context=c;
		this.handler=h; 
		onlineStatus = false;
		this.onlineStatusLock = new Object();
	}
	
	@Override
	public void run() {
		while(!onlineStatus) {
			Log.v(Thread.currentThread().getName(), "Running");
			onlineStatus = networkStatus();
			synchronized (onlineStatusLock) {
				try {
					if(onlineStatus) {
						// Set the server connector thread in motion
						client = TestClient.getInstance();
						client.setNetStatOn();
						
						Log.v(Thread.currentThread().getName(), "Going down to wait mode INDEFINITELY");
						onlineStatusLock.wait();
						Log.v(Thread.currentThread().getName(), "Coming out of wait mode");
					} 
					else {
						Log.v(Thread.currentThread().getName(), "Going down to wait mode for 10s");
						onlineStatusLock.wait(MILLI2SLEEP);
						Log.v(Thread.currentThread().getName(), "Coming out of wait mode after 10s");
					}
				} catch (InterruptedException e) {
					Log.v(Thread.currentThread().getName(), "Interrupted! What do I do now?");
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setNetStatOff() {
		onlineStatus = false;
		
		synchronized (onlineStatusLock) {
			Log.v(Thread.currentThread().getName(), "Notifying to come outta wait");
			onlineStatusLock.notify();
			Log.v(Thread.currentThread().getName(), "Notified successfully");
		}
	}

	private boolean networkStatus() {
		if(NetworkStatus.getInstance(context).getNetworkState()) {
			handler.sendEmptyMessage(SchedulerConstants.FOUND_NETWORK);
			return true;
		}
		else {
			handler.sendEmptyMessage(SchedulerConstants.NO_NETWORK);
			return false;
		}	
	}

}
