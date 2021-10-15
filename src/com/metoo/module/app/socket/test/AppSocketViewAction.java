package com.metoo.module.app.socket.test;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.catalina.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//@Component
//@ServerEndpoint(value = "/websocket/{ip}")
public class AppSocketViewAction {

	private static final Logger log = LoggerFactory.getLogger(AppSocketViewAction.class);

	// 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;
	
	// concurrent包的线程安全Map，用来存放每个客户端对应的MyWebSocket对象。
	private static ConcurrentHashMap<String, AppSocketViewAction> webSocketMap = new ConcurrentHashMap<String, AppSocketViewAction>();

	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	
	private String ip; // 客户端ip
	public static final String ACTION_PRINT_ORDER = "printOrder";
	public static final String ACTION_SHOW_PRINT_EXPORT = "showPrintExport";
	
	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("ip") String ip) {
		this.session = session;
		this.ip = ip;
		webSocketMap.put(ip, this);
		addOnlineCount(); // 在线数加1
//		System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
		log.info("有新连接加入,ip:{}！当前在线人数为:{}", ip, getOnlineCount());
		/*ExportService es = BeanUtils.getBean(ExportService.class);
		List<String> list = es.listExportCodesByPrintIp(ip);
		ResponseData<String> rd = new ResponseData<String>();
		rd.setAction(AppSocketViewAction.ACTION_SHOW_PRINT_EXPORT);
		rd.setList(list);
		sendObject(rd);*/
		
	}
	
	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(@PathParam("ip") String ip) {
		webSocketMap.remove(ip); // 从set中删除
		// Map<String, String> map = session.getPathParameters();
		// webSocketMap.remove(map.get("ip"));
		subOnlineCount(); // 在线数减1
		// System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
		log.info("websocket关闭，IP：{},当前在线人数为:{}", ip, getOnlineCount());
	}
 
	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message
	 *            客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
//		System.out.println("来自客户端的消息:" + message);
		log.debug("websocket来自客户端的消息:{}", message);
		/*OrderService os = BeanUtils.getBean(OrderService.class);
		OrderVo ov = os.getOrderDetailByOrderNo(message);*/
//		System.out.println(ov);
		/*ResponseData<OrderVo> rd = new ResponseData<OrderVo>();
		ArrayList<OrderVo> list = new ArrayList<OrderVo>();
		list.add(ov);
		rd.setAction(AppSocketViewAction.ACTION_PRINT_ORDER);
		rd.setList(list);
		sendObject(rd);*/
//		log.info("推送打印信息完成，单号：{}", ov.getOrderNo());
	}
 
	/**
	 * 发生错误时调用
	 */
	@OnError
	public void onError(Session session, Throwable error) {
//		System.out.println("发生错误");
		log.error("webSocket发生错误！IP：{}", ip);
		error.printStackTrace();
	}
 
	/**
	 * 像当前客户端发送消息
	 * 
	 * @param message
	 *            字符串消息
	 * @throws IOException
	 */
	public void sendMessage(String message) {
		/*try {
			//this.session.getBasicRemote().sendText(message);
			// this.session.getAsyncRemote().sendText(message);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("发送数据错误，ip:{},msg：{}", ip, message);
		}*/
	}
 
	/**
	 * 向当前客户端发送对象
	 * 
	 * @param obj
	 *            所发送对象
	 * @throws IOException
	 */
	public void sendObject(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String s = null;
		try {
			s = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			log.error("转json错误！{}", obj);
		}
		this.sendMessage(s);
	}
 
	/**
	 * 群发自定义消息
	 */
	public static void sendInfo(String message) {
		for (Entry<String, AppSocketViewAction> entry : webSocketMap.entrySet()) {
			AppSocketViewAction value = entry.getValue();
			value.sendMessage(message);
		}
	}
 
	public static synchronized int getOnlineCount() {
		return onlineCount;
	}
 
	public static synchronized void addOnlineCount() {
		AppSocketViewAction.onlineCount++;
	}
 
	public static synchronized void subOnlineCount() {
		AppSocketViewAction.onlineCount--;
	}
 
	public static ConcurrentHashMap<String, AppSocketViewAction> getWebSocketMap() {
		return webSocketMap;
	}
 
}
