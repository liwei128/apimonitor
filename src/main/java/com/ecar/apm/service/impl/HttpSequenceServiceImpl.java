package com.ecar.apm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecar.apm.dao.HttpSequenceLogMapper;
import com.ecar.apm.dao.HttpSequenceMapper;
import com.ecar.apm.model.HttpSequence;
import com.ecar.apm.model.HttpSequenceLog;
import com.ecar.apm.model.HttpSystem;
import com.ecar.apm.model.MonitorFrequency;
import com.ecar.apm.service.HttpSequenceService;
import com.ecar.apm.util.GuidGenerator;
import com.ecar.apm.util.MathUtil;
import com.github.pagehelper.StringUtil;

@Service
public class HttpSequenceServiceImpl implements HttpSequenceService{

	@Autowired
	private HttpSequenceMapper httpSequenceMapper;
	@Autowired
	private HttpSequenceLogMapper httpSequenceLogMapper;
	
	@Override
	public HttpSequence getByGuid(String guid){
		return httpSequenceMapper.getByGuid(guid);
	}
	
	@Override
	public void archived(String guid){
		httpSequenceMapper.archived(guid);
	}
	
	@Override
	public void updateEnabled(HttpSequence httpSequence){
		httpSequenceMapper.updateEnabled(httpSequence);
	}
	
	@Override
	public void insert(HttpSequence httpSequence){
		if(StringUtil.isEmpty(httpSequence.getGuid())){
			httpSequence.setGuid(GuidGenerator.generate());
		}
		httpSequenceMapper.insert(httpSequence);
	}
	@Override
	public void update(HttpSequence httpSequence){
		httpSequenceMapper.update(httpSequence);
	}
	
	@Override
	public List<Map<String, Object>> getMonitorList(){
		List<Map<String, Object>> list = httpSequenceMapper.selectMonitorList();
		for(Map<String,Object> item : list){
			item.put("frequency", MonitorFrequency.valueOf(item.get("frequency").toString()).getLabel());
			item.put("type", HttpSequence.getMonitorTypeName(String.valueOf(item.get("type"))));
			String guid = String.valueOf(item.get("guid"));
			//启动监控
			if((boolean)item.get("enabled")){
				//平均响应时间
				Object avgCostTime = httpSequenceLogMapper.selectAvgCostTimeByPguid(guid);
				item.put("avgCostTime", avgCostTime);
				//最近一次请求状态
				Object recentStatus = httpSequenceLogMapper.selectRecentStatusByPguid(guid);
				if(recentStatus == null){
					item.put("status", "未监控");
					item.put("textColor", "text-muted");
				}else{
					item.put("status", (boolean)recentStatus == true ? "正常" : "故障");
					item.put("textColor", (boolean)recentStatus == true ? "text-green" : "text-red");
				}
				//可用率
				List<Map<String, Object>> usabilityList = httpSequenceLogMapper.selectUsabilityByPguid(guid);
				long count = 0;
				long uCount = 0;
				for(Map<String,Object> u : usabilityList){
					count += (long)u.get("count");
					if((boolean)u.get("status")){
						uCount+=(long)u.get("count");
					}
				}
				item.put("usability", MathUtil.percent(uCount, count));
				
			}else{
				//未启动
				item.put("status", "未监控");
				item.put("textColor", "text-muted");
				item.put("usability", "0%");
				item.put("avgCostTime", "0");
			}
		}
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getLogByGuid(String guid){
		
		List<Map<String, Object>> list = httpSequenceLogMapper.selectLogByPguid(guid);

		for(Map<String,Object> item : list){
			
		}
		return list;
		
	}



	@Override
	public boolean addHttpSystem(String group){
		HttpSystem httpSystem = new HttpSystem();
		httpSystem.setName(group);
		try{
			httpSequenceMapper.insertSystem(httpSystem);
			return true;
		}catch(Exception e){
			return false;
		}
		
	}

	@Override
	public List<HttpSystem> getAllSystem(){
		return httpSequenceMapper.selectAllSystem();
		
	}
	
	@Override
	public void insertLog(HttpSequenceLog httpSequenceLog){
		httpSequenceLogMapper.insert(httpSequenceLog);
	}
	
	@Override
	public void deleteLog(String pguid){
		httpSequenceLogMapper.delete(pguid);
	}

	@Override
	public void cleanLog(int day){
		httpSequenceLogMapper.cleanLogByDay(day);
		
	}
}
