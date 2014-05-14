

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
 * Checks whether a given {@link org.Todo.jersey.examples.beanvalidation.webapp.domain.Todo} string is a valid ID.
 *
 * @author David Orchard (orchard at pacificspirit.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidId.Validator.class, ValidId.ListValidator.class})
public @interface ValidId {

    String message() default "{com.pacificspirit.todo_jersey.webapp.constraint.ValidId.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    public class Validator implements ConstraintValidator<ValidId, String>  {

        @Override
        public void initialize(final ValidId validId) {
        }

        @Override
        public boolean isValid(final String id, final ConstraintValidatorContext constraintValidatorContext) {
            return id.matches("^[0-9a-fA-F]{24}$");
        }
    }

    public class ListValidator implements ConstraintValidator<ValidId, List<String>>  {

        private Validator validator = new Validator();

        @Override
        public void initialize(final ValidId validId) {
        }

        @Override
        public boolean isValid(final List<String> ids, final ConstraintValidatorContext constraintValidatorContext) {
            boolean isValid = true;
            for (final String id : ids) {
                isValid &= validator.isValid(id, constraintValidatorContext);
            }
            return isValid;
        }
    }
}
