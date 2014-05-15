package com.pacificspirit.todo_jersey.webapp.service;

import java.util.*; 

import com.twilio.sdk.*; 
import com.twilio.sdk.resource.factory.*; 

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;


public class MessageService {
	
	 public static final String ACCOUNT_SID = "AC5cc193fbbc0c65efa75c563ce340d54c"; 
	 public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
     public static final TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN); 
	 public static MessageFactory messageFactory;
	 
	 public static void init(MessageFactory mf) {
		 messageFactory = mf;
	 }
	 
	 public static void send(String msg ) {
		 if( messageFactory == null ) {
			 messageFactory = client.getAccount().getMessageFactory();
		 }
		 try {
		 
		 // Build the parameters 
		 List<NameValuePair> params = new ArrayList<NameValuePair>();  
		 params.add(new BasicNameValuePair("From", "+17786542410"));  
		 params.add(new BasicNameValuePair("To", "+16047907978"));  
		 params.add(new BasicNameValuePair("Body", msg));  
		 
		 messageFactory.create(params); 
		 }
		 catch(Exception e){
			 System.out.println(e);
		 }
	 } 
	}
