package edu.se581.trackme;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/*
 * This is the wrapper class for GPS
 */
public class GPS {

	String locProvider;
	LocationManager locManager;
	LocationListener locListener;
	Location currentLoc;
	MainActivity context;
	
	boolean isEnabled = false;

	public GPS(MainActivity paramContext) {

		context = paramContext;
		// Acquire a reference to the system Location Manager
		locManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		// set default to GPS
		locProvider = LocationManager.GPS_PROVIDER;
		
		if(locManager.isProviderEnabled(locProvider)){
			// warning, this value can be null
			currentLoc = locManager.getLastKnownLocation(locProvider);
			isEnabled = true;
		}
		else
		{
			TMLog.showToast(context, "Please Enable GPS first");
			isEnabled = false;
		}

		

		// this will check if we can use other provider (e.g. GPS)
		// decideProvider();

		// Define a listener that responds to location updates
		locListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location newLoc)
			{
				//TMLog.debug("NEW LOCATION!");
				if (isBetterLocation(newLoc, currentLoc))
					currentLoc = newLoc;

			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider)
			{
				// TODO Auto-generated method stub

			}

		};

	}

	public Location GetLastKnownLocation()
	{
		return locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	}
	public Location GetStartLocation()
	{
		// Register the listener with the Location Manager to receive location
		// updates
		if(isEnabled)
		{
			locManager.requestLocationUpdates(locProvider, 0, 0, locListener);
			return locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		else
		{
			TMLog.showToast(context, "Please Enable GPS first");
			return null;
		}
	}

	public Location GetUpdatedLocation()
	{
		return currentLoc;
	}

	public Location GetStopLocation()
	{
		locManager.removeUpdates(locListener);
		return currentLoc;
	}

	private static final int TWO_MINUTES = 1000 * 60 * 2;

	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	protected boolean isBetterLocation(Location location,
			Location currentBestLocation)
	{
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2)
	{
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

}
