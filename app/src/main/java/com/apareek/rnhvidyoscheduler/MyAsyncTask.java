package com.apareek.rnhvidyoscheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

class MyAsyncTask extends AsyncTask<String, Integer, String> {
	private Socket clientSocket;
	private BufferedReader inBuf;
	private PrintWriter out;
	private Handler handler;
	
	public MyAsyncTask(Handler h) {
		this.handler=h;
	}
	
	@Override
	protected void onPreExecute() {
		handler.sendEmptyMessage(SchedulerConstants.DISPLAY_PROGRESS_DIALOG);
	}

	@Override
	protected String doInBackground(String... xmlData) {
		initClient();
		return talk(xmlData[0]);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(String result) {
		//dialog.dismiss();
		Message msg = new Message();
		Bundle b = new Bundle();
		b.putString(SchedulerConstants.FROM_SERVER_KEY, result);
		msg.setData(b);
		msg.what = SchedulerConstants.CONNECT_TO_SERVER;
		
		handler.sendMessage(msg);
	}

	private void initClient() {
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
	
	public String talk(String xml) {
		String strInMessage;
		
		try {
			strInMessage = inBuf.readLine();
			Log.v("fromServer", strInMessage);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		out.write(xml);
		out.flush();
		
		strInMessage = new String();
		String incomingMessage;
		try {
			while((incomingMessage = inBuf.readLine()) != null) {
				strInMessage += incomingMessage + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Log.v("From Server", strInMessage);
		
		return strInMessage;
		/*
		try {
			
			while((strInMessage = inBuf.readLine()) != null) {
				Log.v(Thread.currentThread().getName(), 
						strInMessage);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			inBuf = null;
			out = null;
			clientSocket = null;
		}
		*/
	}
}
