import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

public class RunManager {

	public static void main(String[] args) throws Exception {

		new RunManager().start();
	}
	
	public void start() throws Exception {
		
		Server server = new Server();
		
		ServerConnector http = new ServerConnector(server);
		http.setHost("127.0.0.1");
		http.setPort(8080);
		
		server.addConnector(http);
		
		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(MyServlet.class, "/*");
		server.setHandler(servletHandler);
		
		server.start();
		server.join();
	}

}


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

public class MyServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	static HashMap<String, MsgQueue> queue;

	public MyServlet() {
		queue = new HashMap<String, MsgQueue>();
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		// URI
		String reqUri = req.getRequestURI();
		String[] words = reqUri.split("/");
		String command = words[1];
		String qname = words[2];
		
		String result = "";
		// 동작
		switch(command)
		{
		case "RECEIVE":
			result = deQueue(qname);
			break;
		}
		
		// 응답
		res.setStatus(200);
		res.getWriter().write(result);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		// URI
		String reqUri = req.getRequestURI();
		String[] words = reqUri.split("/");
		String command = words[1];
		String qname = words[2];
		
		// 요청 Body
		InputStreamReader stream = new InputStreamReader(req.getInputStream());
		BufferedReader input = new BufferedReader(stream);
		
		// Json
//		Gson gson = new Gson();
//		JsonReader reader = new JsonReader(input);
//		JsonObject jsonRead = gson.fromJson(reader, JsonObject.class);
		
		JsonObject jsonWrite = new JsonObject();
				
		// 동작
		switch(command)
		{
		case "CREATE":
//			int qsize = jsonRead.get("QueueSize").getAsInt();
//			String result = createQueue(qname, qsize);
			String result = createQueue(qname, input);
			jsonWrite.addProperty("Result", result);
			break;
		case "SEND":
//			String message = jsonRead.get("Message").getAsString();
//			String result2 = enQueue(qname, message);
			String result2 = enQueue(qname, input);
			jsonWrite.addProperty("Result", result2);
			break;
		case "ACK":
			String messageId = words[3];
			String result3 = deleteMsg(qname, messageId);
			jsonWrite.addProperty("Result", result3);
			break;
		case "FAIL":
			String messageId2 = words[3];
			String result4 = setMsg(qname, messageId2);
			jsonWrite.addProperty("Result", result4);
			break;
		}
		
		input.close();
		
		// 응답
		res.setStatus(200);
		res.getWriter().write(jsonWrite.toString());
	}

//	static String createQueue(String qname, int qsize) {
//		
//		if(queue.containsKey(qname)) {
//			return "Queue Exist";
//		} 
//		
//		MsgQueue q = new MsgQueue(qsize);
//		queue.put(qname, q);
//		
//		return "OK";
//	}
	
	static String createQueue(String qname, BufferedReader input) {
		
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(input);
		JsonObject jsonRead = gson.fromJson(reader, JsonObject.class);
		
		int qsize = jsonRead.get("QueueSize").getAsInt();
		
		if(queue.containsKey(qname)) {
			return "Queue Exist";
		} 
		
		MsgQueue q = new MsgQueue(qsize);
		queue.put(qname, q);
		
		return "OK";
	}
	
//	static String enQueue(String qname, String message) {
//		
//		return queue.get(qname).enqueue(message);
//	}
	
	static String enQueue(String qname, BufferedReader input) {
		
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(input);
		JsonObject jsonRead = gson.fromJson(reader, JsonObject.class);
		
		String message = jsonRead.get("Message").getAsString();
		
		return queue.get(qname).enqueue(message);
	}
	
	static String deQueue(String qname) {
		
		return queue.get(qname).getMsg(qname);
	}
	
	static String deleteMsg(String qname, String messageId) {
		
		return queue.get(qname).deleteMsg(qname, messageId);
	}
	
	static String setMsg(String qname, String messageId) {
		
		return queue.get(qname).setMsg(qname, messageId);
	}
}



package com.lgcns.test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.JsonObject;

public class MsgQueue {

	private int size;
	private int seqNo;
	
	private LinkedHashMap<Integer, List<String>> hashMsg;
	
	
	// 생성자
	public MsgQueue(int size) {
		this.size = size;
		this.seqNo = 0;
		hashMsg = new LinkedHashMap<Integer, List<String>>();
	}
	
	public String enqueue(String message) {
		
		if(hashMsg.size() == size) {
			return "Queue Full";
		}
		
		List<String> listMsg = new ArrayList<String>();
		listMsg.add("A"); 				// status : available
		listMsg.add(message);
		hashMsg.put(seqNo++, listMsg);
		return "OK";
	}
	
	public String dequeue() {
		
		if(hashMsg.size() == 0) {
			return "No Message";
		}
		
		int key = (int)hashMsg.keySet().iterator().next();
		String res = hashMsg.get(key).get(0);
		hashMsg.remove(key);
		
		return res;
	}
	
	public String getMsg(String qname) {
		
		JsonObject jsonObj = new JsonObject();
		
		if(hashMsg.size() > 0) {
			for(Integer key : hashMsg.keySet()) {
				if(hashMsg.get(key).get(0).equals("A")) {
					List<String> val = hashMsg.get(key);
					val.set(0, "U");
					hashMsg.put(key, val);
					
					jsonObj.addProperty("Result", "OK");
					jsonObj.addProperty("MessageID", (qname + "_" + key));
					jsonObj.addProperty("Message", val.get(1));
					
					return jsonObj.toString();
				}
			}
		}
		
		jsonObj.addProperty("Result", "No Message");
		return jsonObj.toString();
	}
	
	public String deleteMsg(String qname, String messageId) {
		
		String[] words = messageId.split("_");
		int qkey = Integer.parseInt(words[1]);
		
		if(hashMsg.containsKey(qkey)) {
			
			hashMsg.remove(qkey);
			
			return "OK";
		}
		
		return "No Message";
	}
	
	public String setMsg(String qname, String messageId) {
		
		String[] words = messageId.split("_");
		int qkey = Integer.parseInt(words[1]);
		
		if(hashMsg.size() > 0) {
			if(hashMsg.containsKey(qkey)) {
				hashMsg.get(qkey).set(0, "A");
				return "OK";
			}
		}
		
		return "No Message";
	}
}
