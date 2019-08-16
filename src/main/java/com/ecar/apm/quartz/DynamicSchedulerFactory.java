package com.ecar.apm.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * @author Shengzhao Li
 */
public final class DynamicSchedulerFactory implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicSchedulerFactory.class);
    private static Scheduler scheduler;

    public DynamicSchedulerFactory() {
    }


    /**
     * 注册一个job
     * 1.检查是否有相同的 scheduler(根据 trigger key) ,若有则抛出异常
     * 2.调用 {@link #scheduler} 的 scheduleJob 加入
     *
     * @param job DynamicJob
     * @return True is register successful
     * @throws org.quartz.SchedulerException
     */
    public static boolean registerJob(DynamicJob job) throws SchedulerException {
        final TriggerKey triggerKey = job.triggerKey();
        if (scheduler.checkExists(triggerKey)) {
            final Trigger trigger = scheduler.getTrigger(triggerKey);
            throw new SchedulerException("Already exist trigger [" + trigger + "] by key [" + triggerKey + "] in Scheduler");
        }

        final CronTrigger cronTrigger = job.cronTrigger();

        final JobDetail jobDetail = job.jobDetail();
        final Date date = scheduler.scheduleJob(jobDetail, cronTrigger);

        LOG.debug("Register DynamicJob {} on [{}]", job, date);
        return true;
    }

    /*
   * Check the job is exist or not
   * */
    public static boolean existJob(DynamicJob job) throws SchedulerException {
        final TriggerKey triggerKey = job.triggerKey();
        return scheduler.checkExists(triggerKey);
    }

    /*
    * Pause exist job
    * */
    public static boolean pauseJob(DynamicJob existJob) throws SchedulerException {
        final TriggerKey triggerKey = existJob.triggerKey();
        boolean result = false;
        if (scheduler.checkExists(triggerKey)) {
            scheduler.pauseTrigger(triggerKey);
            result = true;
            LOG.debug("Pause exist DynamicJob {}, triggerKey [{}] successful", existJob, triggerKey);
        } else {
            LOG.debug("Failed pause exist DynamicJob {}, because not fount triggerKey [{}]", existJob, triggerKey);
        }
        return result;
    }


    /*
    * Resume exist job
    * */
    public static boolean resumeJob(DynamicJob existJob) throws SchedulerException {
        final TriggerKey triggerKey = existJob.triggerKey();
        boolean result = false;
        if (scheduler.checkExists(triggerKey)) {
            final CronTrigger newTrigger = existJob.cronTrigger();
            final Date date = scheduler.rescheduleJob(triggerKey, newTrigger);

            result = true;
            LOG.debug("Resume exist DynamicJob {}, triggerKey [{}] on [{}] successful", existJob, triggerKey, date);
        } else {
            LOG.debug("Failed resume exist DynamicJob {}, because not fount triggerKey [{}]", existJob, triggerKey);
        }
        return result;
    }


    /**
     * 删除掉已经存在的 job
     *
     * @param existJob A  DynamicJob which exists in Scheduler
     * @return True is remove successful
     * @throws org.quartz.SchedulerException
     */
    public static boolean removeJob(DynamicJob existJob) throws SchedulerException {
        final TriggerKey triggerKey = existJob.triggerKey();
        boolean result = false;
        if (scheduler.checkExists(triggerKey)) {
            result = scheduler.unscheduleJob(triggerKey);
        }

        LOG.debug("Remove DynamicJob {} result [{}]", existJob, result);
        return result;
    }


    public void setScheduler(Scheduler scheduler) {
        DynamicSchedulerFactory.scheduler = scheduler;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(scheduler, "scheduler is null");
        LOG.info("Initial DynamicSchedulerFactory successful, scheduler instance: {}", scheduler);
    }


}