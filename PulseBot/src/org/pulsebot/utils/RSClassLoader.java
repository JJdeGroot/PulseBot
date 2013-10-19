package org.pulsebot.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class RSClassLoader extends ClassLoader {

	private HashMap<String, Class<?>> classes = new HashMap<>();
	
	public RSClassLoader() {
        super(RSClassLoader.class.getClassLoader());
    }
    
	/**
	 * Returns the full class name based on a byte array
	 * @param data class data as byte array
	 * @return full class name
	 * @throws IOException thrown when unable to find the name
	 */
    public String getFullClassName(byte[] data) throws IOException {           
        return super.defineClass(null, data, 0, data.length, null).getName();
    }
    
    /**
     * Returns the full class name based on the class file name
     * @param classFileName the class file name
     * @return full class name
     * @throws IOException thrown when unable to find the name
     */
    public String getFullClassName(String classFileName) throws IOException {
        return this.getFullClassName(Files.readAllBytes(Paths.get(classFileName)));
    }
    
    /*
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> result = null;
        try {
        	result = super.loadClass(name);
            classes.put(name, result);
        } catch (SecurityException se) {
			try {
				String fullName = getFullClassName(name);
				result = super.loadClass(fullName, true);
	            classes.put(name, result);
			} catch (IOException e) {
				 e.printStackTrace();
			}

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
 
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return this.findClass(name);
    }
    */
 
    
	
	
}
