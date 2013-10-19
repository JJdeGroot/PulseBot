package org.pulsebot.script;

import org.pulsebot.utils.Utilities;

public class ScriptThread extends Thread {

	private Script script;
	
	public ScriptThread(Script script){
		super("Script");
		this.script = script;
		setDaemon(true);
	}
	
	@Override
	public void run() {
		script.onStart();
		while(script.isRunning() && !Thread.currentThread().isInterrupted()){
			if(script.isPaused()){
				Utilities.sleep(10);
				continue;
			}
			Utilities.sleep(script.loop());
		}
	}
	
}
