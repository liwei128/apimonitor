package com.ecar.apm.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.springframework.util.ResourceUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.ecar.apm.model.HttpRequest;
import com.ecar.apm.model.HttpSequence;
import com.ecar.apm.model.HttpRequest.HttpMethod;
import com.github.pagehelper.StringUtil;

public class PostManResolver {

	public static void main(String[] args) throws Exception{
		String jsonText = readJsonFile("classpath:testfile/v2.1postman_collection.json");
		ArrayList<HttpRequest> list = PostManResolver.readV2FromJsonText(jsonText);
		for(HttpRequest request : list){
			System.out.println(request.getUrl());
		}
	}
	
    public static void readToBuffer(StringBuffer buffer, String filePath) throws IOException {
        //InputStream is = new FileInputStream(filePath);
        InputStream is = new FileInputStream(ResourceUtils.getFile(filePath));
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
    }
    
    
	
	public static String readJsonFile(String filepath) throws IOException{
		InputStream is = new FileInputStream(ResourceUtils.getFile(filepath));
	    String jsonText = IOUtils.toString(is,"utf8");
	    is.close();
	    return jsonText;
	}

	public static ArrayList<HttpRequest> readFromJsonFile(String filepath){
		try{
			String jsonText = readJsonFile(filepath);
			return readV1FromJsonText(jsonText);
		}catch(Exception e){
			return null;
		}
	}
	
	public static ArrayList<HttpRequest> readV1FromJsonText(String jsonText){
		ArrayList<String> httpOrderList = new ArrayList<String>();
		ArrayList<HttpRequest> httpRequestList = new ArrayList<HttpRequest>();
		//----------------------开始解析postman脚本------------------------
		JSONObject root = JSONObject.parseObject(jsonText);
		//order
		JSONArray order = root.getJSONArray("order");
		for(Object obj : order){
			httpOrderList.add(obj.toString());
		}
		
		//requests
		JSONArray requests = root.getJSONArray("requests");
		for(Object obj : requests){
			JSONObject request = (JSONObject) obj;
			HttpRequest httpRequest = new HttpRequest();
			httpRequest.setRemark(request.getString("id"));
			//httpRequest.setName(request.getString("name"));
			httpRequest.setUrl(request.getString("url"));
			httpRequest.setHttpMethod(HttpMethod.valueOf(request.getString("method").toUpperCase()));
			//header
			String headers = request.getString("headers");
			if(!StringUtil.isEmpty(headers)){
				HashMap<String, String> headerMap = new HashMap<String, String>();
	    		String[] headerArray = headers.split("\n");
	    		for(String str : headerArray){
	    			String[] header = str.split(": ");
	    			headerMap.put(header[0], header[1]);
	    		}
	    		httpRequest.setHeadersMap(headerMap);
			}
    		//data
    		String data = request.getString("data");
    		if(!StringUtil.isEmpty(data)){
    			JSONArray dataJa = JSONArray.parseArray(data);
        		HashMap<String, String> paramMap = new HashMap<String, String>();
    			for (Object o : dataJa) {
    		        JSONObject dataJo = (JSONObject) o;
    		        paramMap.put(dataJo.getString("key"), dataJo.getString("value"));
    		    }
    			httpRequest.setParametersMap(paramMap);
    		}
    		httpRequestList.add(httpRequest);
		}
		//------------------------------结束解析-----------------------------
		
		ArrayList<HttpRequest> resultList = new ArrayList<HttpRequest>();
        int sort = 1;
		for(String id : httpOrderList){
			for(HttpRequest httpRequest : httpRequestList){
				if(id.equals(httpRequest.getRemark())){
					httpRequest.setSort(sort);
					resultList.add(httpRequest);
					sort++;
				}
			}
		}
		return resultList;
		
	}

