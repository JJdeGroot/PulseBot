package org.pulsebot.injection.utils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: NKN
 * Date: 5/27/13
 * Time: 1:50 PM
 */
public class JarUtils {
    public static HashMap<String, ClassNode> parseJar(JarFile jarfile) {
        HashMap<String, ClassNode> classes = new HashMap();
        try {
            Enumeration<?> enumeration = jarfile.entries();//Get all the classes in the jar file
            while (enumeration.hasMoreElements()) { //While classes exists
                JarEntry entry = (JarEntry) enumeration.nextElement();//Get the class
                if (entry.getName().endsWith(".class")) {//Check if it ends in .class, so make sure it is a class file
                    ClassReader classReader = new ClassReader(jarfile.getInputStream(entry));//Class reader
                    ClassNode classNode = new ClassNode(); //A ASM class file thing
                    classReader.accept(classNode, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES); //Reads the class
                    classes.put(classNode.name, classNode); //Puts class in the hashmap
                }
            }
            jarfile.close();
            return classes;
        } catch (Exception e) {
            return null;
        }
    }

    public static void updateJar(HashMap<String, ClassNode> classes, JarFile jar) {
        try {
            System.out.println("\nDumping injected classes.\n");
            Enumeration<?> enumeration = jar.entries();

            JarOutputStream newJ = new JarOutputStream(new FileOutputStream(new File("injected.jar")), jar.getManifest());
            while (enumeration.hasMoreElements()) {
                JarEntry entry = (JarEntry) enumeration.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().replaceAll(".class", "");
                    if (classes.containsKey(className)) {
                        newJ.putNextEntry(new JarEntry(entry.getName()));
                        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                        ClassNode node = classes.get(className);
                        node.accept(cw);
                        try {
                            ByteArrayInputStream byteAr = new ByteArrayInputStream(cw.toByteArray());
                            byte[] buffer = new byte[1024];
                            while (true) {
                                int count = byteAr.read(buffer);
                                if (count == -1)
                                    break;
                                newJ.write(buffer, 0, count);
                            }
                            newJ.closeEntry();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            newJ.close();
            jar.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}

