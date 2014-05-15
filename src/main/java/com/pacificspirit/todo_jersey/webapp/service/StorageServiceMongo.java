package com.pacificspirit.todo_jersey.webapp.service;

import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;

import com.pacificspirit.todo_jersey.webapp.domain.Todo;

/**
 * Mongo storage of todos.
 *
 * @author David Orchard (orchard at pacificspirit.com)
 */
public class StorageServiceMongo extends StorageService{

    private MongoClient mongoClient = null;
	private DB db = null;
	private DBCollection todos = null;
	

    public StorageServiceMongo() {
    	// To directly connect to a single MongoDB server (note that this will not auto-discover the primary even
    	// if it's a member of a replica set:
    	try {
    		mongoClient = new MongoClient();
    		db = mongoClient.getDB( "mydb" );
    		todos = db.getCollection("todos");
        	
    	} catch (Exception e) {
    		
    	}
     	
    }

    /**
     * Adds a todo into the storage. If a todo with given data already exist {@code null} value is returned.
     *
     * @param todo todo to be added.
     * @return todo with pre-filled {@code id} field, {@code null} if the todo already exist in the storage.
     */
    public Todo addTodo(final Todo todo) {
    	BasicDBObject obj = new BasicDBObject();
    	if(todo.getId() != null ) {
    		obj.put("_id", new ObjectId(todo.getId()));
     
    		DBObject found = todos.findOne(obj);
     
    		if (found != null) {
    			return null;
    		}
    	}
        
        obj = new BasicDBObject();
        todo2DBObject(todo, obj);                
        todos.insert(obj);
        todo.setId(obj.getString("_id"));

        return todo;
    }
    
    /**
     * Updates a todo into the storage.
     *
     * @param todo todo to be updated, must contain an id field
     * @return todo with pre-filled {@code id} field, {@code null} if the todo already exist in the storage.
     */
    public Todo updateTodoFull(final Todo todo) {
        BasicDBObject obj = new BasicDBObject();
        if(todo.getId() == null ) {
        	return null;
        }
    	obj.put("_id", new ObjectId(todo.getId()));
     
    	DBObject found = todos.findOne(obj);
    	
    	if (found == null) {
    		return null;
    	}
    	compareDone(found.get("done").toString().contains("done"), todo.getDone(), found.get("title").toString());
    	
        todo2DBObject(todo, obj);
        todos.update(found, obj);

        return todo;
    }
    
    /**
     * Updates a todo's done into the storage.
     *
     * @param todo todo to be updated, must contain an id field
     * @return todo with pre-filled {@code id} field, {@code null} if the todo already exist in the storage.
     */
    public Todo updateTodoDone(final String id, final boolean done) {
    	BasicDBObject obj = new BasicDBObject("_id", new ObjectId(id.toString()));     
    	DBObject found = todos.findOne(obj);
    	
    	compareDone(found.get("done").toString().contains("true"), done, found.get("title").toString());
    	todos.update(found, new BasicDBObject("$set", new BasicDBObject("done", done)));
        Todo todo = new Todo();
        dBObject2Todo(found, todo);
        todo.setDone(done);
        return todo;
    }

    
    /**
     * Updates a todo into the storage.
     *
     * @param todo todo to be updated, must contain an id field
     * @return todo with pre-filled {@code id} field, {@code null} if the todo already exist in the storage.
     */
    public Todo updateTodoPartial(final Todo todo) {
    	return updateTodoFull(todo);
    	
    }
    
    /** 
     * Copy Todo object to Mongo BasicDBObject
     * 
     * @param todo todo source
     * @param dbObject mongo object target
     */
    public BasicDBObject todo2DBObject(Todo todo, BasicDBObject dbObject) {

		String title = todo.getTitle();
		if(title != null) {
			dbObject.append("title", title);
		}
	
		String body = todo.getBody();
		if(body != null) {
			dbObject.append("body", body);
		}
	
		boolean done = todo.getDone();
		compareDone(dbObject.getBoolean("done"), done, dbObject.getString("title"));
		dbObject.append("done", done);
		return dbObject;
    }
    
    /** 
     * Copy Mongo DBObject to Todo object
     * 
     * @param dbObject mongo object target
     * @param todo todo source
     */
    public Todo dBObject2Todo(DBObject dbObject, Todo todo) {

		Object title = dbObject.get("title");
		if(title != null) {
			todo.setTitle(title.toString());
		}
	
		Object body = dbObject.get("body");
		if(body != null) {
			todo.setBody(body.toString());
		}
	
		Object done = dbObject.get("done");
		if(done != null) {
			todo.setDone(done.toString().contains("true"));
		}
		String id = dbObject.get("_id").toString();
		if(id != null) {
			todo.setId(id);
		}
		return todo;
    }



    /**
     * Removes all todos from the storage.
     *
     * @return list of all removed todos.
     */
    public List<Todo> clear() {
        final Collection<Todo> values = get();
        todos.remove(new BasicDBObject());
        return new ArrayList<Todo>(values);
    }

    /**
     * Removes todo with given {@code id}.
     *
     * @param id id of the todo to be removed.
     * @return removed todo or {@code null} if the todo is not present in the storage.
     */
    public Todo remove(final String id) {
    	Todo removedTodo = get(id);
    	if(removedTodo != null) {
    		BasicDBObject obj = new BasicDBObject("_id", new ObjectId(id));   	
    		todos.remove(obj);
    	}
        return removedTodo;
    }

    /**
     * Retrieves todo with given {@code id}.
     *
     * @param id id of the todo to be retrieved.
     * @return todo or {@code null} if the todo is not present in the storage.
     */
    public Todo get(final String id) {
    	ObjectId oid = null;
    	try {
    		oid = new ObjectId(id);
    	} catch (Exception e){
    		return null;
    	}
    	BasicDBObject queryObj = new BasicDBObject("_id", oid);   	
        DBObject obj = todos.findOne(queryObj);
        if(obj == null) {
        	return null;
        }
        return dBObject2Todo(obj, new Todo());
    }
    
    /**
     * Retrieves all todos
     *
     * @return todos or {@code null} if the todo is not present in the storage.
     */
    public List<Todo> get() {
    	DBCursor cursor = todos.find();
    	final ArrayList<Todo> t = new ArrayList<Todo>();
    	try {
    	   while(cursor.hasNext()) {
    		   t.add(dBObject2Todo(cursor.next(), new Todo()));
    	   }
    	} finally {
    	   cursor.close();
    	}
    	return t;
    }

}
