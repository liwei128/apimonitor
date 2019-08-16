package com.ecar.apm.service;

import java.util.List;
import java.util.Map;

import com.ecar.apm.model.HttpRequest;
import com.ecar.apm.model.HttpSequence;

public interface HttpRequestService {

	boolean enableMonitor(String guid);

    boolean deleteMonitor(String guid);
    
    boolean disableMonitor(String guid);
    

    void executeRequest(String guid);
    
    void archivedHttpData(String guid);
    
    void deleteHttpLog(String guid);
    
    public void cleanMonitorLogs(int day);
    
	public List<HttpRequest> getAllHttpRequest();
	
	public HttpRequest getHttpRequestByGuid(String guid);
	
	public List<HttpRequest> getHttpRequestListByPguid(String pguid);

	public void insertHttpRequest(HttpRequest httpRequest);
	public void updateHttpRequest(HttpRequest httpRequest);
	public void updateEnabled(HttpSequence httpSequence);
	

	public List<Map<String, Object>> getHttpRequestLogByPid(String id);
}