package com.ecar.apm.model;

import java.util.Date;
import java.util.HashMap;

import com.ecar.apm.model.HttpRequest.CheckCondition;
import com.ecar.apm.model.HttpRequest.HttpMethod;
import com.ecar.apm.model.HttpRequest.ResultType;
import com.ecar.apm.model.HttpSequence.MonitorType;
import com.ecar.apm.util.GuidGenerator;
import com.github.pagehelper.StringUtil;


public class HttpRequestForm {
	
	public static HttpRequestForm getHttpRequestForm(HttpSequence s, HttpRequest r){
		HttpRequestForm form = new HttpRequestForm();
		form.setGuid(r.getGuid());
		form.setPguid(s.getGuid());
		
		form.setGroup(s.getGroup());
		form.setType(s.getType());
		form.setName(s.getName());
		form.setFrequency(s.getFrequency());
		form.setRemark(s.getRemark());
		
		form.setHttpMethod(r.getHttpMethod());
		form.setUrl(r.getUrl());
		form.setHeaders(r.getHeaders());
		form.setParameters(r.getParameters());
		form.setConditionType(r.getConditionType());
		form.setCondition(r.getCondition());
		form.setResultType(r.getResultType());
		form.setVariables(r.getVariables());
		form.setMaxConnectionSeconds(r.getMaxConnectionSeconds());
		return form;
	}
	
	private int id;
	
	private String guid;

	private String pguid;
	
	private String group;//所属组
	
	private MonitorType type;//类型：SINGLE-单个接口；SEQUENCE-多个接口
	
	private String name;

    private MonitorFrequency frequency = MonitorFrequency.THIRTY;//监控频率
    
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
	
    private int maxConnectionSeconds;//最大超时时间

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
	

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public MonitorType getType() {
		return type;
	}

	public void setType(MonitorType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getVariables() {
		return variables;
	}

	public void setVariables(String variables) {
		this.variables = variables;
	}

	public HashMap<String, String> getHeadersMap() {
		return headersMap;
	}

	public void setHeadersMap(HashMap<String, String> headers) {
		this.headersMap = headers;
	}

	public HashMap<String, String> getParametersMap() {
		return parametersMap;
	}

	public void setParametersMap(HashMap<String, String> parameters) {
		this.parametersMap = parameters;
	}

	
	public HashMap<String, String> getVariablesMap() {
		return variablesMap;
	}

	public void setVariablesMap(HashMap<String, String> variables) {
		this.variablesMap = variables;
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
	public MonitorFrequency getFrequency() {
		return frequency;
	}

	public void setFrequency(MonitorFrequency frequency) {
		this.frequency = frequency;
	}
	
	
	
	
	
   
}