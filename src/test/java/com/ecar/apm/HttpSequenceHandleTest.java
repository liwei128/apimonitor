package com.ecar.apm;

import java.util.ArrayList;
import java.util.HashMap;

import com.ecar.apm.http.client.HttpClientHandler;
import com.ecar.apm.http.client.HttpSequenceHandle;
import com.ecar.apm.model.HttpRequest;
import com.ecar.apm.model.HttpRequestLog;
import com.ecar.apm.model.HttpSequence;
import com.ecar.apm.model.HttpRequest.ResultType;
import com.ecar.apm.util.PostManResolver;

public class HttpSequenceHandleTest {
	public static void main(String[] args) throws Exception{
		testDashboard();
	}

	public static void testDashboard(){
		//从postman脚本生产request对象
		ArrayList<HttpRequest> requests = PostManResolver.readFromJsonFile("classpath:dashboard.postman_collection.v1.json");
		//执行接口链
		HttpSequence sequence = new HttpSequence();
		sequence.setHttpRequest(requests);
		HttpSequenceHandle HttpSequenceHandle = new HttpSequenceHandle(sequence);
		
		HttpSequenceHandle.execute();
		
		ArrayList<HttpRequestLog> logs = HttpSequenceHandle.httpRequestLogList;
		for(HttpRequestLog log : logs){
			System.out.println(log);
		}
	}
	
	public static void testAndaily(){
		//从postman脚本生产request对象
		ArrayList<HttpRequest> requests = PostManResolver.readFromJsonFile("classpath:testCase.postman_collection1.json");
		//提取变量$$csrf
		HttpRequest request0 = requests.get(0);
		HashMap<String, String> variables = request0.getVariablesMap();
		variables.put("$$csrf", "//meta[@name='X-CSRF-TOKEN']/@content");
		request0.setResultType(ResultType.XML);
		request0.setVariablesMap(variables);
		//设置参数_csrf
		HttpRequest request1 = requests.get(1);
		HashMap<String, String> params = request1.getParametersMap();
		params.put("_csrf", "$$csrf");
		request1.setParametersMap(params);
		
		
		//执行接口链
		HttpSequence sequence = new HttpSequence();
		sequence.setHttpRequest(requests);
		HttpSequenceHandle HttpSequenceHandle = new HttpSequenceHandle(sequence);
		
		HttpSequenceHandle.execute();
		
		ArrayList<HttpRequestLog> logs = HttpSequenceHandle.httpRequestLogList;
		for(HttpRequestLog log : logs){
			System.out.println(log);
		}
	}
}
