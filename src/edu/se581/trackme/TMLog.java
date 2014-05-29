package edu.se581.trackme;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class TMLog {


	public static void showToast(Activity context, String txt, int duration,
			int gravity)
	{
		Toast toast = Toast.makeText(context.getBaseContext(), txt, duration);
		toast.setGravity(gravity, 0, 150);
		toast.show();
	}

	public static void showToast(Activity context, String txt)
	{
		Toast toast = Toast.makeText(context.getBaseContext(), txt,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}

	public static void debug(String txt)
	{
		Log.i("QQQ", txt);
	}

	public static void err(String txt)
	{
		Log.e("QQQ", Thread.currentThread().getStackTrace()[3].toString() + "|"
				+ txt);
	}

	public static void showDebugMethodInfo(Object o)
	{

		debug(o.getClass().getName() + "|"
				+ Thread.currentThread().getStackTrace()[3].getMethodName());
	}

	public static void showDebugWarning(String txt)
	{
		Log.w("QQQ", txt);
	}

	public static void showDebugMethodInfo(Object o, String data)
	{
		debug(o.getClass().getName() + "|"
				+ Thread.currentThread().getStackTrace()[3].getMethodName()
				+ "|" + data);
	}
}
