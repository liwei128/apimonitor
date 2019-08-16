package com.ecar.apm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ecar.apm.http.client.HttpSequenceHandle;
import com.ecar.apm.model.HttpRequest;
import com.ecar.apm.model.HttpRequestLog;
import com.ecar.apm.model.HttpRequest.HttpMethod;
import com.ecar.apm.model.HttpRequest.ResultType;
import com.ecar.apm.model.HttpSequence;
import com.ecar.apm.model.HttpSequence.MonitorType;
import com.ecar.apm.service.HttpRequestService;
import com.ecar.apm.service.HttpSequenceService;
import com.ecar.apm.util.GuidGenerator;
import com.ecar.apm.util.PostManResolver;


@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpRequestServiceTest {

	@Autowired
	private HttpRequestService httpRequestService;

	@Autowired
	private HttpSequenceService httpSequenceService;
	
	
	@Test
	public void test() throws InterruptedException{
		
	    String guid = insertSingleInstance();
		
		enableMonitor(guid);
		Thread.sleep(30000);
		disableMonitor(guid);
		//deleteMonitor(guid);
		
	}

	

	public void cleanMonitorLogs(){
		httpRequestService.cleanMonitorLogs(4);
	}
	
	public void executeRequest(String guid){
		httpRequestService.executeRequest(guid);
	}
	
	
	public void enableMonitor(String guid){
		httpRequestService.enableMonitor(guid);
	}

	public void disableMonitor(String guid){
		httpRequestService.disableMonitor(guid);
	}
	
	public void deleteMonitor(String guid){
		httpRequestService.deleteMonitor(guid);
	}
	
	public void getAllHttpRequest(){
		List<HttpRequest> list = httpRequestService.getAllHttpRequest();
		for(HttpRequest request : list){
			System.out.println("guid="+request.getHttpMethod());
			
		}
	}

	public void getHttpRequestByGuid(){
		HttpRequest httpRequest = httpRequestService.getHttpRequestByGuid("71456d2b113d4b4a8cb00f0c9f3ccfb2");
		System.out.println(httpRequest.getHttpMethod());
	}

	
	@Transactional
	public String insertSingleInstance(){
		String guid1 = GuidGenerator.generate();
		String guid2 = GuidGenerator.generate();
		HttpSequence httpSequence = new HttpSequence();
		List<HttpRequest> list = new ArrayList<HttpRequest>();
		httpSequence.setGroup("test");
		httpSequence.setGuid(guid1);
		httpSequence.setName(guid1);
		httpSequence.setType(MonitorType.SINGLE);
		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setPguid(guid1);
		httpRequest.setGuid(guid2);
		httpRequest.setSort(1);
		httpRequest.setUrl("http://www.baidu.com");
		httpRequest.setHttpMethod(HttpMethod.GET);

		list.add(httpRequest);
		httpSequence.setHttpRequest(list);
		httpRequestService.insertHttpRequest(httpRequest);
		httpSequenceService.insert(httpSequence);
		return guid1;
	}
	
	@Transactional
	public String insertSequenceInstance(){

		HttpSequence httpSequence = new HttpSequence();
		String guid = GuidGenerator.generate();
		httpSequence.setGroup("test");
		httpSequence.setGuid(guid);
		httpSequence.setName(guid);
		httpSequence.setType(MonitorType.SEQUENCE);

		httpSequenceService.insert(httpSequence);
		//从postman脚本生产request对象
		ArrayList<HttpRequest> requests = PostManResolver.readFromJsonFile("classpath:testfile/testCase.postman_collection1.json");
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
		
		for(int i=0;i<requests.size();i++){
			HttpRequest item = requests.get(i);
			String id = GuidGenerator.generate();
			item.setGuid(id);
			item.setPguid(guid);
			item.setSort(i+1);
			httpRequestService.insertHttpRequest(item);
		}
		
		//执行接口链
		httpSequence.setHttpRequest(requests);
		return guid;
	}
}

