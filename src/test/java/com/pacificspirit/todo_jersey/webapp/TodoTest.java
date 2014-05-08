/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2014 Oracle and/or its affiliates. All rights reserved.
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

package com.pacificspirit.todo_jersey.webapp;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

import com.pacificspirit.todo_jersey.webapp.domain.Todo;

import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.validation.ValidationError;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.glassfish.jersey.test.external.ExternalTestContainerFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

//import com.sun.jersey.client.apache.ApacheHttpClient;

/**
 * @author David Orchard (orchard at pacificspirit.com)
 */
public class TodoTest extends JerseyTest {

    private final static Todo TODO_1;
    private final static Todo TODO_2;

    static {
        TODO_1 = new Todo();
        TODO_1.setTitle("Jersey Foo");
        TODO_1.setDone("true");

        TODO_2 = new Todo();
        TODO_2.setTitle("Jersey Bar");
        TODO_2.setBody("jersey@bar.com");
        TODO_2.setDone("false");
    }

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);

        final MyApplication application = new MyApplication();
        application.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        return application;
    }

    @Override
    protected void configureClient(final ClientConfig config) {
        super.configureClient(config);

        config.register(MoxyJsonFeature.class);
    }

    @Override
    protected URI getBaseUri() {
        final UriBuilder baseUriBuilder = UriBuilder.fromUri(super.getBaseUri()).path("todo");
        final boolean externalFactoryInUse = getTestContainerFactory() instanceof ExternalTestContainerFactory;
        return externalFactoryInUse ? baseUriBuilder.path("api").build() : baseUriBuilder.build();
    }

    @Test
    public void testAddTodo() throws Exception {
        final WebTarget target = target().
                path("todo");
        final Response response = target.
                request(MediaType.APPLICATION_JSON_TYPE).
                post(Entity.entity(TODO_1, MediaType.APPLICATION_JSON_TYPE));

        final Todo todo = response.readEntity(Todo.class);

        assertEquals(200, response.getStatus());
        assertNotNull(todo.getId());

        final Response invalidResponse = target.
                request(MediaType.APPLICATION_JSON_TYPE).
                post(Entity.entity(TODO_1, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(500, invalidResponse.getStatus());
        assertTrue(getValidationMessageTemplates(invalidResponse).contains("{todo.already.exist}"));

        assertEquals(200, target.path("" + todo.getId()).request(MediaType.APPLICATION_JSON_TYPE).delete().getStatus());
    }
    
    @Test
    public void testUpdateTodo() throws Exception {
        final WebTarget target = target().
                path("todo");
        Response response = target.
                request(MediaType.APPLICATION_JSON_TYPE).
                post(Entity.entity(TODO_1, MediaType.APPLICATION_JSON_TYPE));

        final Todo todo = response.readEntity(Todo.class);

        assertEquals(200, response.getStatus());
        assertNotNull(todo.getId());
        
        todo.setBody("new body");

        response = target.
        		path("" + todo.getId()).
        		request(MediaType.APPLICATION_JSON_TYPE).
                put(Entity.entity(todo, MediaType.APPLICATION_JSON_TYPE));
        final Todo todoNew = response.readEntity(Todo.class);
        
        assertEquals(200, response.getStatus());
        assertTrue(todoNew.getBody().contains("new body"));

        assertEquals(200, target.path("" + todo.getId()).request(MediaType.APPLICATION_JSON_TYPE).delete().getStatus());
    }
    
    @Test
    public void testPartialUpdateTodo() throws Exception {
        final WebTarget target = target().
                path("todo");
        Response response = target.
                request(MediaType.APPLICATION_JSON_TYPE).
                post(Entity.entity(TODO_1, MediaType.APPLICATION_JSON_TYPE));

        final Todo todo = response.readEntity(Todo.class);

        assertEquals(200, response.getStatus());
        assertNotNull(todo.getId());
        
        todo.setBody("new body");

//        response = target.
//        		path("" + todo.getId()).
//        		request(MediaType.APPLICATION_JSON_TYPE).
//                method("PATCH", Entity.entity(todo, MediaType.APPLICATION_JSON_TYPE));
//        final Todo todoNew = response.readEntity(Todo.class);
        
//        DefaultClientConfig config = new DefaultClientConfig();
//        config.getProperties().put(URLConnectionClientHandler.PROPERTY_HTTP_URL_CONNECTION_SET_METHOD_WORKAROUND, true);
//        Client c = Client.create(config);
//
//        WebResource r = c.resource(getUri().path("test/entity").build());
        
        
//        com.sun.jersey.api.client.Client c = ApacheHttpClient.create();
//        com.sun.jersey.api.client.WebResource r = c.resource("http://localhost:8080/bean-validation-webapp/api/todo/1");
//     big long exception if above
        
        
// https://github.com/fge/json-patch
// http://kingsfleet.blogspot.ca/2014/02/transparent-patch-support-in-jax-rs-20.html
// http://soabits.blogspot.ca/2013/01/http-put-patch-or-post-partial-updates.html
// http://www.javacodegeeks.com/2012/09/simple-rest-client-in-java.html
//        Todo todoNew = r.method("PATCH", Todo.class, todo);
//        
//        assertEquals(200, response.getStatus());
//        assertTrue(todoNew.getBody().contains("new body"));
//
        assertEquals(200, target.path("" + todo.getId()).request(MediaType.APPLICATION_JSON_TYPE).delete().getStatus());
    }


    @Test
    public void testTodoDoesNotExist() throws Exception {
        final WebTarget target = target().
                path("todo");

        // GET
        Response response = target.path("1").request(MediaType.APPLICATION_JSON_TYPE).get();

        assertEquals(500, response.getStatus());

        Set<String> violationsMessageTemplates = getValidationMessageTemplates(response);
        assertEquals(1, violationsMessageTemplates.size());
        assertTrue(violationsMessageTemplates.contains("{todo.does.not.exist}"));

        // DELETE
        response = target.path("1").request(MediaType.APPLICATION_JSON_TYPE).delete();

        assertEquals(500, response.getStatus());

        violationsMessageTemplates = getValidationMessageTemplates(response);
        assertEquals(1, violationsMessageTemplates.size());
        assertTrue(violationsMessageTemplates.contains("{todo.does.not.exist}"));
    }

    @Test
    public void testTodoWrongId() throws Exception {
        final WebTarget target = target().
                path("todo");

        // GET
        Response response = target.path("-1").request(MediaType.APPLICATION_JSON_TYPE).get();

        assertEquals(400, response.getStatus());

        Set<String> violationsMessageTemplates = getValidationMessageTemplates(response);
        assertEquals(1, violationsMessageTemplates.size());
        assertTrue(violationsMessageTemplates.contains("{todo.wrong.id}"));

        // DELETE
        response = target.path("-2").request(MediaType.APPLICATION_JSON_TYPE).delete();

        assertEquals(400, response.getStatus());

        violationsMessageTemplates = getValidationMessageTemplates(response);
        assertEquals(1, violationsMessageTemplates.size());
        assertTrue(violationsMessageTemplates.contains("{todo.wrong.id}"));
    }

    private List<ValidationError> getValidationErrorList(final Response response) {
        return response.readEntity(new GenericType<List<ValidationError>>() {});
    }

    private Set<String> getValidationMessageTemplates(final Response response) {
        return getValidationMessageTemplates(getValidationErrorList(response));
    }

    private Set<String> getValidationMessageTemplates(final List<ValidationError> errors) {
        final Set<String> templates = new HashSet<String>();
        for (final ValidationError error : errors) {
            templates.add(error.getMessageTemplate());
        }
        return templates;
    }

    @Test
    public void testAddInvalidTodo() throws Exception {
         final Todo entity = new Todo();
        entity.setDone("Crrrn");

        final Response response = target().
                path("todo").
                request(MediaType.APPLICATION_JSON_TYPE).
                post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(400, response.getStatus());

        final List<ValidationError> validationErrorList = getValidationErrorList(response);
        for (final ValidationError validationError : validationErrorList) {
            assertTrue(validationError.getPath().contains("TodoResource.addTodo.todo"));
        }

        final Set<String> messageTemplates = getValidationMessageTemplates(validationErrorList);
        assertTrue(messageTemplates.contains("{todo.wrong.title}"));
        assertTrue(messageTemplates.contains("{todo.wrong.done}"));
        assertTrue(messageTemplates.contains("{todo.empty.means}"));
        assertEquals(3, messageTemplates.size());
    }

    @Test
    public void testSearchByUnknown() throws Exception {
        final Response response = target().
                path("todo").
                path("search/unknown").
                queryParam("q", "er").
                request(MediaType.APPLICATION_JSON_TYPE).
                get();

        assertEquals(400, response.getStatus());

        final Set<String> messageTemplates = getValidationMessageTemplates(response);
        assertEquals(1, messageTemplates.size());
        assertTrue(messageTemplates.contains("{com.pacificspirit.todo_jersey.webapp.constraint.SearchType.message}"));
    }

    @Test
    public void testSearchByTitleEmpty() throws Exception {
        final Response response = target().
                path("todo").
                path("search/title").
                queryParam("q", "er").
                request(MediaType.APPLICATION_JSON_TYPE).
                get();

        assertEquals(200, response.getStatus());

        final List<Todo> result = response.readEntity(new GenericType<List<Todo>>() {});
        assertEquals(0, result.size());
    }

    @Test
    public void testSearchByBodyInvalid() throws Exception {
        final Response response = target().
                path("todo").
                path("search/body").
                queryParam("q", (String) null).
                request(MediaType.APPLICATION_JSON_TYPE).
                get();

        assertEquals(400, response.getStatus());

        final Set<String> messageTemplates = getValidationMessageTemplates(response);
        assertEquals(1, messageTemplates.size());
        assertTrue(messageTemplates.contains("{search.string.empty}"));
    }

    @Test
    public void testSearchByTitle() throws Exception {
        final WebTarget target = target().path("todo");
        target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(TODO_1, MediaType.APPLICATION_JSON_TYPE));
        target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(TODO_2, MediaType.APPLICATION_JSON_TYPE));

        Response response = target.
                path("search/title").
                queryParam("q", "er").
                request(MediaType.APPLICATION_JSON_TYPE).
                get();

        List<Todo> todos = response.readEntity(new GenericType<List<Todo>>() {});

        assertEquals(200, response.getStatus());
        assertEquals(2, todos.size());

        for (final Todo todo : todos) {
            assertTrue(todo.getTitle().contains("er"));
        }

        response = target.
                path("search/title").
                queryParam("q", "Foo").
                request(MediaType.APPLICATION_JSON_TYPE).
                get();

        todos = response.readEntity(new GenericType<List<Todo>>() {});

        assertEquals(200, response.getStatus());
        assertEquals(1, todos.size());
        assertTrue(todos.get(0).getTitle().contains("Foo"));

        assertEquals(200, target.request(MediaType.APPLICATION_JSON_TYPE).delete().getStatus());
    }
}
