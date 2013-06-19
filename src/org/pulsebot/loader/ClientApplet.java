package org.pulsebot.loader;

import org.objectweb.asm.tree.ClassNode;
import org.pulsebot.injection.generic.AbstractAnalyser;
import org.pulsebot.injection.impl.CanvasAnalyser;
import org.pulsebot.injection.impl.ClientAnalyser;
import org.pulsebot.injection.impl.PlayerAnalyser;
import org.pulsebot.loader.utils.Utilities;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.pulsebot.injection.utils.JarUtils.updateJar;

/**
 * Loads the RuneScape applet and adds it to a JPanel
 *
 * @author JJ
 */
public class ClientApplet extends JPanel implements AppletStub {
    public static HashMap<String, String> CLASSNAMES = new HashMap();

    private static final long serialVersionUID = -7509230741659843230L;

    private Applet applet = null;
    private URLClassLoader classLoader = null;
    private URL codeBase = null, documentBase = null;
    private HashMap<String, String> parameters = new HashMap<String, String>();
    private HashMap<String, ClassNode> CLASSES = new HashMap();

    private static final Pattern codeRegex = Pattern.compile("code=(.*) ");
    private static final Pattern archiveRegex = Pattern.compile("archive=(.*) ");
    private static final Pattern paramRegex = Pattern.compile("<param name=\"([^\\s]+)\"\\s+value=\"([^>]*)\">");

    /**
     * Constructs a new ClientApplet, which contains RuneScape as an applet
     *
     * @param link         link to the page to download
     * @param width        desired width
     * @param height       desired height
     * @param saveGamepack optional to save the gamepack
     */
    public ClientApplet(String link, int width, int height, boolean saveGamepack) {
        try {
            setLayout(new BorderLayout(0, 0));
            setPreferredSize(new Dimension(width, height));
            String pageSource = Utilities.getPage(link);
            Matcher codeMatcher = codeRegex.matcher(pageSource);
            Matcher archiveMatcher = archiveRegex.matcher(pageSource);

            // Finding the jar location and class to load
            if (codeMatcher.find() && archiveMatcher.find()) {
                String jarLocation = link + archiveMatcher.group(1);
                System.out.println(jarLocation);
                String code = codeMatcher.group(1).replaceAll(".class", "");
                codeBase = new URL(jarLocation);
                documentBase = new URL(link);

                // Finding the parameters
                Matcher paramMatcher = paramRegex.matcher(pageSource);
                while (paramMatcher.find()) {
                    parameters.put(paramMatcher.group(1), paramMatcher.group(2));
                }
                // Downloading the gamepack
                if (!saveGamepack) { // Load it straight away
                    classLoader = new URLClassLoader(new URL[]{new URL(jarLocation)});
                    applet = (Applet) classLoader.loadClass(code).newInstance();
                } else { // Save the jar and load it afterwards
                    Utilities.downloadFile(jarLocation, "./gamepack.jar");
                    CLASSES = org.pulsebot.injection.utils.JarUtils.parseJar(new JarFile("gamepack.jar"));
                    System.out.println(CLASSES.values().size() + " Classes parsed");
                    this.loadAnalysters();
                    this.runAnalysters();
                    updateJar(CLASSES, new JarFile("gamepack.jar"));
                    classLoader = new URLClassLoader(new URL[]{new URL("file:injected.jar")});

                    applet = (Applet) classLoader.loadClass(code).newInstance();
                }

                applet.setStub(this);
                applet.setPreferredSize(new Dimension(width, height));
                add(applet, BorderLayout.CENTER);
                System.out.println(getComponent(0).getName());

            }

            System.out.println("End of ClientApplet");
        } catch (Exception ex) {
            System.out.println("Error constructing ClientApplet: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error loading... please check your internet connection.", "Error loading..", JOptionPane.ERROR_MESSAGE);
        }

    }

    private ArrayList<AbstractAnalyser> analysers = new ArrayList<AbstractAnalyser>();

    private void loadAnalysters() {
        this.analysers.add(new ClientAnalyser());
        this.analysers.add(new PlayerAnalyser());
        this.analysers.add(new CanvasAnalyser());

    }

    private void runAnalysters() {
        Object obj[] = CLASSES.values().toArray();
        for (int i = 0; i < obj.length - 1; i++) {
            ClassNode node = (ClassNode) obj[i];
            for (AbstractAnalyser analyser : this.analysers) {
                String str = analyser.run(node);
                if (str == null)
                    continue;
                String clazz = analyser.getClass().toString().replace("class org.pulsebot.injection.impl.", "");
                clazz = clazz.replace("Analyser", "");
                CLASSNAMES.put(clazz, node.name);
            }
        }
    }

    /**
     * Initializes and starts the applet
     */
    public void start() {
        if (applet != null) {
            applet.init();
            applet.start();
        }
    }

    /**
     * Destructs our applet and closes the class loader
     */
    public void destruct() {
        if (applet != null) {
            remove(applet);
            applet.stop();
            applet.destroy();
            applet = null;
        }

        if (classLoader != null) {
            try {
                classLoader.close();
            } catch (Exception ex) {
                System.out.println("Error closing class loader: " + ex.getMessage());
            }
        }
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public URL getDocumentBase() {
        return documentBase;
    }

    @Override
    public URL getCodeBase() {
        return codeBase;
    }

    @Override
    public String getParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public AppletContext getAppletContext() {
        return null;
    }

    @Override
    public void appletResize(int width, int height) {
    }


}
