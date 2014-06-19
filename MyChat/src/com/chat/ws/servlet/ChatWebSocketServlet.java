package com.chat.ws.servlet;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

import com.chat.model.User;

@WebServlet(urlPatterns = { "/websocket/chat"})
public class ChatWebSocketServlet extends WebSocketServlet{

	private static final long serialVersionUID = -9196457230188597585L;
	public static final String GUEST = "сн©м";
	private static final AtomicInteger GID = new AtomicInteger(1);
	
	private User user = new User();
	
	public User getUser(HttpServletRequest request){
		HttpSession session = request.getSession();
		String name = (String)session.getAttribute("user");
		if(name == null){
			String userName = request.getParameter("userName");
			if(userName == null)
				userName = GUEST + GID.getAndIncrement();
			session.setAttribute("user", userName);
			this.user.setName(userName);
		}else{
			this.user.setName(name);
		}
		return this.user;
	}

	@Override
	protected StreamInbound createWebSocketInbound(String arg0,HttpServletRequest request) {
		return new WebSocketMessageInbound(request,this.getUser(request));
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
