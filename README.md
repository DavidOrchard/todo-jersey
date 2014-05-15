# Todo

This demonstrates a Todo web app and API built with [**Jersey**](https://jersey.java.net/), [**MongoDB**](http://mongodb.org), [**Elastic Search**](http://www.elasticsearch.org/), [**SearchBox.io**](http://www.searchbox.io/), [**AngularJS**](http://angularjs.org), and [**Bootstrap**](http://twitter.github.com/bootstrap)

# Requirements

A Todo list app with a JSON REST API.

1) The todo items should have endpoints that provide the following functionalities:

- Get
- Delete
- Save
- Update
- Mark as done/undone
- Search 

Todo items have a title, a body, and a boolean value to check if it has been done or not.

{
    "title" : "",
    "body" : "",
    "done" : false
}

2) Store the items in-memory and MongoDB

3) The search endpoint should search the query in the title and in the body of the todo item, giving an higher ranking to the matches found in the title.

4) When an API is marked as done, send an sms like: 

"Cleaning the house" task has been marked as done.

## Contents

The mapping of the API URI path space is presented in the following table:

<table border="1">
    <tr>
        <th>URI path</th>
        <th>Resource class</th>
        <th>HTTP methods</th>
        <th>Allowed values</th>
    </tr>

    <tr>
        <td>/api/todo</td>
        <td>TodoResource</td>
        <td>GET, POST, DELETE</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>/api/todo/{id}</td>
        <td>TodoResource</td>
        <td>GET, DELETE, PUT, <i>PATCH</i></td>
        <td>id - alphanumeric string</td>
    </tr>
    <tr>
        <td>/api/todo/search?q={searchValue}</td>
        <td>SearchResource</td>
        <td>GET</td>
        <td>searchValue - non empty string</td>
    </tr>
</table>

Application is configured by using web.xml, which registers
[javax.ws.rs.core.Application](https://jax-rs-
spec.java.net/nonav/2.0/apidocs/javax/ws/rs/core/Application.html) descendant
to get classes and singletons from it (see class MyApplication). Bean
Validation annotations are present on resource classes (see todoResource,
SearchResource) as well as on the domain class (see todo).

When `curl` (see examples below) is used for sending requests to the server,
one can see how to affect the media type of returned validation errors (if
any).

Allowed media types for validation errors entities are:
"text/plain"/"text/html" or "application/json"/"application/xml" (appropriate
provider has to be registered to transform errors into JSON/XML)

Note: `jersey.config.beanValidation.enableOutputValidationErrorEntity.server`
init parameter (`web.xml`) has to be enabled to let Jersey know that it should
send validation errors as a Response entities (this behavior if disabled by
default).

## Running the App

Run the app:

<blockquote><pre> mvn clean package jetty:run</pre></blockquote>

This deploys current example using Jetty. You can access the application at
[http://localhost:8080/todo](http://localhost:8080/todo-jersey-mongo-twilio-elastic-search)

You can access resources of this application also using curl:

<blockquote><pre> curl -X POST --data '{"title":"hi","body":"text body", "done":false}' -H
"Accept: application/json" -H "Content-type: application/json"
http://localhost:8080/todo-jersey-mongo-twilio-elastic-search/api/todo

curl -X PUT --data
'{"title":"hi2","id":1, "body":"text body", "done":false}' -H "Accept: application/json" -H "Content-type: application/json" http://localhost:8080/todo-jersey-mongo-twilio-elastic-search/api/todo/1

curl -X PATCH --data '{"title":"hi2","id":1}' -H "Accept: application/json" -H "Content-type: application/json" http://localhost:8080/todo-jersey-mongo-twilio-elastic-search/api/todo/1

curl -H "Accept: application/json" http://localhost:8080/todo-jersey-mongo-twilio-elastic-search/api/todo

curl -H "Accept: application/json" http://localhost:8080/todo-jersey-mongo-twilio-elastic-search/api/todo/search?q=hi

curl -H "Accept: application/json" http://localhost:8080/todo-jersey-mongo-twilio-elastic-search/api/todo/search?q=*er*

curl -X DELETE http://localhost:8080/todo-jersey-mongo-twilio-elastic-search/api/todo/1

</pre></blockquote>

## Lessons learned, thoughts

Every one of these technologies is brittle at the edges with little support.  I would far rather use Nodejs or Scala for building the app.

*** Jersey ***

Out of the box, it seemed ok.  Eclipse debugging is good, tests are doable and mockito is easy to use.  Lots of examples.

But very straightforward items were hard.  

- Returning a status code other than 400 or 500, such as 404!!!, with an error message required client code check, a custom exception, a custom exception mapper, and custom ugly martialling of the List<ValidationError>.  [Jersey Representations Docs](https://jersey.java.net/documentation/latest/representations.html#d0e5155) The annotation should provide the ability to set the status code in addition to the message for any particular validation failure

- Response entity builder inability to serialize a List&lt;ValidationError&gt;, meaning junk like <pre>entity("[{\"message\":\"" + err.getMessage() + "\",\"messageTemplate\":\"" + err.getMessageTemplate() + "\"}]").type("application/json").build());
</pre>

- Client side library cannot send HTTP PATCH method with a body.

- No easy way to do conditional validation rules.  I wanted to have the same interface for validating IDs that are Int for in-memory and alphanumeric strings for MongoDB backends.

*** MongoDB ***

Reasonable.  My complaints are about inability to easily convert JSON or annotated POJOs to BSON.  I would have thought it would be incredibly common to do that, but I had to write a Todo2DBObject and DBObject2Todo converters.  That's frankly awful to have to manually edit 2 functions every time an attribute is added/changed/deleted on the type.

*** Twilio ***

Great!

*** Elastic Search and SearchBox.io ***

Reasonable as well.  There seems to be a lot of change in the api that isn't tracked well by the client.  The Java client has gone from 0.0.5 to 0.1.1 very quickly.  It doesn't track the API at a very basic level.  

- sometimes messages return an "status":"ok", but the getSucceeded() call checks for status AND an "acknowledged":"true".  This always generates a Java NPE
- the update index responses with "added":true, but the Index.getSucceeded() checks for status, always generating an NPE.

This churn is evident in the 0.1.0 library that commented out the check for status and acknowledged and then the 0.1.1 added part of the check back in.

