package org.pulsebot.injection.generic;

import java.awt.Canvas;
import java.awt.Graphics;


/**
 * @author SunJava, modified by JJ
 */
public class RSCanvas extends Canvas {

	private static final long serialVersionUID = -8924164166965299117L;
	private static RSClient client;
	
	public static void setClient(RSClient _client){
		client = _client;
	}
	
	@Override
	public Graphics getGraphics() {
		Graphics g = super.getGraphics();
		return client != null ? client.drawGraphics(g) : g;
	}
	
	@Override
    public void setLocation(int x, int y)  {
		// Fixes the bouncing of the applet
	}

}
