package com.ecar.apm.model;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


public class Application implements InitializingBean {

    //系统字符编码
    public static final String ENCODING = "UTF-8";

    public static final String PROJECT_HOME = "https://gitee.com/mkk/HeartBeat/";
    //Current development version
    public static final String CURRENT_VERSION = "2.0.0";

    /**
     * 默认清理 监控日志的频率, 30天
     * 即超过30天以上的监控日志将自动清除
     * 日志包括 {@link com.andaily.hb.domain.log.FrequencyMonitorLog} 与 {@link com.andaily.hb.domain.log.MonitoringReminderLog }
     */
    public static final int CLEAN_MONITOR_LOG_FREQUENCY = 30;


    //application host
    private static String host;


    private static String minixinAppKey;
    private static String minixinAppSecret;
    private static String minixinHost;/*

    private static SystemSetting systemSetting;*/

    /*
    * default
    * */
    public Application() {
    }


    public static String minixinAppKey() {
        return minixinAppKey;
    }

    public void setMinixinAppKey(String minixinAppKey) {
        Application.minixinAppKey = minixinAppKey;
    }

    public static String minixinAppSecret() {
        return minixinAppSecret;
    }

    public void setMinixinAppSecret(String minixinAppSecret) {
        Application.minixinAppSecret = minixinAppSecret;
    }

    public static String minixinHost() {
        return minixinHost;
    }

    public void setMinixinHost(String minixinHost) {
        Application.minixinHost = minixinHost;
    }

    public static String host() {
        return host;
    }

    public void setHost(String host) {
        Application.host = host;
    }

/*
    public static SystemSetting systemSetting() {
        checkingAndInitialSystemSetting();
        return systemSetting;
    }

    private static void checkingAndInitialSystemSetting() {
        if (systemSetting == null) {
            UserRepository userRepository = BeanProvider.getBean(UserRepository.class);
            systemSetting = userRepository.findSystemSetting();
            Assert.notNull(systemSetting, "systemSetting is null");
        }
    }*/

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(host, "host is null");

        Assert.notNull(minixinAppKey, "minixinAppKey is null");
        Assert.notNull(minixinAppSecret, "minixinAppSecret is null");
        Assert.notNull(minixinHost, "minixinHost is null");
    }
}