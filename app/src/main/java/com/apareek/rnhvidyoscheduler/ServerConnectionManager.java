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

public class ServerConnectionManager implements Runnable {
	private Thread clientThread;
	private Socket clientSocket;
	private BufferedReader inBuf;
	private PrintWriter out;
	private String xmlToWrite;
	private Handler handler;
	private String serverMessage;

	public ServerConnectionManager(String xml, Handler h) {
		clientThread = null;
		clientSocket = null;
		xmlToWrite = xml;
		this.handler = h;
	}
	
	public void go() {
		clientThread = new Thread(this);
		clientThread.start();
	}
	
	public void initClient() {
		try {
			clientSocket = new Socket(SchedulerConstants.ALT_SERVER, SchedulerConstants.PORT);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(clientSocket == null)
			return;
		
		try {
			inBuf = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream())
			);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		try {
			out = new PrintWriter(clientSocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		
		Log.v("From Server", serverMessage);
		
		return serverMessage;
	}
	
	@Override
	public void run() {
		initClient();
		handleMessage(talk());
	}

	private void handleMessage(String s) {
		Message msg = new Message();
		Bundle b = new Bundle();
		b.putString(SchedulerConstants.FROM_SERVER_KEY, s);
		msg.setData(b);
		msg.what = SchedulerConstants.CONNECT_TO_SERVER;
		
		handler.sendMessage(msg);
	}

}
