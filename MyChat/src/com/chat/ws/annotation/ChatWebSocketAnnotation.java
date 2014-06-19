package com.chat.ws.annotation;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.chat.model.User;

@ServerEndpoint(value="/websocket/chat")
public class ChatWebSocketAnnotation{
	
	private static final Set<ChatWebSocketAnnotation> connections = new CopyOnWriteArraySet<ChatWebSocketAnnotation>(); 
	private static final String GUEST = "游客";
	private static final AtomicInteger connectionIds = new AtomicInteger(0);
	
	private Session session;
	private User user;
	
	public ChatWebSocketAnnotation(){
		this.user.setName(GUEST + connectionIds.getAndIncrement());
	}

	@OnOpen
	public void open(Session session){
		this.session = session;
		connections.add(this);
		String message = "欢迎[" + user.getName() + "]加入群聊";
		broadcast(message);
	}
	
	@OnClose
	public void close(){
		connections.remove(this);
		String message = "[" + this.user.getName() + "]退出了群聊";
		broadcast(message);
	}
	
	@OnMessage
	public void sendMessage(String message){
		String filteredMessage = String.format("%s: %s",this.user.getName(), message.trim());
		broadcast(filteredMessage);
	}
	
	@OnError
	public void onError(){
		System.out.println("System occurs error");
	}
	
	public static void broadcast(String message){
		for(ChatWebSocketAnnotation chat : connections){
			synchronized (chat) {
				try {
					chat.session.getBasicRemote().sendText(message);
				} catch (IOException e) {
					chat.close();
				}
			}
		}
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
