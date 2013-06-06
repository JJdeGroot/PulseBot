package org.pulsebot.loader.utils;

import java.awt.Canvas;
import java.util.HashMap;

import org.pulsebot.loader.Client;

public class ClientPool {

	private static ClientPool instance = new ClientPool();
	private static HashMap<Integer, Client> clients = new HashMap<Integer, Client>();
	
	/** Private constructor so the user can't create instances */
	private ClientPool() {}
	
	/**
	 * Returns the one and only instance of this class
	 * @return ClientPool instance
	 */
	public static ClientPool getInstance(){
		return instance;
	}
	
	/**
	 * Returns a HashMap containing the clients
	 * @return HashMap with clients
	 */
	public static HashMap<Integer, Client> getClients(){
		return clients;
	}
	
	/**
	 * Attempts to return a client with the given hashCode
	 * @param hashCode hashCode of the client
	 * @return the client that belongs to the given hashCode
	 */
	public static Client getClient(int hashCode){
		System.out.println("[ClientPool] getClient #" + hashCode);
		synchronized (clients) {
			return clients.containsKey(hashCode) ? clients.get(hashCode) : null;
		}
	}
	
	/**
	 * Attempts to return a client that belongs to the given canvas
	 * @param canvas canvas of the client
	 * @return the client that belongs to the given canvas
	 */
	public static Client getClient(Canvas canvas){
		System.out.println("[ClientPool] getClient #" + canvas);
		synchronized (clients) {
			int hashCode = canvas.getClass().getClassLoader().hashCode();
			return clients.containsKey(hashCode) ? clients.get(hashCode) : null;
		}
	}
	
	
}
