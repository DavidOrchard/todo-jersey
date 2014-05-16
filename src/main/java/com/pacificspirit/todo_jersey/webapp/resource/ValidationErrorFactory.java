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

package com.pacificspirit.todo_jersey.webapp.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.validation.Configuration;
import javax.validation.MessageInterpolator;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.constraints.NotNull;
import javax.validation.metadata.ConstraintDescriptor;

import org.glassfish.jersey.server.validation.ValidationError;

import com.pacificspirit.todo_jersey.webapp.domain.Todo;
import com.pacificspirit.todo_jersey.webapp.exceptions.CustomNotFoundException;
import com.pacificspirit.todo_jersey.webapp.service.*;
import com.pacificspirit.todo_jersey.webapp.resource.HasId;
import com.pacificspirit.todo_jersey.webapp.resource.AtLeastOneTodo;

/**
 * Todo basic resource class. Provides support for inserting, retrieving and deleting Todos.
 * <p/>
 * See validation annotations (input method parameters, field, return values).
 *
 * @author David Orchard (orchard at pacificspirit.com)
 */
@Path("todo")
@Produces("application/json")
public class ValidationErrorFactory {

    private Configuration<?> configuration = Validation.byDefaultProvider().configure();
	private MessageInterpolator m = configuration.getDefaultMessageInterpolator();
	private MessageInterpolator.Context ctx = new MessageInterpolator.Context() {

		@Override
		public ConstraintDescriptor<?> getConstraintDescriptor() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getValidatedValue() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> T unwrap(Class<T> arg0) {
			// TODO Auto-generated method stub
			return null;
		}};

		
	public ValidationError create(String message) {
		ValidationError error = new ValidationError();
    	
        error.setMessage(m.interpolate("{todo.does.not.exist}", ctx));
        error.setMessageTemplate("{todo.does.not.exist}");
       
        return error;
    }
}
