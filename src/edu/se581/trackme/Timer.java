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
    
	
	public Timer(Context context) {
		
		engine = new Engine(context);
		handler = new Handler();
		thread = new Runnable() {
	        @Override
	        public void run() {
	        	engine.UpdateSession();
	            handler.postDelayed(this, GenParam.UpdateLoopInMilisec);
	        }
	    };
	}
	
	public void StartRecording()
	{
		engine.StartSession();
		handler.postDelayed(thread, GenParam.UpdateLoopInMilisec);
	}
	
	public void StopRecording()
	{
		handler.removeCallbacks(thread);
		engine.StopSession();
	}

}
