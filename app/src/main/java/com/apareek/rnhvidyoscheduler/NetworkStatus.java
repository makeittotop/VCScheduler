package com.apareek.rnhvidyoscheduler;

import java.util.HashMap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkStatus {
	private static NetworkStatus instance = null;
	private static Context ctx;
	private static HashMap<String, String> networkStateMap;
	
	private static final String CONNECTED = "CONNECTED";
	private static final String MOBILE = "MOBILE";
	private static final String WIFI = "WIFI";
	
	private NetworkStatus(Context c) {
		ctx=c;
		networkStateMap = acquireAllNetworkStateInfo();	
	}
	
	public static NetworkStatus getInstance(Context c) {
		if(instance == null)
			instance = new NetworkStatus(c);
		
		return instance;
	}
	
	private static HashMap<String, String> acquireAllNetworkStateInfo() {
		HashMap<String, String> networkStateMap = new HashMap<String, String>();
		
		ConnectivityManager conMan = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] networkInfo = conMan.getAllNetworkInfo();
		
		for(NetworkInfo network: networkInfo) {
			Log.v("network", network.getTypeName() + " is " + network.getDetailedState().toString());
			networkStateMap.put(network.getTypeName(), network.getDetailedState().toString());
		}
		
		return networkStateMap;
	}
	
	public boolean getMobileState() {
		acquireAllNetworkStateInfo();
		final String s = networkStateMap.get(MOBILE);
		return s.equalsIgnoreCase(CONNECTED);
	}
	
	public boolean getWifiState() {
		acquireAllNetworkStateInfo();
		return networkStateMap.get(WIFI).equalsIgnoreCase(CONNECTED);
	}	
	
	public boolean getNetworkState() {
		return getMobileState() || getWifiState();
	}
}
