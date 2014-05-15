package com.pacificspirit.todo_jersey.webapp.service;

import com.pacificspirit.todo_jersey.webapp.service.MessageService;

/**
 * storage of todos.
 *
 * @author David Orchard (orchard at pacificspirit.com)
 */
public abstract class StorageService implements StorageServiceIntf {

    
    /** 
     * compares done fields and calls MessageService if value has gone from false to true
     * @param oldDone
     * @param newDone
     */
    public static void compareDone(boolean oldDone, boolean newDone, String title) {
    	if(!oldDone && newDone) {
    		try {
    			MessageService.send("\"" + title + "\" task has been marked as done.");
    		} catch(Exception e){
    			
    		}
    	}
    }
     
}
