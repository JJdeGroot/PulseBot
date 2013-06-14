package org.pulsebot.loader;

import org.pulsebot.injection.input.PulseMouseListeners;
import org.pulsebot.injection.interfaces.ClientInterface;
import org.pulsebot.injection.interfaces.Player;
import org.pulsebot.loader.ui.OverviewPanel;

import java.applet.Applet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * Describes a RuneScape Client
 * @author JJ
 */
public class Client {

	private ClientApplet loader = null;
	private BufferedImage gameBuffer = null, paintBuffer = null;
	private OverviewPanel overviewPanel;

	/**
	 * Creates a new Client
	 * @param link link to load
	 * @param width client width
	 * @param height client height
	 */
	public Client(String link, int width, int height, OverviewPanel overviewPanel) {
		this.overviewPanel = overviewPanel;
		this.loader = new ClientApplet(link, width, height, true);

		// Gives our buffers the same size as the applet's canvas
		this.gameBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.paintBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		System.out.println("Starting applet");
		this.loader.start();
        while(getCanvas() == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        PulseMouseListeners m = new PulseMouseListeners(getCanvas(),this);
        m.windMouse(200,200,10);
	}
	
	/**
	 * Returns the canvas
	 * @return canvas
	 */
	public Canvas getCanvas(){
		Applet applet = getApplet();
		return applet.getComponentCount() > 0 ? (Canvas) applet.getComponent(0) : null;
	}
	
	/**
	 * Returns the loader
	 * @return loader
	 */
	public ClientApplet getLoader(){
		return loader;
	}
	
	/**
	 * Returns the applet
	 * @return applet
	 */
	public Applet getApplet(){
		return loader != null ? (Applet) this.loader.getComponent(0) : null;
	}
	
	/**
	 * Makes a copy of a BufferedImage
	 * @param image image to copy
	 * @return copy of image
	 */
	private BufferedImage deepCopy(BufferedImage image){
		ColorModel cm = image.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = image.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	/**
	 * Returns the game buffer
	 * @return game buffer
	 */
	public BufferedImage getGameBuffer(){
		return deepCopy(gameBuffer);
	}
	
	/**
	 * Returns the paint buffer
	 * @return paint buffer
	 */
	public BufferedImage getPaintBuffer(){
		return deepCopy(paintBuffer);
	}


    private String getMyPlayer(Player players[]){
        if(players.length>0)
            for(int j = 0;j<players.length;j++){
                if(players[j]!=null&&j==players.length-1)
                    return players[j].getPlayerName();
            }
        return "null";
    }
    /**
     * Custom implementation of drawing the graphics
     * @param g graphics to draw with
     * @return graphics we have drawn
     */
	public Graphics drawGraphics(Graphics2D g){
		Graphics paintGraphics = paintBuffer.getGraphics();
		paintGraphics.drawImage(gameBuffer, 0, 0, null);
		Player[] playerArr = ((ClientInterface) getApplet()).getPlayerArray();
        paintGraphics.drawString(getMyPlayer(playerArr),200,200);
		paintGraphics.setColor(Color.WHITE);
		paintGraphics.drawString("Custom drawing hashcode #" + getCanvas().getClass().getClassLoader().hashCode(), 100, 100);
		paintGraphics.dispose();

		if(g != null){
			g.drawImage(paintBuffer, 0, 0, null);
		}
		
		overviewPanel.update(getCanvas().getClass().getClassLoader().hashCode());
		return gameBuffer.getGraphics();
	}
	
	
}