	public static ArrayList<HttpRequest> readV2FromJsonText(String jsonText){
		ArrayList<HttpRequest> httpRequestList = new ArrayList<HttpRequest>();
		//----------------------开始解析postman脚本------------------------
		JSONObject root = JSONObject.parseObject(jsonText);
		
		//requests
		JSONArray items = root.getJSONArray("item");
        int sort = 1;
		for(Object obj : items){
			JSONObject item = (JSONObject) obj;
			JSONObject request = item.getJSONObject("request");
			HttpRequest httpRequest = new HttpRequest();
			String url = request.getString("url");
			if(url.contains("\"raw\":")){
				httpRequest.setUrl(request.getJSONObject("url").getString("raw"));
			}else{
				httpRequest.setUrl(request.getString("url"));
			}
			
			
			httpRequest.setHttpMethod(HttpMethod.valueOf(request.getString("method").toUpperCase()));
			//header
			JSONArray headers = request.getJSONArray("header");
			if(!headers.isEmpty()){
				HashMap<String, String> headerMap = new HashMap<String, String>();
	    		for(Object obj1 : headers){
	    			JSONObject header = (JSONObject) obj1;
	    			headerMap.put(header.getString("key"), header.getString("value"));
	    		}
	    		httpRequest.setHeadersMap(headerMap);
			}
    		//data
    		String raw = request.getJSONObject("body").getString("raw");
    		if(!StringUtil.isEmpty(raw)){
        		HashMap<String, String> paramMap = new HashMap<String, String>();
    			String[] rawArray = raw.split("&");
	    		for(String str : rawArray){
	    			String[] param = str.split("=");
	    			paramMap.put(param[0], param[1]);
	    		}
    			httpRequest.setParametersMap(paramMap);
    		}
			httpRequest.setSort(sort);
    		httpRequestList.add(httpRequest);
			sort++;
		}
		//------------------------------结束解析-----------------------------
		return httpRequestList;
		
	}
	
	public static void readBigFile() throws FileNotFoundException{
		HttpSequence httpSequence = new HttpSequence();
		ArrayList<String> ids = new ArrayList<String>();
		ArrayList<HttpRequest> httpList = new ArrayList<HttpRequest>();
        JSONReader jsonReader = new JSONReader(new FileReader(ResourceUtils.getFile("classpath:testCase.postman_collection1.json")));
        jsonReader.startObject();//将整个json文件当作 Map<String,Object> 对象来解析 {,}
        while(jsonReader.hasNext()) {
            String key = jsonReader.readString();
            if(key.equals("order")){
            	jsonReader.startArray();//数组
                while(jsonReader.hasNext()) {
                    String id = jsonReader.readString();
                    ids.add(id);
                }
                jsonReader.endArray();
            }else if(key.equals("requests")){
            	jsonReader.startArray();//---> [  开启读List对象
                while(jsonReader.hasNext()) {
                	HttpRequest httpRequest = new HttpRequest();
                    jsonReader.startObject();
                    while(jsonReader.hasNext()) {
                        String objKey = jsonReader.readString();
                    	if(objKey.equals("id")){
                    		httpRequest.setRemark(jsonReader.readString());
                    	}else if(objKey.equals("url")){
                    		httpRequest.setUrl(jsonReader.readString());
                    		
                    	}else if(objKey.equals("method")){
                			httpRequest.setHttpMethod(HttpMethod.valueOf(jsonReader.readString().toUpperCase()));
                    		
                    	}else if(objKey.equals("headers")){
                    		HashMap<String, String> map = new HashMap<String, String>();
                    		String[] headers = jsonReader.readString().split("\n");
                    		for(String str : headers){
                    			String[] header = str.split(": ");
                    			map.put(header[0], header[1]);
                    		}
                    		httpRequest.setHeadersMap(map);
                    		
                    	}else if(objKey.equals("name")){
                    		//httpRequest.setName(jsonReader.readString());
                    		
                    	}else if(objKey.equals("data")){
                    		String data = jsonReader.readString();
                    		if(data!=null){
                    			JSONArray jsonArray = JSONArray.parseArray(data);
                        		HashMap<String, String> map = new HashMap<String, String>();
                    			for (Object obj : jsonArray) {
                    		        JSONObject jsonObject = (JSONObject) obj;
                    		        map.put(jsonObject.getString("key"), jsonObject.getString("value"));
                    		    }
                    			httpRequest.setParametersMap(map);
                    		}
                    	}else{
                            jsonReader.readObject();
                    	}
                    }
                    jsonReader.endObject();
                    httpList.add(httpRequest);
                }
                jsonReader.endArray();//---> ]
            }else{
            	jsonReader.readObject();
            }
            
        }
        jsonReader.endObject();
        jsonReader.close();

        ArrayList<HttpRequest> requests = new ArrayList<HttpRequest>();
        int sort = 1;
		for(String id : ids){
			for(HttpRequest httpRequest : httpList){
				if(id.equals(httpRequest.getRemark())){
					httpRequest.setSort(sort);
					requests.add(httpRequest);
					sort++;
				}
			}
		}
		httpSequence.setHttpRequest(requests);
		
	}
}
