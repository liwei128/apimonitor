package com.ecar.apm.service;

import java.util.List;
import java.util.Map;

import com.ecar.apm.model.HttpSequence;
import com.ecar.apm.model.HttpSequenceLog;
import com.ecar.apm.model.HttpSystem;

public interface HttpSequenceService {
	public HttpSequence getByGuid(String guid);

	public void archived(String guid);
	public void updateEnabled(HttpSequence httpSequence);
	public void insert(HttpSequence httpSequence);
	public void update(HttpSequence httpSequence);
	public List<Map<String, Object>> getMonitorList();
	public List<Map<String, Object>> getLogByGuid(String guid);
	public boolean addHttpSystem(String group);
	public List<HttpSystem> getAllSystem();
	
	public void insertLog(HttpSequenceLog httpSequenceLog);
	public void deleteLog(String pguid);
	
	public void cleanLog(int day);
}
