package com.apareek.rnhvidyoscheduler;

import java.lang.reflect.Field;

import android.drm.DrmStore.Action;
import android.util.Log;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class XMLHandler {
	private Vidyo vidyo;
	private XStream xstream;
	
	public XMLHandler() {
		this.vidyo = new Vidyo();
		this.xstream= new XStream();
	}
	
	public XMLHandler(Vidyo v) {
		this.vidyo = v;
		this.xstream= new XStream();
		
		alias();
	}
	
	private void alias() {
		Class<?> c;
		Field[] fieldsArray = null;
		
		try {
			c = Class.forName(vidyo.getClass().getCanonicalName());
			fieldsArray = c.getDeclaredFields();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Field f : fieldsArray) {
			Log.v("field", f.getName());
		}
		
		xstream.alias("vidyo", Vidyo.class);
		xstream.alias("user", User.class);
		xstream.alias("type", Type.class);
		xstream.alias("time", Time.class);
		xstream.alias("notify", Notify.class);
		xstream.alias("options", Options.class);
		xstream.alias("action", Action.class);
	}
	
	/*
	private void addTag(String tag) {
		xstream.alias(tag, type)
	}
	*/
	
	public String vidyoToXml() {
		return xstream.toXML(vidyo);
	}
	
	public Vidyo xmlToVidyo(String xml) {
		this.xstream.alias("vidyo", Vidyo.class);
		this.xstream.alias("action", Action.class);
		
		return (Vidyo) xstream.fromXML(xml);
	}
}
