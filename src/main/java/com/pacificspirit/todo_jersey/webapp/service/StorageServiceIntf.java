package com.pacificspirit.todo_jersey.webapp.service;

import java.util.List;
import com.pacificspirit.todo_jersey.webapp.domain.Todo;

/**
 * Simple storage of todos.
 *
 * @author David Orchard (orchard at pacificspirit.com)
 */
public interface StorageServiceIntf {
   
    /**
     * Adds a todo into the storage. If a todo with given data already exist {@code null} value is returned.
     *
     * @param todo todo to be added.
     * @return todo with pre-filled {@code id} field, {@code null} if the todo already exist in the storage.
     */
    public Todo addTodo(final Todo todo);
    
    /**
     * Updates a todo into the storage.
     *
     * @param todo todo to be updated, must contain an id field
     * @return todo with pre-filled {@code id} field, {@code null} if the todo already exist in the storage.
     */
    public Todo updateTodoFull(final Todo todo); 
    
    /**
     * Updates a todo's done into the storage.
     *
     * @param todo todo to be updated, must contain an id field
     * @return todo with pre-filled {@code id} field, {@code null} if the todo already exist in the storage.
     */
    public Todo updateTodoDone(final String id, final boolean done);

    
    /**
     * Updates a todo into the storage.
     *
     * @param todo todo to be updated, must contain an id field
     * @return todo with pre-filled {@code id} field, {@code null} if the todo already exist in the storage.
     */
    public Todo updateTodoPartial(final Todo todo);
    
    /**
     * Removes all todos from the storage.
     *
     * @return list of all removed todos.
     */
    public List<Todo> clear();
    /**
     * Removes todo with given {@code id}.
     *
     * @param id id of the todo to be removed.
     * @return removed todo or {@code null} if the todo is not present in the storage.
     */
    public Todo remove(final String id);
    /**
     * Retrieves todo with given {@code id}.
     *
     * @param id id of the todo to be retrieved.
     * @return todo or {@code null} if the todo is not present in the storage.
     */
    public Todo get(final String id);
    /**
     * Retrieves all todos
     *
     * @return todos or {@code null} if the todo is not present in the storage.
     */
    public List<Todo> get();

}
