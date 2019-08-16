package com.ecar.apm.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ecar.apm.context.BeanProvider;
import com.ecar.apm.service.HttpRequestService;


@DisallowConcurrentExecution
public class HttpMonitoringJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpMonitoringJob.class);
    public static final String APPLICATION_INSTANCE_GUID = "instanceGuid";

    private transient HttpRequestService instanceService = BeanProvider.getBean(HttpRequestService.class);

    public HttpMonitoringJob() {
    	
    }

    /*
    * 每次的监控会将 以下代码执行一次
    * */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        final JobKey key = context.getJobDetail().getKey();
        LOGGER.debug("*****  Start execute Job [{}]", key);

        final String guid = context.getMergedJobDataMap().getString(APPLICATION_INSTANCE_GUID);
        instanceService.executeRequest(guid);

        LOGGER.debug("&&&&&  End execute Job [{}]", key);
    }
}