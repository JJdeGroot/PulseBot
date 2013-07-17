package org.pulsebot.injection.generic;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.objectweb.asm.tree.ClassNode;
import org.pulsebot.injection.impl.*;
import org.pulsebot.injection.interfaces.IClient;
import org.pulsebot.injection.interfaces.INPC;
import org.pulsebot.injection.interfaces.IPlayer;
import org.pulsebot.injection.utils.JarUtils;
import org.pulsebot.utils.Utilities;

public class RSClient extends JPanel implements AppletStub {

	private static final long serialVersionUID = 3057153191784972138L;
	
	private final String rsLink = "http://oldschool11.runescape.com/";
	private final Pattern codeRegex = Pattern.compile("code=(.*) ");
	private final Pattern archiveRegex = Pattern.compile("archive=(.*) ");
	private final Pattern paramRegex = Pattern.compile("<param name=\"([^\\s]+)\"\\s+value=\"([^>]*)\">");

	// LOADER
	private HashMap<String, String> parameters = new HashMap<String, String>();
	private Applet applet = null;
    private URLClassLoader classLoader = null;
    private URL codeBase = null, documentBase = null;
    // INJECTION
    // Canvas related
 	public static final CanvasAnalyzer canvasAnalyzer = new CanvasAnalyzer();
 	// Node related
 	public static final NodeAnalyzer nodeAnalyzer = new NodeAnalyzer();
 	public static final CacheableNodeAnalyzer cacheableNodeAnalyzer = new CacheableNodeAnalyzer();
 	public static final CacheableNodeQueueAnalyzer cacheableNodeQueueAnalyzer = new CacheableNodeQueueAnalyzer();
 	public static final NodeHashTableAnalyzer nodeHashTableAnalyzer = new NodeHashTableAnalyzer();
 	public static final NodeQueueAnalyzer nodeQueueAnalyzer = new NodeQueueAnalyzer();
 	public static final NodeCacheAnalyzer nodeCacheAnalyzer = new NodeCacheAnalyzer();
 	// Renderable related
 	public static final RenderableAnalyzer renderableAnalyzer = new RenderableAnalyzer();
 	public static final ActorAnalyzer actorAnalyzer = new ActorAnalyzer();
 	public static final ModelAnalyzer modelAnalyzer = new ModelAnalyzer();
 	// Player related
 	public static final PlayerAnalyzer playerAnalyzer = new PlayerAnalyzer();
 	public static final PlayerDefinitionAnalyzer playerDefinitionAnalyzer = new PlayerDefinitionAnalyzer();
 	// NPC related
 	public static final NPCAnalyzer npcAnalyzer = new NPCAnalyzer();
 	public static final NPCDefinitionAnalyzer npcDefinitionAnalyzer = new NPCDefinitionAnalyzer();
 	// Game related
 	public static final ClientAnalyzer clientAnalyzer = new ClientAnalyzer();
 	public static final GameShellAnalyzer gameShellAnalyzer = new GameShellAnalyzer();
 	// GRAPHICS
 	private BufferedImage gameBuffer = new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);
 	private BufferedImage paintBuffer = new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);

    /**
     * Constructs a new Oldschool RuneScape client
     * -----------------------------------------------------
     * At first the location of the gamepack is determined
     * Secondly the gamepack is downloaded and investigated
     * Finally, the applet is constructed and started
     */
	public RSClient(){
		RSCanvas.setClient(this);
		setLayout(new BorderLayout(0, 0));
		setPreferredSize(new Dimension(765, 503));
  
		// Finding the archive location and code name
		try { 
			String pageSource = Utilities.getPage(rsLink);

			// Searching for the archive and code patterns
	        Matcher archiveMatcher = archiveRegex.matcher(pageSource);
	        Matcher codeMatcher = codeRegex.matcher(pageSource);
	        if (archiveMatcher.find() && codeMatcher.find()) {
				// Link to the RuneScape JAR
	            String jarLink = rsLink + archiveMatcher.group(1);
	            System.out.println("Jar location: " + jarLink);
	            
	            // Name of the executable class in the JAR
	            String codeName = codeMatcher.group(1).replaceAll(".class", "");
	            System.out.println("Code name: " + codeName);
	            
	            codeBase = new URL(jarLink);
	            documentBase = new URL(rsLink);
	            
	            // Finding JAR parameters
	            System.out.println("\nLoading parameters...");
                Matcher paramMatcher = paramRegex.matcher(pageSource);
                while (paramMatcher.find()) {
                	String key = paramMatcher.group(1);
                	String value = paramMatcher.group(2);
                	System.out.printf("%-20s %s", key, value + "\n");
                    parameters.put(key, value);
                }
	            

                // Downloading and injecting gamepack
                Utilities.downloadFile(jarLink, "./gamepack.jar");
                ;

                // Loading local gamepack
                HashMap<String, ClassNode> classMap = JarUtils.parseJar(new JarFile("gamepack.jar"));
                runAnalyzers(classMap);
                JarUtils.updateJar(classMap, new JarFile("gamepack.jar"));
                
                // Constructs the RuneScape applet               
                classLoader = new URLClassLoader(new URL[]{new URL("file:injected.jar")});
                applet = (Applet) classLoader.loadClass(codeName).newInstance();
                applet.setStub(this);

                // Starts the RuneScape applet
                applet.init();
    			applet.start();
                add(applet, BorderLayout.CENTER);
			}
		} catch (Exception e) {
			System.out.println("Error constructing client");
			e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading... please check your internet connection.", "Error loading..", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Returns the injected RSCanvas
	 * @return injected RSCanvas
	 */
	public RSCanvas getCanvas() {
		return (RSCanvas) applet.getComponent(0);
	}
    public Applet getApplet(){
        return applet;
    }
	
	/**
	 * Returns the image of RuneScape
	 * @return RuneScape image
	 */
	public BufferedImage getImage() {
		BufferedImage img = new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);
		img.getGraphics().drawImage(gameBuffer, 0, 0, null);
		return img;
	}
	
	/**
     * Custom implementation of drawing the graphics
     *
     * @param g graphics to draw with
     * @return graphics we have drawn
     */
    public Graphics drawGraphics(Graphics g) {
        Graphics paintGraphics = paintBuffer.getGraphics();
        paintGraphics.drawImage(gameBuffer, 0, 0, null);
        paintGraphics.setColor(Color.WHITE);

        // Player array
        IPlayer[] players = ((IClient) applet).getPlayers();
        paintGraphics.drawString("Players: " + players, 10, 150);
        paintGraphics.drawString("Players length: " + players.length, 10, 170);
        int playerCount = 0;
        for(IPlayer player : players){
        	if(player != null){
        		paintGraphics.drawString("Player \"" + player.getName() + "\"", 200, 10+playerCount*20);
        		playerCount++;
        	}
        }
 
        // NPC array
        INPC[] npcs = ((IClient) applet).getNPCs();
        paintGraphics.drawString("NPCs: " + npcs, 10, 200);
        paintGraphics.drawString("NPCs length: " + npcs.length, 10, 220);
        int npcCount = 0;
        for(INPC npc : npcs){
        	if(npc != null){
        		paintGraphics.drawString("NPC \""  + npc.getDefinition().getName() + "\"", 400, 10+npcCount*20);
        		npcCount++;
        	}
        }
        double radians = ((IClient)applet).getCameraYaw();
        int yaw = (int)Math.round(radians/2048*360);

        paintGraphics.drawString("Camera Yaw: "+Integer.toString(yaw),100,100);
       
        // Pulsebot
        paintGraphics.drawString("PulseBot RSClient", 10, 100);
        paintGraphics.dispose();

        if (g != null) {
            g.drawImage(paintBuffer, 0, 0, null);
        }

        return gameBuffer.getGraphics();
    }
	
	/**
	 * Runs the analyzers on all classes
	 * @param classMap a HashMap containing the classes to analyse
	 */
	private void runAnalyzers(HashMap<String, ClassNode> classMap){
		System.out.println("\nAnalyzing has started!");

		// Creating the analysers
		ArrayList<AbstractAnalyzer> analyzers = new ArrayList<AbstractAnalyzer>();
		// Canvas related
		analyzers.add(canvasAnalyzer);
		// Node related
		analyzers.add(nodeAnalyzer);
		analyzers.add(cacheableNodeAnalyzer);
		analyzers.add(cacheableNodeQueueAnalyzer);
		analyzers.add(nodeHashTableAnalyzer);
		analyzers.add(nodeQueueAnalyzer);
		analyzers.add(nodeCacheAnalyzer);
		// Renderable related
		analyzers.add(renderableAnalyzer);
		analyzers.add(actorAnalyzer);
		analyzers.add(modelAnalyzer);
		// Player related
		analyzers.add(playerAnalyzer);
		analyzers.add(playerDefinitionAnalyzer);
		// NPC related
		analyzers.add(npcAnalyzer);
		analyzers.add(npcDefinitionAnalyzer);
		// Game related
		analyzers.add(clientAnalyzer);
		analyzers.add(gameShellAnalyzer);

		// Looping through all classes
		Collection<ClassNode> classNodes = classMap.values();
		for(AbstractAnalyzer analyzer : analyzers){
			for(ClassNode classNode : classNodes){
				// The result is the name of the detected class
				String name = analyzer.run(classNode);
				/*
				if(name != null){
					classNodes.remove(classNode);
					break;
				}
				*/
			}
		}
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
