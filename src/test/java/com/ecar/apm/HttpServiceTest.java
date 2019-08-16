package com.ecar.apm;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;


@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpServiceTest {
	
	
	@Test
	public void testGet() throws ClientProtocolException, IOException, URISyntaxException{

		CloseableHttpClient httpclient = HttpClients.createDefault();
		URIBuilder builder = new URIBuilder("http://localhost:8080/httpService/sendGetData?name=jection11");

		//builder.setParameter("name", "jection");
		builder.setParameter("city", "shenzheng11");
		
        HttpGet httpGet = new HttpGet(builder.build());
        
	      
        CloseableHttpResponse response2 = httpclient.execute(httpGet);
	      try {
	          System.out.println(response2.getStatusLine());
	          HttpEntity entity2 = response2.getEntity();

	          System.out.println(EntityUtils.toString(entity2));
	      } finally {
	          response2.close();
	      }
	      
	}
	
	//@Test
	public void test() throws ClientProtocolException, IOException{

		CloseableHttpClient httpclient = HttpClients.createDefault();
	      HttpPost httpPost = new HttpPost("http://localhost:8080/httpService/sendPostDataByJson");
	      List <NameValuePair> nvps = new ArrayList <NameValuePair>();
	      nvps.add(new BasicNameValuePair("name", "jection"));
	      nvps.add(new BasicNameValuePair("city", "shenzheng"));
	      httpPost.setEntity(new UrlEncodedFormEntity(nvps));
	      CloseableHttpResponse response2 = httpclient.execute(httpPost);
	      try {
	          System.out.println(response2.getStatusLine());
	          HttpEntity entity2 = response2.getEntity();

	          System.out.println(EntityUtils.toString(entity2));
	      } finally {
	          response2.close();
	      }
	      
	}
	

	//@Test
	public void test1() throws ClientProtocolException, IOException{
		Map<String, String> map = new HashMap<String, String>();
        map.put("name", "wyj");
        map.put("city", "南京");
        String json = JSON.toJSONString(map);
        
		CloseableHttpClient httpclient = HttpClients.createDefault();
	      HttpPost httpPost = new HttpPost("http://localhost:8080/httpService/sendPostDataByJson");
	      // 设置参数到请求对象中
	        StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
	        stringEntity.setContentEncoding("utf-8");
	        httpPost.setEntity(stringEntity);
	        CloseableHttpResponse response = httpclient.execute(httpPost);

	        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	        	System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
	        }

	        response.close();
	      
		
	}
}
