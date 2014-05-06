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

package org.glassfish.jersey.examples.beanvalidation.webapp.service;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;

import java.util.Arrays;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.glassfish.jersey.examples.beanvalidation.webapp.domain.Todo;

/**
 * Simple storage of todos.
 *
 * @author David Orchard (orchard at pacificspirit.com
 */
public class StorageService {

    private static final AtomicLong todoCounter = new AtomicLong(0);
    private static final Map<Long, Todo> todos = new HashMap<Long, Todo>();
    private static MongoClient mongoClient = null;
	private static DB db = null;
	
    /**
     *  init
     *  
     */
    public static void init() {
    	// To directly connect to a single MongoDB server (note that this will not auto-discover the primary even
    	// if it's a member of a replica set:
    	try {
    		mongoClient = new MongoClient();
    		db = mongoClient.getDB( "mydb" );
    	} catch (Exception e) {
    		
    	}
     	
    }

    /**
     * Adds a todo into the storage. If a todo with given data already exist {@code null} value is returned.
     *
     * @param todo todo to be added.
     * @return todo with pre-filled {@code id} field, {@code null} if the todo already exist in the storage.
     */
    public static Todo addTodo(final Todo todo) {
    	DBCollection table = db.getCollection("todos");
    	 
//        if (todos.containsValue(todo)) {
//            return null;
//        }
        todo.setId(todoCounter.incrementAndGet());
        todos.put(todo.getId(), todo);
//        BasicDBObject query = new BasicDBObject(String.valueOf(todo.getId()), contact);
//        table.insert(query);
        return todo;
    }

    /**
     * Removes all todos from the storage.
     *
     * @return list of all removed todos.
     */
    public static List<Todo> clear() {
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
    public static Todo remove(final Long id) {
        return todos.remove(id);
    }

    /**
     * Retrieves todo with given {@code id}.
     *
     * @param id id of the todo to be retrieved.
     * @return todo or {@code null} if the todo is not present in the storage.
     */
    public static Todo get(final Long id) {
        return todos.get(id);
    }

    /**
     * Finds todos whose email contains {@code emailPart} as a substring.
     *
     * @param titlePart search phrase.
     * @return list of matched todos or an empty list.
     */
    public static List<Todo> findByTitle(final String titlePart) {
        final List<Todo> results = new ArrayList<Todo>();

        for (final Todo todo : todos.values()) {
            final String title = todo.getTitle();
            if (title != null && title.contains(titlePart)) {
                results.add(todo);
            }
        }

        return results;
    }

    /**
     * Finds todos whose name contains {@code namePart} as a substring.
     *
     * @param bodyPart search phrase.
     * @return list of matched todos or an empty list.
     */
    public static List<Todo> findByBody(final String bodyPart) {
        final List<Todo> results = new ArrayList<Todo>();

        for (final Todo todo : todos.values()) {
        	if (todo.getBody().contains(bodyPart)) {
                results.add(todo);
            }
        }

        return results;
    }

    /**
     * Finds todos whose phone contains {@code phonePart} as a substring.
     *
     * @param donePart search phrase.
     * @return list of matched todos or an empty list.
     */
    public static List<Todo> findByDone(final String donePart) {
        final List<Todo> results = new ArrayList<Todo>();

        for (final Todo todo : todos.values()) {
            final String phone = todo.getDone();
            if (phone != null && phone.contains(donePart)) {
                results.add(todo);
            }
        }

        return results;
    }
}
