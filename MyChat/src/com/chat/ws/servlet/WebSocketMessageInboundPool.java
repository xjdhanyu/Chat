package com.chat.ws.servlet;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.chat.model.User;

public class WebSocketMessageInboundPool {
	
	private static final Map<String,WebSocketMessageInbound > connections = new HashMap<String,WebSocketMessageInbound>();
	
	public String currentUser = null;
	

	public static void sendWelcomeMessage(String message) {
		Set<String> userSet = connections.keySet();
		for(String user : userSet){
			WebSocketMessageInbound inbound = connections.get(user);
			if(inbound != null){
				try {
					inbound.getWsOutbound().writeTextMessage(CharBuffer.wrap(message));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void sendMessage(User user,String message) {
		Set<String> userSet = connections.keySet();
		for(String userName : userSet){
			WebSocketMessageInbound inbound = connections.get(userName);
			if(inbound != null){
				try {
					if(user.getName().equals(userName))
						message = String.format("%s: %s",user.getName(), message.trim());
					inbound.getWsOutbound().writeTextMessage(CharBuffer.wrap(message));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void sendMessage(String user,String message) {
		Set<String> userSet = connections.keySet();
		for(String userName : userSet){
			WebSocketMessageInbound inbound = connections.get(userName);
			if(inbound != null){
				try {
					if(message.indexOf(":") == -1)
						message = String.format("%s: %s",user, message.trim());
					inbound.getWsOutbound().writeTextMessage(CharBuffer.wrap(message));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public static void addInbound(WebSocketMessageInbound inbound) {
		connections.put(inbound.getUser().getName(), inbound);
	}


	public static void removeInbound(WebSocketMessageInbound inbound) {
		connections.remove(inbound.getUser().getName());
	}

}
