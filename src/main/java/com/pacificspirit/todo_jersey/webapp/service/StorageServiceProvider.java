package com.pacificspirit.todo_jersey.webapp.service;

/**
 * Simple storage of todos.
 *
 * @author David Orchard (orchard at pacificspirit.com)
 */
public class StorageServiceProvider{
	private static StorageService s = null;
	public static StorageService get() {
		System.out.println("StorageService get");
		if(s != null) {
			return s;
		}
		String storageServiceType = System.getenv("TODO_JERSEY_STORAGE_TYPE");
	    storageServiceType = (storageServiceType == null || storageServiceType.length() == 0) ? "InMemory" : storageServiceType;
	    System.out.println(storageServiceType);
	    
		if(storageServiceType.contains("mongo")) {
			s = new StorageServiceMongo();
		} else {
			s = new StorageServiceInMemory();
		}
		return s;
	}
	
	public static String getIDRegexp() {
		return (s == null ? "" : s.getIDRegexp());
	}
     
}
