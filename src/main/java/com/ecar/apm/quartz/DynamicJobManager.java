package com.ecar.apm.quartz;


import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ecar.apm.context.BeanProvider;
import com.ecar.apm.job.HttpMonitoringJob;
import com.ecar.apm.model.HttpSequence;
import com.ecar.apm.service.HttpRequestService;

/**
 * @author Shengzhao Li
 */
public class DynamicJobManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicJobManager.class);

    private static final String MONITORING_INSTANCE_JOB_NAME_PREFIX = "monitoring-instance-";


    public static String generateMonitoringInstanceJobName(String key) {
        return MONITORING_INSTANCE_JOB_NAME_PREFIX + key;
    }

    
    
    private transient HttpRequestService httpRequestService = BeanProvider.getBean(HttpRequestService.class);

    
    private HttpSequence instance;
    
    public DynamicJobManager(HttpSequence instance) {
        this.instance = instance;
    }

    public boolean enable() {
        //final ApplicationInstance instance = instanceRepository.findByGuid(guid, ApplicationInstance.class);
        if (instance.isEnabled()) {
            LOGGER.debug("<{}> Instance[guid={}] already enabled, ignore it", username(), instance.getGuid());
            return false;
        }

        final boolean addSuccessful = startupMonitoringJob(instance);
        if (!addSuccessful) {
            LOGGER.debug("<{}> NOTE: Add MonitoringJob[jobName={}] failed", username(), instance.getJobName());
            return false;
        }

        //update
        instance.setEnabled(true);
        httpRequestService.updateEnabled(instance);
        LOGGER.debug("<{}> Update ApplicationInstance[guid={}] enabled=true,jobName={}", username(), instance.getGuid(), instance.getJobName());

        return true;
    }

    private boolean startupMonitoringJob(HttpSequence instance) {
        final String jobName = getAndSetJobName(instance);

        DynamicJob job = new DynamicJob(jobName)
                .cronExpression(instance.getFrequency().getCronExpression())
                .target(HttpMonitoringJob.class)
                .addJobData(HttpMonitoringJob.APPLICATION_INSTANCE_GUID, instance.getGuid());

        return executeStartup(instance, job);
    }

    private boolean executeStartup(HttpSequence instance, DynamicJob job) {
        boolean result = false;
        try {
            if (DynamicSchedulerFactory.existJob(job)) {
                result = DynamicSchedulerFactory.resumeJob(job);
                LOGGER.debug("<{}> Resume  [{}] by ApplicationInstance[guid={},instanceName={}] result: {}", username(), job, instance.getGuid(), instance.getName(), result);
            } else {
                result = DynamicSchedulerFactory.registerJob(job);
                LOGGER.debug("<{}> Register  [{}] by ApplicationInstance[guid={},instanceName={}] result: {}", username(), job, instance.getGuid(), instance.getName(), result);
            }
        } catch (SchedulerException e) {
            LOGGER.error("<{}> Register [" + job + "] failed", username(), e);
        }
        return result;
    }

    private String getAndSetJobName(HttpSequence instance) {
        String jobName = instance.getJobName();
        if (StringUtils.isEmpty(jobName)) {
            jobName = generateMonitoringInstanceJobName(instance.getGuid());
            instance.setJobName(jobName);
        }
        return jobName;
    }

    private String username() {
    	return null;
        //return SecurityUtils.currentUsername();
    }
    

    public boolean delete() {
        if (instance.isEnabled()) {
            LOGGER.debug("<{}> Forbid delete enabled ApplicationInstance[guid={}]", username(), instance.getGuid());
            return false;
        }

        httpRequestService.deleteHttpLog(instance.getGuid());

        checkAndRemoveJob(instance);

        //logic delete
        instance.setArchived(true);
        httpRequestService.archivedHttpData(instance.getGuid());
        LOGGER.debug("<{}> Archive ApplicationInstance[guid={}] and FrequencyMonitorLogs,MonitoringReminderLogs", username(), instance.getGuid());
        return true;
    }

    private void checkAndRemoveJob(HttpSequence instance) {
        DynamicJob job = new DynamicJob(getAndSetJobName(instance));
        try {
            if (DynamicSchedulerFactory.existJob(job)) {
                final boolean result = DynamicSchedulerFactory.removeJob(job);
                LOGGER.debug("<{}> Remove DynamicJob[{}] result [{}]", username(), job, result);
            }
        } catch (SchedulerException e) {
            LOGGER.error("<{}> Remove [" + job + "] failed", username(), e);
        }
    }

    
    /* * 1. Remove the job
     * 2. update instance to enabled=false
     **/
     
    public boolean kill() {
        if (!instance.isEnabled()) {
            LOGGER.debug("<{}> Expect ApplicationInstance[guid={}] enabled=true,but it is false, illegal status",
            		username(), instance.getGuid());
            return false;
        }

        if (!pauseJob(instance)) {
            LOGGER.debug("<{}> Pause Job[name={}] failed", username(), instance.getJobName());
            return false;
        }

        //update
        instance.setEnabled(false);
        httpRequestService.updateEnabled(instance);
        LOGGER.debug("<{}> Update ApplicationInstance[guid={}] enabled=false", username(), instance.getGuid());
        return true;
    }

    private boolean pauseJob(HttpSequence instance) {
        DynamicJob job = new DynamicJob(getAndSetJobName(instance));
        try {
            return DynamicSchedulerFactory.pauseJob(job);
        } catch (SchedulerException e) {
            LOGGER.error("<{}> Pause [" + job + "] failed", username(), e);
            return false;
        }
    }
}