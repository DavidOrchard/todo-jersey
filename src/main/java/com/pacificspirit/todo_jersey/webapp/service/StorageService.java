package com.pacificspirit.todo_jersey.webapp.service;

import com.pacificspirit.todo_jersey.webapp.service.MessageService;
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
    	if((oldDone == null || oldDone.equalsIgnoreCase("false")) && newDone.equalsIgnoreCase("true")) {
    		try {
    			MessageService.send("\"" + title + "\" task has been marked as done.", messageFactory);
    		} catch(Exception e){
    			
    		}
    	}
    }
     
}
