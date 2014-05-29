package edu.se581.trackme;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import edu.se581.trackme.WebProxy.OnFinishProcessHttp;
import android.app.Activity;
import android.content.Context;
import android.location.Location;

public class Engine {

	GPS gps;
	WebProxy web;
	String activeSessionId;
	Map map;
	
	double activeSessionDist;
	Location lastRecordedLoc;
	MainActivity mContext;

	public Engine(MainActivity context) {
		
		mContext = context;
		map = new Map(mContext);
		gps = new GPS(mContext);
		web = new WebProxy();
		activeSessionId = null; // only filled when we got something back from TrackMe
								// web during start session
		map.showLocation(gps.GetLastKnownLocation());
		
		activeSessionDist = 0;
		lastRecordedLoc = null;
	}

	public boolean StartSession()
	{
		TMLog.debug("StartSession");
		
		Location startLoc = gps.GetStartLocation();
		if(startLoc == null)
			return false;
		
		map.showLocation(startLoc);
		
		web.post(URIBuilder.toStartSession(), "", new OnFinishProcessHttp() {
			@Override
			public void onFinishProcessHttp(int statusCode, String responseBody)
			{
				activeSessionId = null;

				if (statusCode == HttpStatus.SC_OK) {
					try {
						JSONObject json = new JSONObject(responseBody);
						activeSessionId = json.getString("session_id");
						
						TMLog.debug("activeId = " + activeSessionId);
						TMLog.showToast(mContext, "See live @\r\n" + URIBuilder.toViewLive());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						TMLog.err("JSON parse fail");
					}
				}
			}
		});
		
		return true;
	}
	
	public void UpdateSession()
	{
		TMLog.debug("UpdateSession");

		if(activeSessionId == null)
		{	
			TMLog.err("no active session is running");
			return;			
		}
		
		// get location
		Location currentLoc = gps.GetUpdatedLocation();
		
		map.showLocation(currentLoc);
		
		// get total distance
		if(lastRecordedLoc != null)
			activeSessionDist += currentLoc.distanceTo(lastRecordedLoc);
		
		web.post(URIBuilder.toPutSession(activeSessionId, currentLoc, activeSessionDist), "", null);
		
		lastRecordedLoc = currentLoc;
	}

	public void StopSession()
	{
		TMLog.debug("StopSession");
		
		if(activeSessionId == null)
		{	
			TMLog.err("no active session is running");
			return;			
		}
		
		map.showLocation(gps.GetStopLocation());

		// get location
		web.post(URIBuilder.toEndSession(activeSessionId), "", new OnFinishProcessHttp() {
			@Override
			public void onFinishProcessHttp(int statusCode, String responseBody)
			{
				if (statusCode == HttpStatus.SC_OK) {
					
					TMLog.showToast(mContext, "See session history @ " + URIBuilder.toViewHistory());
				}
			}
		});
		
		activeSessionId = null;
		activeSessionDist = 0;
		lastRecordedLoc = null;
	}



}
