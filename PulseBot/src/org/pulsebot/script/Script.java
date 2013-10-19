package org.pulsebot.script;

public abstract class Script {

	private boolean running = true;
	private boolean paused = false;
	
	public abstract void onStart();
	public abstract int loop();
	public abstract void onFinish();
	
	public boolean isRunning(){
		return running;
	}
	
	public boolean isPaused(){
		return paused;
	}
	
	public void stop(){
		running = false;
	}
	
	public void pause(){
		paused = !paused;
	}

}

