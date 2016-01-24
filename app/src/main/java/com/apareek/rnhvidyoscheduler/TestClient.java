package com.apareek.rnhvidyoscheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TestClient extends Thread {
	private static Socket clientSocket;
	private static BufferedReader inBuf;
	private static PrintWriter out;
	private static String xmlToWrite;
	private static Handler handler;
	private static String serverMessage;
	private static boolean serverConnected;
	private static boolean networkConnected;
	private static boolean dataReceived;
	private static Object serverConLock;
	private static Object networkConLock;
	private static Object dataLock;
	
	private static TestClient instance = null;
	
	private TestClient() {
		clientSocket = null;		
		networkConnected = false;
		serverConnected = false;
		dataReceived = false;
				
		networkConLock = new Object();
		serverConLock = new Object();
		dataLock = new Object();
		
		out = null;
		inBuf = null;
	}
	
	public static TestClient getInstance(/*String xml, Handler h*/) {
		if(instance == null)
			instance = new TestClient();
				
		return instance;
	}
	
	public static void initClient() {
		try {
			clientSocket = new Socket(SchedulerConstants.ALT_SERVER, SchedulerConstants.PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(clientSocket == null)
			return;
		else {
			serverConnected = true;
		}
		
		try {
			inBuf = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream())
			);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			out = new PrintWriter(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public String talk() {		
		try {
			String serverMessage = inBuf.readLine();
			Log.v("fromServer", serverMessage);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		out.write(xmlToWrite);
		out.flush();
		
		serverMessage = new String();
		String incomingMessage;
		try {
			while((incomingMessage = inBuf.readLine()) != null) {
				serverMessage += incomingMessage + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			out = null;
			inBuf = null;
		}
		
		Log.v("From Server", serverMessage);
		
		return serverMessage;
	}
	
	@Override
	public void run() {
		while(true) {
			// 1
			while(!networkConnected) {
				synchronized (networkConLock) {
					try {
						Log.v(Thread.currentThread().getName(), "Going to wait mode till the network comes up");
						networkConLock.wait();
						Log.v(Thread.currentThread().getName(), "The network is up! Moving to server connection");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			// 2
			while(!serverConnected) {
				initClient();
				
				if(serverConnected)
					break;
				
				synchronized (serverConLock) {
					try {
						Log.v(Thread.currentThread().getName(), "Going to wait mode till the server connection is made");
						serverConLock.wait(SchedulerConstants.MILLI2SLEEP);
						Log.v(Thread.currentThread().getName(), "Server connection established! Moving to data sending mode");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			// 3
//			while(!dataReceived) {
			synchronized (dataLock) {
				try {
					Log.v(Thread.currentThread().getName(), "Going to wait mode till the data becomes available");
					dataLock.wait();
					Log.v(Thread.currentThread().getName(), "Data received. Coming outta wait mode.");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			handleMessage(talk());
//			}
			serverConnected=false;
			dataReceived=false;
		}
	}

	public void setNetStatOn() {
		networkConnected = true;
		
		synchronized (networkConLock) {
			networkConLock.notify();
		}
	}
	
	private void handleMessage(String s) {
		Message msg = new Message();
		Bundle b = new Bundle();
		b.putString(SchedulerConstants.FROM_SERVER_KEY, s);
		msg.setData(b);
		msg.what = SchedulerConstants.CONNECT_TO_SERVER;
		
		handler.sendMessage(msg);
	}

	public static void setInfo(String xml, Handler handler2) {
		xmlToWrite = xml;	
		handler = handler2;
		
		synchronized (dataLock) {
			Log.v(Thread.currentThread().getName(), "Notifying datalock to come outta the wait mode");
			dataLock.notify();
			Log.v(Thread.currentThread().getName(), "Notified");
		}
	}

}
