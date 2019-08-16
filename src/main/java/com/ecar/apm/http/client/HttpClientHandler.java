package com.ecar.apm.http.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONPath;
import com.ecar.apm.model.Application;
import com.ecar.apm.model.HttpRequest;
import com.ecar.apm.model.HttpRequest.HttpMethod;
import com.ecar.apm.model.HttpRequest.ResultType;
import com.ecar.apm.model.HttpRequestLog;
import com.ecar.apm.util.XmlUtil;
import com.github.pagehelper.StringUtil;
import com.google.common.xml.XmlEscapers;


public class HttpClientHandler {

	protected static final Logger LOGGER = LoggerFactory.getLogger(HttpClientHandler.class);
	// Convert mill seconds to second unit
	protected static final int MS_TO_S_UNIT = 1000;
	// Normal http response code
	protected static final String NORMAL_RESPONSE_CODE = "200";
	// https prefix
	protected static final String HTTPS = "https";

	protected static HttpsTrustManager httpsTrustManager = new HttpsTrustManager();

	private HttpSequenceHandle httpSequenceHandle;
	
	private HttpEntity httpEntity;

	private HttpRequest httpRequest;
	
	
	protected String output;
	

	
	
	public HttpClientHandler(HttpRequest httpRequest, HttpSequenceHandle httpSequenceHandle) {
		this.httpRequest = httpRequest;
		this.httpSequenceHandle = httpSequenceHandle;
	}

	public void httpEntity(HttpEntity httpEntity) {
		this.httpEntity = httpEntity;
	}

	/*
	 * Convert response as string
	 */
	protected String responseAsString(CloseableHttpResponse response) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			response.getEntity().writeTo(baos);
			return new String(baos.toByteArray(), Application.ENCODING);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	
	public HttpRequestLog execute(){

		long start = System.currentTimeMillis();
		String statusCode = null;
		String body = null;
		try{
			CloseableHttpResponse response = sendRequest();
			statusCode = String.valueOf(getStatusCode(response));
			body = getResponseBody(response);
			response.close();
			validResponse(body, statusCode);
			handleVariables(body);
		}catch(Exception e){
			appendMessage(e.toString());
			LOGGER.error("Send request to url[" + httpRequest.getUrl() + "] failed", e);
		}

		HttpRequestLog requestLog = new HttpRequestLog();
		requestLog.setCostTime(System.currentTimeMillis() - start);
		requestLog.setStatus(StringUtil.isEmpty(output));
		requestLog.setStatusCode(statusCode);
		requestLog.setResponseBody(body);
		requestLog.setLog(output);
		return requestLog;
	}
	

	protected CloseableHttpResponse sendRequest() throws ClientProtocolException, IOException {
		RequestBuilder builder = createRequestBuilder();
		addRequestParams(builder);
		setHttpEntity(builder);
		HttpUriRequest request = builder.setUri(httpRequest.getUrl()).build();
		setHeaders(request);
		CloseableHttpClient client = createHttpClient();
		return client.execute(request);
	}

	protected String getResponseBody(CloseableHttpResponse httpResponse) throws ParseException, IOException{
		if (httpResponse == null)return null;
		HttpEntity entity = httpResponse.getEntity();
		if(entity == null)return null;
		String webPage = EntityUtils.toString(entity,"UTF-8");
		return webPage;
	}
	
	protected int getStatusCode(CloseableHttpResponse httpResponse){
		int status = httpResponse.getStatusLine().getStatusCode();
		return status;
	}
	
	protected void validResponse(String body, String statusCode) throws Exception {
		switch (httpRequest.getConditionType()) {
		case CONTAINS:
			if (StringUtil.isEmpty(body) || !body.contains(httpRequest.getCondition())) {
				appendMessage(httpRequest.getUrl() + " doesn't contain "
						+ XmlEscapers.xmlContentEscaper().escape(httpRequest.getCondition()));
			}
			break;
		case DOESNT_CONTAIN:
			if (StringUtil.isEmpty(body) || body.contains(httpRequest.getCondition())) {
				appendMessage(httpRequest.getUrl() + " contains "
						+ XmlEscapers.xmlContentEscaper().escape(httpRequest.getCondition()));
			}
			break;
		case STATUSCODE:
			if (!statusCode.equals(httpRequest.getCondition())) {
				appendMessage("Invalid status: " + httpRequest.getUrl() + " required: " + httpRequest.getCondition() + ", received: " + statusCode);
			}
			break;
		default:
			if (!"200".equals(statusCode)) {
				appendMessage("Invalid status: " + httpRequest.getUrl() + " required: " + 200 + ", received: " + statusCode);
			}
		break;
		}
		
	}

