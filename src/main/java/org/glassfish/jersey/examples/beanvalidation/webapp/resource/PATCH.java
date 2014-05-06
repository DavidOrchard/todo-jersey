package org.glassfish.jersey.examples.beanvalidation.webapp.resource;

import javax.ws.rs.HttpMethod;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Target({ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME) 
@HttpMethod("PATCH") 
public @interface PATCH { 
} 