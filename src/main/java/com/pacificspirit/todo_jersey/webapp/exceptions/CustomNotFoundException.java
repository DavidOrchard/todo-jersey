package com.pacificspirit.todo_jersey.webapp.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.validation.ValidationError;

/**
 * This exceptions will get mapped to a 404 response with the application exception mapper
 * implemented by {@link NotFoundExceptionMapper} class.
 *
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 */
public class CustomNotFoundException extends WebApplicationException {
	/**
	  * Create a HTTP 404 (Not Found) exception.
	  */
	  public CustomNotFoundException() {
	    super(Response.status(404).build());
	  }
	 
	  /**
	  * Create a HTTP 404 (Not Found) exception.
	  * @param err the Validation Error that is the entity of the 404 response.
	  */
	  public CustomNotFoundException(ValidationError err) {
      	// TODO: use list.  very annoying, I can't figure out how to get a List<ValidationError> to serialize

	    super(Response.status(404).
	    entity("[{\"message\":\"" + err.getMessage() + "\",\"messageTemplate\":\"" + err.getMessageTemplate() + "\"}]").type("application/json").build());
	  }
}
