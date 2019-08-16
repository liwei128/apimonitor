package com.ecar.apm.http.demo;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
 
public class PostForm {
	
	
	
	/*
	 * 自动填单
	 */
	public String hcPost() throws ClientProtocolException, IOException, URISyntaxException  {
		
		BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();
        try {
            HttpUriRequest post = RequestBuilder.post()
                    .setUri(new URI("http://localhost:8080/httpService/sendPost?a=1&b=2"))
                    .addParameter("a", "jection")
                    .addParameter("b", "shenzheng")
                    .build();
            CloseableHttpResponse response2 = httpclient.execute(post);
            try {
                HttpEntity entity = response2.getEntity();
 
                System.out.println(" form status: " + response2.getStatusLine());
                System.out.println(" form result: " + EntityUtils.toString(entity));
                
                System.out.println("Post logon cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("- " + cookies.get(i).toString());
                    }
                }
            } finally {
                response2.close();
            }
        } finally {
            httpclient.close();
        }
        return "yes";
	}

	public String hcGet() throws ClientProtocolException, IOException, URISyntaxException  {
		
		BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();
        try {
            HttpUriRequest get = RequestBuilder.get()
                    .setUri(new URI("http://localhost:8080/httpService/sendGetData"))
                    .addParameter("name", "jection")
                    .addParameter("city", "shenzheng")
                    .build();
            CloseableHttpResponse response2 = httpclient.execute(get);
            try {
                HttpEntity entity = response2.getEntity();
 
                System.out.println(" form status: " + response2.getStatusLine());
                System.out.println(" form result: " + EntityUtils.toString(entity));
                
                System.out.println("Post logon cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("- " + cookies.get(i).toString());
                    }
                }
            } finally {
                response2.close();
            }
        } finally {
            httpclient.close();
        }
        return "yes";
	}
	public static void main(String[]args) throws Exception{
		
		PostForm pf=new PostForm();	
        
        String ret=pf.hcGet();
        
        System.out.println("********"+ret);  
  
	}
}
