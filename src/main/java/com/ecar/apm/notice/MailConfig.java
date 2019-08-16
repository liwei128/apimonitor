package com.ecar.apm.notice;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSON;

/**
 * 邮箱配置
 * @author 01383518
 * @date:   2019年3月1日 下午2:53:05
 */
@Configuration
@ConfigurationProperties(prefix = MailConfig.CONF_PREFIX)
public class MailConfig {
	
	public static final String CONF_PREFIX = "mail";
	
	/**
	 * 用户名
	 */
	private String user;
	
	/**
	 * 邮箱服务器地址
	 */
	private String host;
	
	/**
	 * 都登录密码
	 */
	private String pwd;
	
	/**
	 * 发信箱地址
	 */
	private String from;
	
	/**
	 * 邮件主题后缀
	 */
	private String suffix;

	

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}


	
	
	
	

}
