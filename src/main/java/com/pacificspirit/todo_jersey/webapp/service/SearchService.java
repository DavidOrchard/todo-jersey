package com.pacificspirit.todo_jersey.webapp.service;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.ClientConfig;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.pacificspirit.todo_jersey.webapp.domain.Todo;

import java.io.IOException;
import java.util.List;
	
public class SearchService {
	public static final String APIKey = System.getenv("SEARCHLY_API_KEY");
	private static final String connectionURL = "http://site:" + APIKey + "@dwalin-us-east-1.searchly.com";
	private static JestClient client = null;
	private static final String indexName = "todos";
	private static final String indexedType = "todo";
	
	public static void init() {
    
	 // Configuration
	 ClientConfig clientConfig = new ClientConfig.Builder(connectionURL).multiThreaded(true).build();

	 // Construct a new Jest client according to configuration via factory
	 JestClientFactory factory = new JestClientFactory();
	 factory.setClientConfig(clientConfig);
	 client = factory.getObject();
	 index();
	}
	
	public static void clear() {
		try {
	        // Delete articles index if it is exists
			IndicesExists indicesExists = new IndicesExists.Builder(indexName).build();
            JestResult result = client.execute(indicesExists);

            if (result.isSucceeded()) {
            	DeleteIndex deleteIndex = new DeleteIndex.Builder(indexName).build();
            	client.execute(deleteIndex);
	        } 
		} catch (IOException e) {
			System.out.println("SearchService.clear");
        	System.out.println(e);
	//          logger.error("Indexing error", e);
	    } catch (Exception e) {
	    	System.out.println("SearchService.clear");
        	System.out.println(e);
	//          logger.error("Indexing error", e);
	    } finally {
	    	index();
	    }
	}
	
	/*
	 * Index the db 
	 */
	
	public static void index() {

        try {
            
        	IndicesExists indicesExists = new IndicesExists.Builder(indexName).build();
            JestResult result = client.execute(indicesExists);

            if (!result.isSucceeded()) {
                // Create todos index
                CreateIndex createIndex = new CreateIndex.Builder(indexName).build();
                client.execute(createIndex);
            }
 
        } catch (IOException e) {
        	System.out.println("SearchService.index");
        	System.out.println(e);
//            logger.error("Indexing error", e);
        } catch (Exception e) {
        	System.out.println("SearchService.index");
        	System.out.println(e);
//            logger.error("Indexing error", e);
        }
    	System.out.println("SearchService.index done");
	}
	
	public static void add(Todo todo) {
		if(client == null) {
			init();
		}
		try {

	        Index index = new Index.Builder(todo).index(indexName).type(indexedType).id(todo.getId()).build();
	        JestResult result = client.execute(index);
	
	        System.out.println(result.getJsonString());

        } catch (IOException e) {
        	System.out.println("SearchService.add");
        	System.out.println(e);
//            logger.error("Indexing error", e);
        } catch (Exception e) {
        	System.out.println("SearchService.add");
        	System.out.println(e);
//            logger.error("Indexing error", e);
        }
	}
	
	public static void remove(Todo todo) {
		if(client == null) {
			init();
		}
		try {
			
	        Delete delete = new Delete.Builder(indexName, indexedType, todo.getId()).build();
	        JestResult result = client.execute(delete);
	
	        System.out.println(result.getJsonString());

        } catch (IOException e) {
        	System.out.println("SearchService.remove");
        	System.out.println(e);
//            logger.error("Indexing error", e);
        } catch (Exception e) {
        	System.out.println("SearchService.remove");
        	System.out.println(e);
//            logger.error("Indexing error", e);
        }
	}

	
	public static List<Todo> search(String term) {
		try {
	        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.query(QueryBuilders.queryString(term));
	
			Search search = (Search) new Search.Builder(searchSourceBuilder.toString())
			                                // multiple index or types can be added.
			                                .addIndex(indexName)
			                                .addType(indexedType)
			                                .build();
	
			// elastic search + jest can throw NPE: https://github.com/spring-io/sagan/issues/274
			JestResult result = client.execute(search);
			if(!result.isSucceeded()) {
				System.out.println("Search error, jsonString = " + result.getJsonString());
			}
			return result.getSourceAsObjectList(Todo.class);
        } catch (IOException e) {
        	System.out.println("SearchService.search");
        	System.err.print(e);
        	
//            logger.error("Search error", e);
        } catch (Exception e) {
        	System.out.println("SearchService.search");
        	System.err.print(e);
//            logger.error("Search error", e);
        }
		return null;
	}
}
