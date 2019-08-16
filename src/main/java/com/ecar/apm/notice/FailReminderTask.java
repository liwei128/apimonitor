package com.ecar.apm.notice;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ecar.apm.dao.HttpSequenceLogMapper;
import com.ecar.apm.dao.HttpSequenceMapper;
import com.ecar.apm.model.HttpSequence;
import com.ecar.apm.model.HttpSequenceLog;
import com.google.common.collect.Maps;


/**
 * 日志邮件提醒任务
 * @author 01383518
 * @date:   2019年4月3日 下午7:39:22
 */
@Component
public class FailReminderTask {
	
	@Autowired
	private HttpSequenceMapper httpSequenceMapper;
	@Autowired
	private HttpSequenceLogMapper httpSequenceLogMapper;
	@Autowired
	private MailSendService mailSendService;
	
	public Map<String,String> failId = Maps.newConcurrentMap(); 
	
	
	/**
	 * 异常日志邮件提醒，每30秒一次
	 */
	@Scheduled(cron = "3/30 * * * * ?")
	public void mailReminder() {
		List<HttpSequence> list = httpSequenceMapper.selectEnabledMonitorList();
		list.forEach(m->{
			HttpSequenceLog logs = httpSequenceLogMapper.selectLastLogByPguid(m.getGuid());
			if(logs==null){
				return ;
			}
			//成功
			if(logs.isStatus()){
				if(failId.get(m.getGuid())!=null){
				    sendMessage(m,logs,true);
				}
				failId.remove(m.getGuid());
				return;
			}
			//失败
			if(failId.get(m.getGuid())==null){
				failId.put(m.getGuid(), logs.getLog());
				sendMessage(m,logs,false);
			}
			
		});
	}


	private void sendMessage(HttpSequence m, HttpSequenceLog logs,boolean success) {
		String msg = builderMsg(m,logs,success);
		String remark = m.getRemark();
		if(StringUtils.isNotEmpty(remark)){
			String[] mails = remark.split(",");
			mailSendService.sendMail(mails, "服务可用性监控", msg);
		}
	}


	private String builderMsg(HttpSequence m, HttpSequenceLog logs,boolean success) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = simpleDateFormat.format(logs.getCreateTime());
		if(success){
			return date+"  服务【"+m.getName()+"】已恢复正常";
		}
		return date+"  服务【"+m.getName()+"】异常,详细信息："+logs.getLog();
	}


}
