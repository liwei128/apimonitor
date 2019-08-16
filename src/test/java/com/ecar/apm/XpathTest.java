package com.ecar.apm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.util.ResourceUtils;

public class XpathTest {
	public static void main(String[] args) throws Exception{

		readXmlByJavax();
	}
	
	public static void readXmlByDom4j() throws Exception{

		InputStream is = new FileInputStream(ResourceUtils.getFile("classpath:xpath.html"));
	    String xml = IOUtils.toString(is,"utf8");
		xml = xml.replaceAll("&nbsp;","&amp;nbsp;").replaceAll("&raquo;", "");
		
		Document doc = DocumentHelper.parseText(xml);
		
		//SAXReader saxReader = new SAXReader();    
       // Document doc = saxReader.read(ResourceUtils.getFile("classpath:xpath.html"));
        
		HashMap<String,String> variableResult = new HashMap<String,String>();
		HashMap<String, String> variables = new HashMap<String, String>();
		variables.put("$a", "//input[@name='_csrf']");
		variables.put("$b", "//meta[@name='X-CSRF-TOKEN']/@content");
		variables.put("$c", "/html/head/title");
		
        for (String variableName : variables.keySet()) {
			String variablePath = variables.get(variableName);
			Node node = doc.selectSingleNode(variablePath);
			
			String variableValue = null;
			if(node!=null){
				variableValue = node.getText();
			}
			variableResult.put(variableName, variableValue);
		}
		for (String name : variableResult.keySet()) {
			System.out.println(name+ ":"+  variableResult.get(name));
		}
	}
	
	public static void readXmlByJavax() throws Exception{
		HashMap<String,String> variableResult = new HashMap<String,String>();
		HashMap<String, String> variables = new HashMap<String, String>();
		variables.put("$a", "//input[@name='_csrf']");
		variables.put("$b", "//meta[@name='X-CSRF-TOKEN']/@content");
		variables.put("$c", "/html/head/title");
		
	
		InputStream is = new FileInputStream(ResourceUtils.getFile("classpath:xpath.html"));
	    String body = IOUtils.toString(is,"utf8");

	    body = body.replaceAll("&nbsp;","&amp;nbsp;").replaceAll("&raquo;", "");
	    
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		org.w3c.dom.Document doc = builder.parse(IOUtils.toInputStream(body, "UTF-8"));
		XPathFactory xPathfactory = XPathFactory.newInstance();
		for (String variableName : variables.keySet()) {
			String variablePath = variables.get(variableName);

			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(variablePath);
			// TODO Retrieve whole XML fragment
			String variableValue = expr.evaluate(doc);
			variableResult.put(variableName, variableValue);
		}
		
		for (String name : variableResult.keySet()) {
			System.out.println(name+ ":"+  variableResult.get(name));
		}
		
	}
}
