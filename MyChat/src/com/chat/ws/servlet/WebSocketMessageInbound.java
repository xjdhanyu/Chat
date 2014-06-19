package com.chat.ws.servlet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import com.chat.model.User;

public class WebSocketMessageInbound extends MessageInbound  {
	
	private User user ; 
	private HttpSession session = null;

	public WebSocketMessageInbound(HttpServletRequest request,User user) {
		this.user = user;
		session = request.getSession();
	}

	@Override
	protected void onOpen(WsOutbound outbound) {
		WebSocketMessageInboundPool.addInbound(this);
		WebSocketMessageInboundPool.sendWelcomeMessage("��ӭ[" + this.user.getName() + "]�û�����Ⱥ��");
	}

	@Override
	protected void onClose(int status) {
		WebSocketMessageInboundPool.sendWelcomeMessage("[" + this.user.getName() + "]�û��Ƴ�Ⱥ��");
		WebSocketMessageInboundPool.removeInbound(this);
	}
	
	@Override
	protected void onBinaryMessage(ByteBuffer message) throws IOException {
		throw new UnsupportedOperationException("Binary message not supported.");
	}

	@Override
	protected void onTextMessage(CharBuffer message) throws IOException {
		String user = (String)session.getAttribute("user");
		WebSocketMessageInboundPool.sendMessage(user,message.toString());
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


}
