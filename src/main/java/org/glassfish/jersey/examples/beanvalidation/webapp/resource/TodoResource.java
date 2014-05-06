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

package src.main.java.org.glassfish.jersey.examples.beanvalidation.webapp.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

//import org.glassfish.jersey.examples.beanvalidation.webapp.constraint.AtLeastOneTodo;
import org.glassfish.jersey.examples.beanvalidation.webapp.domain.Todo;
import org.glassfish.jersey.examples.beanvalidation.webapp.service.StorageService;
import org.glassfish.jersey.examples.beanvalidation.webapp.constraint.HasId;

/**
 * Todo basic resource class. Provides support for inserting, retrieving and deleting Todos.
 * <p/>
 * See validation annotations (input method parameters, field, return values).
 *
 * @author David Orchard (orchard at pacificspirit.com)
 */
@Path("todo")
@Produces("application/json")
public class TodoResource {

    @Context
    @NotNull
    private ResourceContext resourceContext;

    @POST
    @Consumes("application/json")
    @NotNull(message = "{todo.already.exist}")
    @HasId
    public Todo addTodo(
//            @NotNull @AtLeastOneTodo(message = "{todo.empty.means}") @Valid
            @NotNull(message = "{todo.empty.means}") @Valid
           final Todo todo) {
        return StorageService.addTodo(todo);
    }

    @GET
    @NotNull @HasId
    public List<Todo> getTodos() {
        return StorageService.findByBody("");
    }

    @GET
    @Path("{id}")
    @NotNull(message = "{todo.does.not.exist}")
    @HasId
    public Todo getTodo(
            @DecimalMin(value = "0", message = "{todo.wrong.id}")
            @PathParam("id") final Long id) {
        return StorageService.get(id);
    }

    @DELETE
    @NotNull @HasId
    public List<Todo> deleteTodos() {
        return StorageService.clear();
    }

    @DELETE
    @Path("{id}")
    @NotNull(message = "{todo.does.not.exist}")
    @HasId
    public Todo deleteTodo(
            @DecimalMin(value = "0", message = "{todo.wrong.id}")
            @PathParam("id") final Long id) {
        return StorageService.remove(id);
    }

    @Path("search/{searchType}")
    public SearchResource search() {
        return resourceContext.getResource(SearchResource.class);
    }
}
