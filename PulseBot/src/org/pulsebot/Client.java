package org.pulsebot;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPanel;

import org.pulsebot.utils.Utilities;

public class Client extends JPanel implements AppletStub {

	private static final long serialVersionUID = 3616466239292259942L;

	private final String rsLink = "http://oldschool11.runescape.com/";
	private final Pattern codeRegex = Pattern.compile("code=(.*) ");
	private final Pattern archiveRegex = Pattern.compile("archive=(.*) ");
	private final Pattern paramRegex = Pattern.compile("<param name=\"([^\\s]+)\"\\s+value=\"([^>]*)\">");

	private HashMap<String, String> parameters = new HashMap<String, String>();
	private Applet applet = null;
    private URLClassLoader classLoader = null;
    private URL codeBase = null, documentBase = null;
    
    private static BufferedImage gameBuffer = new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);
    private static BufferedImage paintBuffer = new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);

    public Client(){
    	// Setting layout and size
    	setLayout(new BorderLayout(0, 0));
    	setPreferredSize(new Dimension(765, 503));

    	// Finding the archive location and code name
    	String pageSource = Utilities.getPage(rsLink);
        Matcher archiveMatcher = archiveRegex.matcher(pageSource);
        Matcher codeMatcher = codeRegex.matcher(pageSource);
        if (archiveMatcher.find() && codeMatcher.find()) {
			// Link to the RuneScape JAR
            String jarLink = rsLink + archiveMatcher.group(1);
            System.out.println("RuneScape jar: " + jarLink);
            
            // Name of the executable class in the JAR
            String codeName = codeMatcher.group(1).replace(".class", "");
            System.out.println("Main class: " + codeName);
    
	        try {   
	        	// Setting code and document bvase
	            codeBase = new URL(jarLink);
	            documentBase = new URL(rsLink);
	            
	            // Finding JAR parameters
	            Matcher paramMatcher = paramRegex.matcher(pageSource);
	            while (paramMatcher.find()) {
	            	String key = paramMatcher.group(1);
	            	String value = paramMatcher.group(2);
	                parameters.put(key, value);
	            }
	            System.out.println("Parameters: " + parameters);
	            
	            // Downloading and starting the RuneScape JAR
	            Utilities.downloadFile(jarLink, "./gamepack.jar");
	            classLoader = new URLClassLoader(new URL[]{new URL("file:gamepack.jar")});
                
	            applet = (Applet) classLoader.loadClass(codeName).newInstance();
                applet.setStub(this);
                applet.init();
    			applet.start();

    			add(applet, BorderLayout.CENTER);
	    	} catch (Exception e) {
				System.out.println("Error constructing client");
				e.printStackTrace();
			}
		}
    }
    
    public Applet getApplet(){
    	return applet;
    }
    
    public Canvas getCanvas(){
    	return applet.getComponentCount() > 0 ? (Canvas) applet.getComponent(0) : null;
    }
    
    public static Graphics drawGraphics(Graphics g) {
    	Graphics paintGraphics = paintBuffer.getGraphics();
		
    	paintGraphics.drawImage(gameBuffer, 0, 0, null);
		paintGraphics.setColor(Color.WHITE);
		paintGraphics.drawString("Hello PulseBot!", 100, 100);
		paintGraphics.dispose();

		g.drawImage(paintBuffer, 0, 0, null);

		return gameBuffer.getGraphics();
    }
    
    public Object findObjectFromPath(Object o, String path) throws Exception {
        String[] parts = path.split("\\.");
        Stack<String> stack = new Stack<String>();
        Field field;
        if (o == null) {
            Class<?> c = classLoader.loadClass(parts[0]);
            for (int i = parts.length - 1; i > 0; i--) {
                stack.push(parts[i]);
            }
            field = c.getDeclaredField(stack.pop());
            field.setAccessible(true);
            o = field.get(null);
        } else {
            for (int i = parts.length - 1; i >= 0; i--) {
                stack.push(parts[i]);
            }
        }
        if (!stack.empty()) {
            while (!stack.empty()) {
                String theField = stack.pop();
                Class<?> theClass = o.getClass();
                field = null;
                while (field == null && theClass != Object.class) {
                    try {
                        field = theClass.getDeclaredField(theField);
                    } catch (Exception e) {
                        try {
                            theClass = theClass.getSuperclass();
                        } catch (Exception x) {
                            break;
                        }
                    }
                }
                field.setAccessible(true);
                o = field.get(o);
            }
        }
        return o;
    }
    
    @Override
	public void appletResize(int x, int y) {
		if(applet != null){
			applet.setSize(x, y);
		}
	}

	@Override
	public AppletContext getAppletContext() {
		return applet != null ? applet.getAppletContext() : null;
	}

	@Override
	public URL getCodeBase() {
		return codeBase;
	}

	@Override
	public URL getDocumentBase() {
		return documentBase;
	}

	@Override
	public String getParameter(String key) {
		return parameters.get(key);
	}

	@Override
	public boolean isActive() {
		return true;
	}
    
}
