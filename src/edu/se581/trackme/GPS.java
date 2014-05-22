package edu.se581.trackme;

import edu.se581.trackme.Engine.MyListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
	Context context;

	public GPS(Context paramContext) {

		context = paramContext;
		// Acquire a reference to the system Location Manager
		locManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		// set default to Network
		locProvider = LocationManager.NETWORK_PROVIDER;

		// warning, this value can be null
		currentLoc = locManager.getLastKnownLocation(provider);

		// this will check if we can use other provider (e.g. GPS)
		decideProvider();

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location newLoc) {
				if (isBetterLocation(newLoc, currentLoc))
					currentLoc = newLoc;

			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
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

		};

	}

	public Location GetStartLocation() {
		
		loca
		
		// Register the listener with the Location Manager to receive location updates
		locManager.requestLocationUpdates(provider, 0, 0, locationListener);
		return null;
	}

	public Location GetUpdatedLocation() {
		return curLoc;
	}

	public Location GetStopLocation() {
		return null;
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
			Location currentBestLocation) {
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
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	public void offerGpsEnableDialog() {

		// ask user if wanted to enable GPS. if not just use network provided
		// location
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder
				.setMessage(
						"GPS service is disabled in your device. What do you want to do?")
				.setCancelable(false)
				.setPositiveButton("Enable in Location Setting",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								((Activity) mClient)
										.startActivity(callGPSSettingIntent);
								mOfferGPSAlert = null;
							}
						});
		alertDialogBuilder.setNegativeButton("Use Network Location Service",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						mOfferGPSAlert = null;
					}
				});

		mOfferGPSAlert = alertDialogBuilder.create();
		mOfferGPSAlert.show();
	}

}
