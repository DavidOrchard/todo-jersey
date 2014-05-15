package com.pacificspirit.todo_jersey.webapp.service;

/**
 * Simple storage of todos.
 *
 * @author David Orchard (orchard at pacificspirit.com)
 */
public class StorageServiceProvider{
	private static StorageService s = null;
	public static StorageService get(String storageType) {
		
		if(storageType.contains("mongo")) {
			s = new StorageServiceMongo();
		} else {
			s = new StorageServiceInMemory();
		}
		return s;
	}
     
}
