package org.pulsebot;

import org.objectweb.asm.tree.ClassNode;
import org.pulsebot.injection.analyzers.*;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.FieldHook;
import org.pulsebot.injection.generic.Hook;
import org.pulsebot.util.Configuration;
import org.pulsebot.util.JarUtils;
import org.pulsebot.util.Utilities;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Updater {

    private URLClassLoader classLoader = null;
    private URL codeBase = null, documentBase = null;
    private ArrayList<Hook> hooks = new ArrayList<>();
    private Hook analyzerHook;

    public static void main(String[] args){
        new Updater();
    }
    public Updater(){
        try {
           String rsLink = "http://oldschool11.runescape.com/";
            String pageSource = Utilities.getPage(rsLink);

            // Searching for the archive and code patterns
            Pattern archiveRegex = Pattern.compile("archive=(.*) ");
            Matcher archiveMatcher = archiveRegex.matcher(pageSource);
            Pattern codeRegex = Pattern.compile("code=(.*) ");
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
                Pattern paramRegex = Pattern.compile("<param name=\"([^\\s]+)\"\\s+value=\"([^>]*)\">");
                Matcher paramMatcher = paramRegex.matcher(pageSource);
                while (paramMatcher.find()) {
                    String key = paramMatcher.group(1);
                    String value = paramMatcher.group(2);
                    System.out.printf("%-20s %s", key, value + "\n");
                    HashMap<String, String> parameters = new HashMap<String, String>();
                    parameters.put(key, value);
                }

                File cachedClient = new File(Configuration.HOME, "client.jar");
                if (!cachedClient.exists()) {
                    System.out.println("\nDownloading Client");
                    Utilities.downloadFile(jarLink, cachedClient);
                    System.out.println("\nFinished Downloading");
                }

                HashMap<String, ClassNode> classMap = JarUtils.parseJar(new JarFile(cachedClient));
                runAnalyzers(classMap);
                for(Hook hook:hooks){
                    System.out.println("Class Hook: "+hook.getClassName() + "  " + hook.getClassLocation());
                    HashMap<String, FieldHook> hooks = hook.getFieldHooks();

                    for(Map.Entry<String, FieldHook> entry : hooks.entrySet()){
                        FieldHook fHook = entry.getValue();
                        System.out.println("   Field Hook: " +fHook.getLocation() + "  " + fHook.getName() + "  " + fHook.getReturn() + "   " + fHook.getMultiplier());
                    }
                }

            }



        } catch (Exception e) {
            System.out.println("Error constructing client");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading... please check your internet connection.", "Error loading..", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void runAnalyzers(HashMap<String, ClassNode> classMap){
        System.out.println("\nAnalyzing has started!");

        // Creating the analysers
        ArrayList<AbstractAnalyzer> analyzers = new ArrayList<>();
        // Canvas related

        analyzers.add(new NodeAnalyzer());
        analyzers.add(new CacheableNodeAnalyzer());
        analyzers.add(new RenderableAnalyzer());
        analyzers.add(new ModelAnalyzer());
        analyzers.add(new ActorAnalyzer());
        analyzers.add(new NodeHashTableAnalyzer());
        analyzers.add(new PlayerAnalyzer());
        analyzers.add(new PlayerDefAnalyzer());
        analyzers.add(new NPCAnalyzer());
        analyzers.add(new NPCDefinitionAnalyzer());
        analyzers.add(new ClientAnalyzer());

        // Looping through all classes
        Collection<ClassNode> classNodes = classMap.values();
        for(AbstractAnalyzer analyzer : analyzers){
            for(ClassNode classNode : classNodes){
                analyzerHook = analyzer.run(classNode);
                if(analyzerHook != null){
                    hooks.add(analyzerHook);
                }
            }
        }
    }

}
