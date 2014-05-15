package com.pacificspirit.todo_jersey.webapp.service;

import java.util.*; 

import com.twilio.sdk.*; 
import com.twilio.sdk.resource.factory.*; 
import com.twilio.sdk.resource.instance.*; 

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;


public class MessageService {
	
	 public static final String ACCOUNT_SID = "AC5cc193fbbc0c65efa75c563ce340d54c"; 
	 public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
     public static final TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN); 
	 public static MessageFactory messageFactory;
	 public static void send(String msg) {
		 send(msg, messageFactory); 
	 }
	 
	 public static void send(String msg, MessageFactory mf) {
		 messageFactory = (mf == null ? client.getAccount().getMessageFactory() : mf);
		 try {
		 
		 // Build the parameters 
		 List<NameValuePair> params = new ArrayList<NameValuePair>();  
		 params.add(new BasicNameValuePair("From", "+17786542410"));  
		 params.add(new BasicNameValuePair("To", "+16047907978"));  
		 params.add(new BasicNameValuePair("Body", msg));  
		 
		 Message message = messageFactory.create(params); 
		 }
		 catch(Exception e){
			 System.out.println(e);
		 }
	 } 
	}
