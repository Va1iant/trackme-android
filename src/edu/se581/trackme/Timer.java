/**
 * 
 */
package edu.se581.trackme;

import android.content.Context;
import android.os.Handler;

/**
 * @author a50140
 *
 */
public class Timer {
	Engine engine;
	Handler handler;
	Runnable thread;
	Map map;
	
	boolean isRecording;
    
	public Timer(MainActivity context) {
		
		map = new Map(context);
		engine = new Engine(context);
		handler = new Handler();
		thread = new Runnable() {
	        @Override
	        public void run() {
	        	engine.UpdateSession();
	            handler.postDelayed(this, GenParam.UpdateLoopInMilisec);
	        }
	    };
	    
	    isRecording = false;
	}
	
	public boolean StartRecording()
	{
		if(engine.StartSession())
		{
			handler.postDelayed(thread, GenParam.UpdateLoopInMilisec);
			isRecording = true;
			return true;
		}
		else
			return false;
		
	}
	
	public void StopRecording()
	{
		handler.removeCallbacks(thread);
		engine.StopSession();
		isRecording = false;
	}
	
	public boolean IsRecording()
	{
		return isRecording;
	}

}
