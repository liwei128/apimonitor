package com.ecar.apm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecar.apm.dao.HttpRequestLogMapper;
import com.ecar.apm.dao.HttpRequestMapper;
import com.ecar.apm.http.client.HttpSequenceHandle;
import com.ecar.apm.model.HttpRequest;
import com.ecar.apm.model.HttpRequestLog;
import com.ecar.apm.model.HttpSequence;
import com.ecar.apm.quartz.DynamicJobManager;
import com.ecar.apm.service.HttpRequestService;
import com.ecar.apm.service.HttpSequenceService;
import com.ecar.apm.util.GuidGenerator;
import com.github.pagehelper.StringUtil;

@Service
public class HttpRequestServiceImpl implements HttpRequestService {



	@Autowired
	private HttpRequestMapper httpRequestMapper;
	

	@Autowired
	private HttpSequenceService httpSequenceService;

	@Autowired
	private HttpRequestLogMapper httpRequestLogMapper;
	
	
	
    @Transactional
    @Override
    public boolean enableMonitor(String guid) {
    	HttpSequence instance = httpSequenceService.getByGuid(guid);
    	DynamicJobManager dynamicJobManager = new DynamicJobManager(instance);
        return dynamicJobManager.enable();
    }

    @Transactional
    @Override
    public boolean disableMonitor(String guid) {
    	HttpSequence instance = httpSequenceService.getByGuid(guid);
    	DynamicJobManager dynamicJobManager = new DynamicJobManager(instance);
        return dynamicJobManager.kill();
    }
    

    @Transactional
    @Override
    public boolean deleteMonitor(String guid) {
    	HttpSequence instance = httpSequenceService.getByGuid(guid);
    	DynamicJobManager dynamicJobManager = new DynamicJobManager(instance);
        return dynamicJobManager.delete();
    }
    
    
    
    @Transactional
    @Override
    public void executeRequest(String guid) {
    	HttpSequence instance = httpSequenceService.getByGuid(guid);
    	if(instance==null){
    		//删除任务
    		return;
    	}
    	List<HttpRequest> list = this.getHttpRequestListByPguid(guid);
    	instance.setHttpRequest(list);
    	
    	//执行接口探测
    	HttpSequenceHandle httpSequenceHandle = new HttpSequenceHandle(instance);
    	httpSequenceHandle.execute();
    	httpSequenceService.insertLog(httpSequenceHandle.httpSequenceLog);
    	
    	//保存日志
    	for(HttpRequestLog httpRequestLog : httpSequenceHandle.httpRequestLogList){
    		httpRequestLog.setPid(httpSequenceHandle.httpSequenceLog.getId());
    		httpRequestLogMapper.insert(httpRequestLog);
    	}
    }
    
    

    @Transactional
    @Override
	public void archivedHttpData(String guid){
    	httpSequenceService.archived(guid);
		httpRequestMapper.archived(guid);
	}
    

    @Transactional
    @Override
	public void deleteHttpLog(String guid){
    	httpSequenceService.deleteLog(guid);
		httpRequestLogMapper.delete(guid);
	}
    

    @Transactional
    @Override
    public void cleanMonitorLogs(int day) {
		httpRequestLogMapper.cleanLogByDay(day);
		httpSequenceService.cleanLog(day);
    }
    
    
	@Override
	public void updateEnabled(HttpSequence httpSequence){
		httpSequenceService.updateEnabled(httpSequence);
	}
	

	
	
	@Override
	public List<HttpRequest> getAllHttpRequest(){
		return httpRequestMapper.selectAll();
	}
	
	@Override
	public HttpRequest getHttpRequestByGuid(String guid){
		return httpRequestMapper.getByGuid(guid);
	}
	

	@Override
	public List<HttpRequest> getHttpRequestListByPguid(String pguid){
		return httpRequestMapper.getListByPguid(pguid);
	}
	

	@Override
	public void insertHttpRequest(HttpRequest httpRequest){
		if(StringUtil.isEmpty(httpRequest.getGuid())){
			httpRequest.setGuid(GuidGenerator.generate());
		}
		httpRequestMapper.insert(httpRequest);
	}

	@Override
	public void updateHttpRequest(HttpRequest httpRequest){
		httpRequestMapper.update(httpRequest);
	}

	
	@Override
	public List<Map<String, Object>> getHttpRequestLogByPid(String id){
		List<Map<String, Object>> list = httpRequestLogMapper.getByPid(Integer.parseInt(id));
		return list;
		
	}
}
