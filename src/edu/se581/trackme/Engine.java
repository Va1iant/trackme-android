package edu.se581.trackme;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Engine {

	GPS gps;

	public Engine(Context context) {

	}

	public void StartSession() {

	}

	public void StopSession() {
		locManager.removeUpdates(locListener);
	}

	public void UpdateSession() {
		Location lastKnownLocation = locListener.GetLatestLoc();
	}

	// Define a listener that responds to location updates
	class MyListener implements LocationListener {
		
		Location _loc;
		public Location GetLatestLoc()
		{
			return _loc; 
		}

		@Override
		public void onLocationChanged(Location location) {
			_loc = location;
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

	}

}
