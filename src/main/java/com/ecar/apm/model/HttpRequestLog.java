package com.ecar.apm.model;

import java.util.Date;

public class HttpRequestLog {
	

	private int id;
	
	private int pid;
	
	private String ppguid;
	
	private String pguid;

	private boolean status;//请求状态: false-失败,true-成功
	
    private long costTime;//响应时间

	private String statusCode;//响应状态码：200-成功
	
    private String responseBody;//响应体
    
    private String log;

    private Date createTime;
    
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getPpguid() {
		return ppguid;
	}

	public void setPpguid(String ppguid) {
		this.ppguid = ppguid;
	}

	public String getPguid() {
		return pguid;
	}

	public void setPguid(String pguid) {
		this.pguid = pguid;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public long getCostTime() {
		return costTime;
	}

	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}


	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    @Override
    public String toString() {
        return "{" +
                "status=" + status +
                ", costTime=" + costTime +
                ", statusCode='" + statusCode + '\'' +
                ", responseBody='" + responseBody + '\'' +
                ", log='" + log  + '\'' +
                '}';
    }
    
    
}
