package com.ecar.apm.notice;

import java.util.Date;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 邮件发送服务
 * @author 01383518
 * @date:   2019年3月1日 下午2:57:46
 */
@Service
public class MailSendService{
	
	private static final Logger logger = LoggerFactory.getLogger(MailSendService.class);
	
	@Autowired
	private MailConfig mailConfig;

	/**
	 * 发送邮件
	 */
	public void sendMail(String[] to,String title,String mailBody){
		logger.info("发送邮件：to:{},title:{},mailBody:{}",to,mailBody);
		Transport transport = null;
		try{
			//获取邮件传输对象
			Session session = createSession();
			transport = session.getTransport();
			//创建邮件
            MimeMessage message = createMimeMessage(session,title,mailBody,to);
            //连接
            transport.connect(mailConfig.getUser(), mailConfig.getPwd());
            // 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            
		}catch(Exception e){
			logger.error("邮件发送异常,to:{},title:{},body:{}",to,title,mailBody,e);
    	}finally {
    		if(transport!=null){
    			try {
					transport.close();
				} catch (MessagingException e) {
					logger.error("邮件资源关闭失败",e);
				}
    		}
		}
	}

	/**
	 * 创建一封邮件
	 * @param session
	 * @param title
	 * @param mailBody
	 * @param tos
	 * @return
	 * @throws Exception
	 */
	private MimeMessage createMimeMessage(Session session,String title, String mailBody,String[] tos) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mailConfig.getFrom(), "ITRMS", "UTF-8"));
        for(String to : tos){
        	message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to, "", "UTF-8"));
        }
        message.setSubject(title+mailConfig.getSuffix(), "UTF-8");
        message.setContent(mailBody, "text/html;charset=UTF-8");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }
	
	/**
	 * 创建邮箱配置
	 * @return
	 */
	private Session createSession() {
		Properties props = new Properties(); 
        props.setProperty("mail.transport.protocol", "smtp"); 
        props.setProperty("mail.smtp.host", mailConfig.getHost()); 
        props.setProperty("mail.smtp.auth", "true"); 
        return Session.getDefaultInstance(props);
	}



}
