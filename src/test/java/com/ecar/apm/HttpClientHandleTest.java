package com.ecar.apm;

import java.util.ArrayList;

import com.ecar.apm.http.client.HttpClientHandler;
import com.ecar.apm.model.HttpRequest;
import com.ecar.apm.model.HttpRequestLog;
import com.ecar.apm.util.PostManResolver;

public class HttpClientHandleTest {
	public static void main(String[] args) throws Exception{
		testJiankongbao();
	}
	
	public static void test(){
		ArrayList<HttpRequest> requests = PostManResolver.readFromJsonFile("classpath:testCase.postman_collection1.json");
		ArrayList<HttpRequestLog> logs = new ArrayList<HttpRequestLog>();
		for(HttpRequest request : requests){
			HttpClientHandler handle = new HttpClientHandler(request,null);
			HttpRequestLog log = handle.execute();
			logs.add(log);
		}
		for(HttpRequestLog log : logs){
			System.out.println(log);
		}
	}

	public static void testJiankongbao(){
		ArrayList<HttpRequest> requests = PostManResolver.readFromJsonFile("classpath:jiankongbao.postman_collection.json");
		ArrayList<HttpRequestLog> logs = new ArrayList<HttpRequestLog>();
		for(HttpRequest request : requests){
			HttpClientHandler handle = new HttpClientHandler(request,null);
			HttpRequestLog log = handle.execute();
			logs.add(log);
		}
		for(HttpRequestLog log : logs){
			System.out.println(log);
		}
	}
	
}
