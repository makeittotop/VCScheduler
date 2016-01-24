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
import android.widget.Toast;

public class Client extends Thread {
	private  Socket clientSocket;
	private  BufferedReader inBuf;
	private  PrintWriter out;
	private  String xmlToWrite;
	private  Handler handler;
	private  String serverMessage;
	
	//private static Client instance = null;
	
	public Client(String xml, Handler h) {
		clientSocket = null;		

		handler = h;
		xmlToWrite = xml;	
		
		out = null;
		inBuf = null;
	}

	/*
	public static Client getInstance(String xml, Handler h) {
		if(instance == null)
			instance = new Client(xml, h);
				
		return instance;
	}
*/
	
	public boolean initClient() {
		try {
			clientSocket = new Socket(SchedulerConstants.ALT_SERVER, SchedulerConstants.PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(clientSocket == null)
			return false;
		
		try {
			inBuf = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream())
			);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		try {
			out = new PrintWriter(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
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
		if(initClient())
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
