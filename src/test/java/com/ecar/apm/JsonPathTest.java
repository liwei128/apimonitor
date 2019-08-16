package com.ecar.apm;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.springframework.util.ResourceUtils;

import com.alibaba.fastjson.JSONPath;

public class JsonPathTest {

	public static void main(String[] args) throws Exception {
		InputStream is = new FileInputStream(ResourceUtils.getFile("classpath:testfile/store.json"));
	    String body = IOUtils.toString(is,"utf8");
	    Object value = JSONPath.read(body, "$.store.book[title='Sword of Honour'].author");
		//Object value = JsonPath.read(body, "$..book[?(@.title=='Sword of Honour')].author");
		System.out.println(value);
	}
	
	public static void test1() throws IOException{
		// TODO Auto-generated method stub
				HashMap<String,String> variableResult = new HashMap<String,String>();
				HashMap<String, String> variables = new HashMap<String, String>();
				variables.put("$a", "$.requests[1].id");
				variables.put("$b", "$.order");
				variables.put("$c", "$..id");
				
				
				InputStream is = new FileInputStream(ResourceUtils.getFile("classpath:testCase.postman_collection1.json"));
			    String body = IOUtils.toString(is,"utf8");
				
				for (String variableName : variables.keySet()) {
					String variablePath = variables.get(variableName);
					Object variableValue = JSONPath.read(body, variablePath);
					variableResult.put(variableName, variableValue.toString());
				}
				for (String name : variableResult.keySet()) {
					System.out.println(name+ ":"+  variableResult.get(name));
				}
	}

}
