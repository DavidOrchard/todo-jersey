package com.pacificspirit.todo_jersey.webapp.service;

import com.pacificspirit.todo_jersey.webapp.resource.MessageResource;
import com.twilio.sdk.resource.factory.MessageFactory;

/**
 * Simple storage of todos.
 *
 * @author David Orchard (orchard at pacificspirit.com)
 */
public abstract class StorageService implements StorageServiceIntf {

    private static MessageFactory messageFactory = null;
	
	
    /**
     *  init
     *  
     */
    public StorageService(MessageFactory mf) {
    	messageFactory = mf;   	
    }

    /** 
     * compares done fields and calls MessageService if value has gone from false to true
     * @param oldDone
     * @param newDone
     */
    public static void compareDone(String oldDone, String newDone, String title) {
    	System.out.println("compareDone, oldDone = " + oldDone + ", newDone = " + newDone);
    	if((oldDone == null || oldDone.equalsIgnoreCase("false")) && newDone.equalsIgnoreCase("true")) {
    		try {
    			System.out.println("sending message");
    			MessageResource.send("\"" + title + "\" task has been marked as done.", messageFactory);
    		} catch(Exception e){
    			
    		}
    	}
    }
     
}
