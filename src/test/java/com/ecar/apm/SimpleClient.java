package com.ecar.apm;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/** 
 *最简单的HTTP客户端,用来演示通过GET或者POST方式访问某个页面
  *@authorLiudong
*/
public class SimpleClient {
public static void main(String[] args) throws IOException 
{


	
	CloseableHttpClient httpclient = HttpClients.createDefault();
	HttpGet httpGet = new HttpGet("http://java.sun.com");
	CloseableHttpResponse response1 = httpclient.execute(httpGet);
	
      //使用POST方法
      //HttpMethod method = new PostMethod("http://java.sun.com");
	try {
      //打印服务器返回的状态
      System.out.println(response1.getStatusLine());
      HttpEntity entity = response1.getEntity();
      String webPage = EntityUtils.toString(entity);
      System.out.println(webPage);
      
	
	} finally {
	    response1.close();
	}
   }
}