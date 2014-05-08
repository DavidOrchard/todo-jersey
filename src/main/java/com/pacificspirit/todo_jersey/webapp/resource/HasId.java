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

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.pacificspirit.todo_jersey.webapp.domain.Todo;

/**
 * Checks whether a given {@link org.Todo.jersey.examples.beanvalidation.webapp.domain.Todo} entity has ID.
 * Only return values are supposed to be annotated with this annotation.
 *
 * @author David Orchard (orchard at pacificspirit.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {HasId.Validator.class, HasId.ListValidator.class})
public @interface HasId {

    String message() default "{com.pacificspirit.todo_jersey.webapp.constraint.HasId.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    public class Validator implements ConstraintValidator<HasId, Todo>  {

        @Override
        public void initialize(final HasId hasId) {
        }

        @Override
        public boolean isValid(final Todo todo, final ConstraintValidatorContext constraintValidatorContext) {
            return todo == null || todo.getId() != null;
        }
    }

    public class ListValidator implements ConstraintValidator<HasId, List<Todo>>  {

        private Validator validator = new Validator();

        @Override
        public void initialize(final HasId hasId) {
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
