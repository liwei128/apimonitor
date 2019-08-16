package com.ecar.apm.model;


public enum MonitorFrequency {
    //5秒
    //FIVE(5, "0/5 * * * * ?", "5秒"),
    //10秒
    //TEN(10, "0/10 * * * * ?", "10秒"),
    //20秒
    //TWENTY(20, "0/20 * * * * ?", "20秒"),
    //30秒
    THIRTY(30, "0/30 * * * * ?", "30秒"),
    //1分钟
    SIXTY(60, "0 0/1 * * * ?", "1分钟"),
    //2分钟
    //TWO_MINUTES(120, "0 0/2 * * * ?", "2分钟"),
    //3分钟
    //THREE_MINUTES(180, "0 0/3 * * * ?", "3分钟"),
    //5分钟
    FIVE_MINUTES(300, "0 0/5 * * * ?", "5分钟"),
    //10分钟
    ///TEN_MINUTES(600, "0 0/10 * * * ?", "10分钟"),
    //30分钟
    THIRTY_MINUTES(1800, "0 0/30 * * * ?", "30分钟"),
    //1小时
    ONE_HOUR(3600, "0 0 0/1 * * ?", "1小时");


    //时间,秒
    private int seconds;
    //cron表达式
    private String cronExpression;
    //标记
    private String label;

    private MonitorFrequency(int seconds, String cronExpression, String label) {
        this.seconds = seconds;
        this.cronExpression = cronExpression;
        this.label = label;
    }

    public int getSeconds() {
        return seconds;
    }

    // second -> milli second
    public int getMilliSeconds() {
        return seconds * 1000;
    }

    public String getValue() {
        return name();
    }

    public String getLabel() {
        return label;
    }

    public String getCronExpression() {
        return cronExpression;
    }
}