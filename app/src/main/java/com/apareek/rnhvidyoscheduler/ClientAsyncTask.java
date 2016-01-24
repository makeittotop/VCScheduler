package com.apareek.rnhvidyoscheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

public class ClientAsyncTask extends AsyncTask<String, Void, String> {
	private  Socket clientSocket;
	private  BufferedReader inBuf;
	private  PrintWriter out;
	private  String serverMessage;
	
	public ClientAsyncTask() {
		clientSocket = null;		
		
		out = null;
		inBuf = null;
	}
	
	@Override
	protected String doInBackground(String... xmls) {
		if(initClient()) {
			return talk(xmls[0]);
		}
		
		return null;
	}

	
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
	
	public String talk(String xml) {		
		try {
			String serverMessage = inBuf.readLine();
			Log.v("fromServer", serverMessage);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		out.write(xml);
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
}
