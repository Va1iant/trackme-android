package edu.se581.trackme;

import android.location.Location;

public class URIBuilder {

	static String startSessionFmt = "mapi/start/user/%s";
	static String putSessionFmt = "mapi/put/user/%s/session/%s/lat/%s/lng/%s/tdist/%s";
	static String endSessionFmt = "mapi/end/user/%s/session/%s";
	
	static String viewSessionFmt = "live/user/%s";
	static String viewHistoryFmt = "history/user/%s";

	public URIBuilder() {
		// TODO Auto-generated constructor stub
	}

	public static String toStartSession()
	{
		return GenParam.WebUrl + "/"
				+ String.format(startSessionFmt, GenParam.UserId);
	}
	
	public static String toViewLive()
	{
		return GenParam.WebUrl + "/"
				+ String.format(viewSessionFmt, GenParam.UserId);
	}
	
	public static String toViewHistory()
	{
		return GenParam.WebUrl + "/"
				+ String.format(viewHistoryFmt, GenParam.UserId);
	}

	public static String toPutSession(String sessionId, Location loc,
			double totalDistance)
	{
		return GenParam.WebUrl
				+ "/"
				+ String.format(putSessionFmt, GenParam.UserId, sessionId,
						loc.getLatitude(), loc.getLongitude(), totalDistance);
	}

	public static String toEndSession(String sessionId)
	{
		return GenParam.WebUrl + "/"
				+ String.format(endSessionFmt, GenParam.UserId, sessionId);
	}

}
