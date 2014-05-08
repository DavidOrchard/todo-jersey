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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.pacificspirit.todo_jersey.webapp.domain.Todo;

/**
 * Checks whether a return entity (entities) has a not-empty {@code searchType} field which contains {@code q} search phrase.
 * (Double check for entities returned from {@link com.pacificspirit.todo_jersey.webapp.service.StorageService}.)
 *
 * @author David Orchard (orchard at pacificspirit.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NotEmptySearchField.Validator.class, NotEmptySearchField.ListValidator.class})
public @interface NotEmptySearchField {

    String message() default "{com.pacificspirit.todo_jersey.webapp.constraint.NotEmptySearchField.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    public class Validator implements ConstraintValidator<NotEmptySearchField, Todo> {

        private UriInfo uriInfo;

        public Validator(@Context final UriInfo uriInfo) {
            this.uriInfo = uriInfo;
        }

        @Override
        public void initialize(final NotEmptySearchField hasId) {
        }

        @Override
        public boolean isValid(final Todo todo, final ConstraintValidatorContext constraintValidatorContext) {
            final String searchType = uriInfo.getPathParameters().getFirst("searchType");
            final String searchValue = uriInfo.getQueryParameters().getFirst("q");

            if ("title".equals(searchType)) {
                return todo.getTitle() != null && todo.getTitle().contains(searchValue);
            } else if ("Done".equals(searchType)) {
                return todo.getDone() != null && todo.getDone().contains(searchValue);
            } else {
                return todo.getBody().contains(searchValue);
            }
        }
    }

    public class ListValidator implements ConstraintValidator<NotEmptySearchField, List<Todo>> {

        @Context
        private UriInfo uriInfo;

        private Validator validator;

        @Override
        public void initialize(final NotEmptySearchField hasId) {
            validator = new Validator(uriInfo);
        }

        @Override
        public boolean isValid(final List<Todo> todos, final ConstraintValidatorContext constraintValidatorContext) {
            boolean isValid = true;
            for (final Todo todo : todos) {
                isValid &= validator.isValid(todo, constraintValidatorContext);
            }
            return isValid;
        }
    }
}
