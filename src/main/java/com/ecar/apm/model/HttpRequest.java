package com.ecar.apm.model;

import java.util.Date;
import java.util.HashMap;

import com.ecar.apm.util.GuidGenerator;
import com.github.pagehelper.StringUtil;


public class HttpRequest {

    private static final long serialVersionUID = 1826152029135090793L;

	public static HttpRequest getHttpRequest(HttpRequestForm httpRequestForm){
		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setGuid(httpRequestForm.getGuid());
		httpRequest.setPguid(httpRequestForm.getPguid());
		httpRequest.setHttpMethod(httpRequestForm.getHttpMethod());
		httpRequest.setUrl(httpRequestForm.getUrl());
		httpRequest.setHeaders(httpRequestForm.getHeaders());
		httpRequest.setParameters(httpRequestForm.getParameters());
		httpRequest.setConditionType(httpRequestForm.getConditionType());
		httpRequest.setCondition(httpRequestForm.getCondition());
		httpRequest.setResultType(httpRequestForm.getResultType());
		httpRequest.setVariables(httpRequestForm.getVariables());
		httpRequest.setMaxConnectionSeconds(httpRequestForm.getMaxConnectionSeconds());
		return httpRequest;
	}

	public enum CheckCondition {
		CONTAINS, DOESNT_CONTAIN, STATUSCODE, DEFAULT
	}
	public enum HttpMethod {
		GET, HEAD, POST, PUT, DELETE
	}
	
	public enum ResultType {
		XML, JSON
	}    
	
	private int id;
	
	private String guid;

	private String pguid;
	
	private int sort = 1;//序号

    private String url;//地址

    private String remark;//备注

	private CheckCondition conditionType = CheckCondition.DEFAULT;//结果校验类型（包含，不包含，状态码，默认200）

	private String condition;//结果校验内容
	
	private ResultType resultType;//返回结果类型（xml，json）
	
	private HttpMethod httpMethod;//http方法（get,head,post,put,delete）

	private String headers;

	private String parameters;

	private String variables;
	
	private HashMap<String,String> headersMap = new HashMap<String,String>();//请求头部

	private HashMap<String,String> parametersMap = new HashMap<String,String>();//请求参数
	
	private HashMap<String,String> variablesMap = new HashMap<String,String>();//变量
	
    private int maxConnectionSeconds = 30;//最大超时时间

    private Date createTime;

    private boolean archived;//是否删除（0-有效，1-删除）
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getPguid() {
		return pguid;
	}

	public void setPguid(String pguid) {
		this.pguid = pguid;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public CheckCondition getConditionType() {
		return conditionType;
	}

	public void setConditionType(CheckCondition conditionType) {
		this.conditionType = conditionType;
	}

	
	public ResultType getResultType() {
		return resultType;
	}

	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
		this.headersMap = this.stringToMap(headers);
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
		this.parametersMap = this.stringToMap(parameters);
	}

	public String getVariables() {
		return variables;
	}

	public void setVariables(String variables) {
		this.variables = variables;
		this.variablesMap = this.stringToMap(variables);
	}

	public HashMap<String, String> getHeadersMap() {
		return headersMap;
	}

	public void setHeadersMap(HashMap<String, String> headers) {
		this.headersMap = headers;
		this.headers = this.mapToString(headersMap);
	}

	public HashMap<String, String> getParametersMap() {
		return parametersMap;
	}

	public void setParametersMap(HashMap<String, String> parameters) {
		this.parametersMap = parameters;
		this.parameters = this.mapToString(parametersMap);
	}

	
	public HashMap<String, String> getVariablesMap() {
		return variablesMap;
	}

	public void setVariablesMap(HashMap<String, String> variables) {
		this.variablesMap = variables;
		this.variables = this.mapToString(variablesMap);
	}

	public int getMaxConnectionSeconds() {
		return maxConnectionSeconds;
	}

	public void setMaxConnectionSeconds(int maxConnectionSeconds) {
		this.maxConnectionSeconds = maxConnectionSeconds;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}
	
	public String mapToString(HashMap<String, String> map){
		if(map == null || map.size() == 0)return null;
		StringBuffer sb = new StringBuffer();
		for (String key : map.keySet()) {
			if(sb.length()!=0)sb.append("\r\n");
			sb.append(key).append("::").append(map.get(key));
		}
		return sb.toString();
	}

	public HashMap<String, String> stringToMap(String text){
		if(StringUtil.isEmpty(text))return null;
		HashMap<String, String> map = new HashMap<String, String>();
		String[] strArray = text.split("\r\n");
		for(String str : strArray){
			String[] header = str.split("::");
			map.put(header[0], header[1]);
		}
		return map;
	}
	
	
	
	
	
   
}