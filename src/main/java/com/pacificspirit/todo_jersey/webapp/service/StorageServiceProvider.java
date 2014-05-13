package com.pacificspirit.todo_jersey.webapp.service;

import com.twilio.sdk.resource.factory.MessageFactory;

/**
 * Simple storage of todos.
 *
 * @author David Orchard (orchard at pacificspirit.com)
 */
public class StorageServiceProvider{
	private static StorageService s = null;
	private static MessageFactory mf = null;
	public static StorageService get(String storageType) {
		
		if(storageType.contains("mongo")) {
			s = new StorageServiceMongo(mf);
		} else {
			s = new StorageServiceInMemory(mf);
		}
		return s;
	}
	
	/**
     *  init
     *  
     */
    public static void init(MessageFactory m) {
    	mf = m;   	
    }
     
}
