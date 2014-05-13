
package com.pacificspirit.todo_jersey.webapp.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * An exception mapper to return 404 responses when a {@link CustomNotFoundException} is thrown.
 *
 * @author David Orchard (orchard at pacificspirit.com)
 */
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<CustomNotFoundException> {

    @Override
    public Response toResponse(CustomNotFoundException exception) {
    	System.out.println("got CustomNFE");
        return Response.status(404).build();
    }

}
