package com.ecar.apm.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ecar.apm.service.HttpRequestService;

@Component
public class AutoCleanMonitorLogJob implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(AutoCleanMonitorLogJob.class);

    @Autowired
    private transient HttpRequestService logService;

    public AutoCleanMonitorLogJob() {
    }

    @Value("${auto.clean.monitor.log.keep.day}") 
    private int day;

    @Scheduled(cron = "${auto.clean.monitor.log.cron.expression}")
    public void execute() {
        LOG.debug("*****  Start execute Job [{}]", getClass());

        logService.cleanMonitorLogs(day);

        LOG.debug("&&&&&  End execute Job [{}]", getClass());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(logService, "logService is null");
    }

}
