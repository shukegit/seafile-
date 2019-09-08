package com.henu.seafile.util.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 这是自己的双工通信版本
 * 作为通知用户重复登录的工具
 * @author shuke
 *@Conponent:把普通类交由spring托管
 */
@Component
@ServerEndpoint("/seafilewebsocket/{token}")
public class WebSocket {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 在线人数
     */
    public static int onlineNumber = 0;
    /**
     * 以用户的姓名为key，WebSocket为对象保存起来
     */
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();
    /**
     * 会话
     */
    private Session session;
    /**
     * 用户token
     */
    private String token;
    
    /**
     * 连接成功后触发
     * @param username
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("token") String token, Session session) throws IOException {
    	onlineNumber++;
    	logger.info("有新连接加入！ 当前在线人数" + onlineNumber);
        this.token = token;
        this.session = session;
    	clients.put(token, this);
        
    }
    
    @OnClose
    public void onClose() {
    	onlineNumber--;
    	logger.info("有连接关闭！ 当前在线人数" + onlineNumber);
    	clients.remove(token);
    }
    
    @OnError
    public void onError(Session session, Throwable error) {
        logger.info("服务端发生了错误"+error.getMessage());
        //error.printStackTrace();
    }
    
    /**
     * 收到客户端消息后触发
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
    	
    }
    
    
    /**
     * 这个用于发送消息，可以通过这个通知前台下线
     * @param message
     * @param ToUserName
     * @throws IOException
     */
    public void sendMessage(String message, String TOKEN) {
    	try {
    		for (WebSocket item : clients.values()) {
                if (item.token.equals(TOKEN) ) {
                    item.session.getAsyncRemote().sendText(message);
                    break;
                }
            }
		} catch (Exception e) {
		}
        
    }
    
    public void sendMessageToAll(String message) throws IOException {
        for (WebSocket item : clients.values()) {
                item.session.getAsyncRemote().sendText(message);
        }
    }
    
    public static synchronized int getOnlineCount() {
        return onlineNumber;
    }

}
