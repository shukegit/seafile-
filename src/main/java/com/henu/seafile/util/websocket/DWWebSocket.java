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

@Component
@ServerEndpoint("/seafilewebsocket/{token}")
public class DWWebSocket {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 在线人数
     */
    public static int onlineNumber = 0;
    /**
     * 以用户的姓名为key，WebSocket为对象保存起来
     */
    private static Map<String, DWWebSocket> clients = new ConcurrentHashMap<String, DWWebSocket>();
    /**
     * 会话
     */
    private Session session;
    /**
     * 用户名称
     */
    private String token;
    
    /**
     * 连接成功后触发
     * @param username
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("token") String token, Session session) {
    	onlineNumber++;
    	logger.info("有新连接加入！ 当前在线人数" + onlineNumber);
        this.token = token;
        this.session = session;
    	//把自己的信息加入到map当中去
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
    public void sendMessage(String message, String TOKEN) throws IOException {
        for (DWWebSocket item : clients.values()) {
            if (item.token.equals(TOKEN) ) {
                item.session.getAsyncRemote().sendText(message);
                break;
            }
        }
    }
    
    public void sendMessageToAll(String message,String FromUserName) throws IOException {
        for (DWWebSocket item : clients.values()) {
                item.session.getAsyncRemote().sendText(message);
        }
    }

}
