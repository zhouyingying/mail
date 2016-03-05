/**
 * 
 */
package com.zhou.mail;
import javax.mail.*;
/**
 * @author zhou
 *
 */
public class MyAuthenticator {
	String userName = null;
	String password = null;

	public MyAuthenticator() {
	}

	public MyAuthenticator(String username, String password) {
		this.userName = username;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}

	public static void main(String[] args) {
		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.126.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("bujie356@126.com");
		mailInfo.setPassword("z77118162");// 您的邮箱密码
		
		mailInfo.setFromAddress("bujie356@126.com");
		mailInfo.setToAddress("1064013525@qq.com");
		
		mailInfo.setSubject("起床了");
		mailInfo.setContent("起床了");
		
		
		// 这个类主要来发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		
		sms.sendTextMail(mailInfo);// 发送文体格式
		//sms.sendHtmlMail(mailInfo);// 发送html格式
	}
}
