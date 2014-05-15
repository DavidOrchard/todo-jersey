/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.pacificspirit.todo_jersey.webapp.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.pacificspirit.todo_jersey.webapp.domain.Todo;

/**
 * Simple storage of todos.
 *
 * @author David Orchard (orchard at pacificspirit.com
 */
public class StorageServiceInMemory extends StorageService{

    private final AtomicLong todoCounter = new AtomicLong(0);
    private final Map<String, Todo> todos = new HashMap<String, Todo>();
    
    /**
     * Adds a todo into the storage. If a todo with given data already exist {@code null} value is returned.
     *
     * @param todo todo to be added.
     * @return todo with pre-filled {@code id} field, {@code null} if the todo already exist in the storage.
     */
    public Todo addTodo(final Todo todo) {
    	 
        if (todos.containsValue(todo)) {
            return null;
        }
        todo.setId(String.valueOf(todoCounter.incrementAndGet()));
        todos.put(todo.getId(), todo);
        return todo;
    }
    
    /**
     * Updates a todo into the storage.
     *
     * @param todo todo to be updated, must contain an id field
     * @return todo with pre-filled {@code id} field, {@code null} if the todo already exist in the storage.
     */
    public Todo updateTodoFull(final Todo todo) {
    	Todo oldTodo = todos.get(todo.getId());
    	compareDone(oldTodo.getDone(), todo.getDone(), todo.getTitle());
         
        todos.put(todo.getId(), todo);
        return todo;
    }
    
    /**
     * Updates a todo's done into the storage.
     *
     * @param todo todo to be updated, must contain an id field
     * @return todo with pre-filled {@code id} field, {@code null} if the todo already exist in the storage.
     */
    public Todo updateTodoDone(final String id, final boolean done) {
        Todo oldTodo = todos.get(id);
        compareDone(oldTodo.getDone(), done, oldTodo.getTitle());
        oldTodo.setDone(done);
        todos.put(oldTodo.getId(), oldTodo);
        return oldTodo;
    }

    
    /**
     * Updates a todo into the storage.
     *
     * @param todo todo to be updated, must contain an id field
     * @return todo with pre-filled {@code id} field, {@code null} if the todo already exist in the storage.
     */
    public Todo updateTodoPartial(final Todo todo) {
    	Todo storedTodo = todos.get(todo.getId());
    	if (storedTodo == null) {
    		return updateTodoFull(todo);
    	}	
    	
    	String newTitle = todo.getTitle();
    	if(newTitle != null) {
    		storedTodo.setTitle(newTitle);
    	}
 
    	String newBody = todo.getBody();
    	if(newBody != null) {
    		storedTodo.setBody(newBody);
    	}

    	boolean newDone = todo.getDone();
    	compareDone(storedTodo.getDone(), newDone, storedTodo.getTitle());
    	storedTodo.setDone(newDone);
    	
        return updateTodoFull(storedTodo);
    }


    /**
     * Removes all todos from the storage.
     *
     * @return list of all removed todos.
     */
    public List<Todo> clear() {
        final Collection<Todo> values = todos.values();
        todos.clear();
        return new ArrayList<Todo>(values);
    }

    /**
     * Removes todo with given {@code id}.
     *
     * @param id id of the todo to be removed.
     * @return removed todo or {@code null} if the todo is not present in the storage.
     */
    public Todo remove(final String id) {
        return todos.remove(id);
    }

    /**
     * Retrieves todo with given {@code id}.
     *
     * @param id id of the todo to be retrieved.
     * @return todo or {@code null} if the todo is not present in the storage.
     */
    public Todo get(final String id) {
        return todos.get(id);
    }
    
    /**
     * Retrieves all todo.
     *
     * @return todo or {@code null} if the todo is not present in the storage.
     */
    public List<Todo> get() {
    	return new ArrayList<Todo>(todos.values());
    }


//    /**
//     * Finds todos whose email contains {@code emailPart} as a substring.
//     *
//     * @param titlePart search phrase.
//     * @return list of matched todos or an empty list.
//     */
//    public List<Todo> findByTitle(final String titlePart) {
//        final List<Todo> results = new ArrayList<Todo>();
//
//        for (final Todo todo : todos.values()) {
//            final String title = todo.getTitle();
//            if (title != null && title.contains(titlePart)) {
//                results.add(todo);
//            }
//        }
//
//        return results;
//    }
//
//    /**
//     * Finds todos whose name contains {@code namePart} as a substring.
//     *
//     * @param bodyPart search phrase.  empty means match any including empty or non-existant body parts
//     * @return list of matched todos or an empty list.
//     */
//    public List<Todo> findByBody(final String bodyPart) {
//        final List<Todo> results = new ArrayList<Todo>();
//
//        for (final Todo todo : todos.values()) {
//        	final String body = todo.getBody();
//        	if ((bodyPart.length() == 0) || (body != null && body.contains(bodyPart))) {
//        		results.add(todo);
//             }
//        }
//
//        return results;
//    }
//
//    /**
//     * Finds todos whose phone contains {@code phonePart} as a substring.
//     *
//     * @param donePart search phrase.
//     * @return list of matched todos or an empty list.
//     */
//    public List<Todo> findByDone(final String donePart) {
//        final List<Todo> results = new ArrayList<Todo>();
//
//        for (final Todo todo : todos.values()) {
//            final String phone = todo.getDone();
//            if (phone != null && phone.contains(donePart)) {
//                results.add(todo);
//            }
//        }
//
//        return results;
//    }
     
}
