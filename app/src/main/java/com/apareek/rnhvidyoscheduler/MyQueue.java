package com.apareek.rnhvidyoscheduler;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.util.Log;

public class MyQueue extends SynchronousQueue<MyPacket> {
	private static final long serialVersionUID = 445435500507300199L;
	private static MyQueue instance = null;
	private static MyPacket myPack = null;
	
	public static MyQueue getInstance() {
		if(instance == null)
			instance = new MyQueue();
		return instance;
	}

	@Override
	public void put(MyPacket o) throws InterruptedException {
		instance.put(o);
	}

	public void put(String s, Handler h) throws InterruptedException {		
		myPack=new MyPacket(s, h);
		instance.put(myPack);
	}
	
	@Override
	public MyPacket take() throws InterruptedException {		
		return instance.take();
	}
	
	
	

}