	protected void handleVariables(String body) throws Exception {
		if(StringUtil.isEmpty(body))return;
		HashMap<String, String> variables = httpRequest.getVariablesMap();
		ResultType type = httpRequest.getResultType();
		if(variables == null || type == null)return;
		if(type.equals(ResultType.JSON)){
			for (String variableName : variables.keySet()) {
				String variablePath = variables.get(variableName);
				Object variableValue = JSONPath.read(body, variablePath);
				this.httpSequenceHandle.setVariables(variableName, variableValue.toString());
			}
			
		}else if(type.equals(ResultType.XML)){
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc = builder.parse(IOUtils.toInputStream(XmlUtil.replaceChar(body), "UTF-8"));
			XPathFactory xPathfactory = XPathFactory.newInstance();
			for (String variableName : variables.keySet()) {
				String variablePath = variables.get(variableName);

				XPath xpath = xPathfactory.newXPath();
				XPathExpression expr = xpath.compile(variablePath);
				// TODO Retrieve whole XML fragment
				String variableValue = expr.evaluate(doc);
				this.httpSequenceHandle.setVariables(variableName, variableValue);
			}
		}
		
	}
	
	
	public String getOutput() {
		return output;
	}

	protected void appendMessage(String message) {
		if (output == null) {
			output = "";
		}
		if (message != null && !message.trim().isEmpty()) {
			output += message;
		}
	}
	
	protected void addRequestParams(RequestBuilder builder) {
		HashMap<String, String> map = httpRequest.getParametersMap();
		if(map==null || map.size()==0)return;
		for (String key : map.keySet()) {
			String val = map.get(key);
			builder.addParameter(key, useVariable(val));
		}
	}
	
	protected String useVariable(String val){
		if(!val.startsWith("$$")){
			return val;
		}else{
			String result = this.httpSequenceHandle.getVariables(val);
			return result == null ? val : result;
		}
	}

	
	protected void setHttpEntity(RequestBuilder builder) {
		if (this.httpEntity != null) {
			builder.setEntity(this.httpEntity);
		}
	}

	protected void setHeaders(HttpUriRequest request) {
		HashMap<String, String> map = httpRequest.getHeadersMap();
		if(map==null || map.size()==0)return;
		for (String key : map.keySet()) {
			request.addHeader(key, map.get(key));
		}
	}

	protected CloseableHttpClient createHttpClient() {
		final RequestConfig requestConfig = requestConfig();
		HttpClientBuilder httpClientBuilder;
		if (isHttps()) {
			// Support SSL
			SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(createSSLContext());
			httpClientBuilder = HttpClients.custom().setDefaultRequestConfig(requestConfig)
					.setSSLSocketFactory(sslConnectionSocketFactory);
		} else {
			httpClientBuilder = HttpClients.custom().setDefaultRequestConfig(requestConfig);
		}
		if(httpSequenceHandle!=null){
			httpClientBuilder.setDefaultCookieStore(httpSequenceHandle.cookieStore);
		}
		return httpClientBuilder.build();
	}

	private RequestConfig requestConfig() {
		final int maxConnMillSeconds = httpRequest.getMaxConnectionSeconds() * MS_TO_S_UNIT;
		return RequestConfig.custom().setSocketTimeout(maxConnMillSeconds).setConnectTimeout(maxConnMillSeconds).build();
	}

	private SSLContext createSSLContext() {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new HttpsTrustManager[] { httpsTrustManager }, null);
			return sslContext;
		} catch (Exception e) {
			throw new IllegalStateException("Create SSLContext error", e);
		}
	}

	protected boolean isHttps() {
		return httpRequest.getUrl().toLowerCase().startsWith(HTTPS);
	}

	protected RequestBuilder createRequestBuilder() {
		if (httpRequest.getHttpMethod().equals(HttpMethod.GET)) {
			return RequestBuilder.get();
		} else if (httpRequest.getHttpMethod().equals(HttpMethod.POST)) {
			return RequestBuilder.post();
		} else if (httpRequest.getHttpMethod().equals(HttpMethod.HEAD)) {
			return RequestBuilder.head();
		} else if (httpRequest.getHttpMethod().equals(HttpMethod.PUT)) {
			return RequestBuilder.put();
		} else if (httpRequest.getHttpMethod().equals(HttpMethod.DELETE)) {
			return RequestBuilder.delete();
		} else {
			return null;
		}

	}

	/**
	 * Default X509TrustManager implement
	 */
	private static class HttpsTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
			// ignore
		}

		@Override
		public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
			// ignore
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
}