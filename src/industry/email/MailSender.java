package industry.email;

import industry.util.PathUtil;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 邮箱发送类
 * @author
 * @date 2014年7月1日 下午2:09:13
 */
public class MailSender{
	
	private Session session;
	
	public MailSender(){
		//需要身份认证，则创建一个密码验证器   
		MyAuthenticator authenticator=new MyAuthenticator("邮箱账号", "邮箱密码");
		//创建邮件会话属性
		Properties p=new Properties();
		p.put("mail.smtp.host", "smtp.163.com");
		p.put("mail.smtp.port", "25");
		p.put("mail.smtp.auth", "true");
		//根据邮件会话属性和密码验证器，构造一个发送邮件的session
		session=Session.getDefaultInstance(p, authenticator);
	}
	
	/**
	 * 发送文本格式的邮件
	 */
	public void sendTextMail(Email mail){
		Message msg=new MimeMessage(session);
		try {
			Address from=new InternetAddress(mail.getFromAddress());
			msg.setFrom(from);
			Address to=new InternetAddress(mail.getToAddress());
			msg.setRecipient(RecipientType.TO, to);
			msg.setSubject(mail.getSubject());
			msg.setSentDate(new Date());
			msg.setText(mail.getContent());
			Transport.send(msg);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 发送html格式的邮件
	 */
	public void sendHtmlMail(Email mail){
		Message msg=new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(mail.getFromAddress()));
			msg.setRecipient(RecipientType.TO, new InternetAddress(mail.getToAddress()));
			msg.setSentDate(new Date());
			msg.setSubject(mail.getSubject());
			// MimeMultipart类是一个容器类，包含MimeBodyPart类型的对象  
			Multipart multipart=new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart 
			BodyPart bodyPart=new MimeBodyPart();
			bodyPart.setContent(mail.getContent(), "text/html; charset=utf-8");
			multipart.addBodyPart(bodyPart);
			msg.setContent(multipart);
			Transport.send(msg);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 发送内嵌图片的邮件
	 */
	public void sendInnerImageMail(Email mail){
		Message msg=new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(mail.getFromAddress()));
			msg.addRecipient(RecipientType.TO, new InternetAddress(mail.getToAddress()));
			msg.setSubject(mail.getSubject());
			msg.setSentDate(new Date());
			Multipart multipart=new MimeMultipart("related");
			
//			MimeBodyPart imagePart=new MimeBodyPart();
//			DataSource datesource=new FileDataSource("D:\\01.bmp");
//			DataHandler handler=new DataHandler(datesource);
//			imagePart.setDataHandler(handler);
//			imagePart.setContentID("123.jpg");
//			multipart.addBodyPart(imagePart);
			
//			MimeBodyPart htmlPart=new MimeBodyPart();
//			Multipart htmlMultipart=new MimeMultipart("alternative");
//			MimeBodyPart htmlBodyPart=new MimeBodyPart();
//			htmlBodyPart.setContent("<span style='color:red;'>这是带内嵌图片的HTML邮件哦！！！<img src=\"cid:123.jpg\"/> </span>", "text/html;charset=utf-8");
//			htmlMultipart.addBodyPart(htmlBodyPart);
//			htmlPart.setContent(htmlMultipart);
//			multipart.addBodyPart(htmlPart);
//			
			msg.setContent(multipart);
			msg.saveChanges();
			Transport.send(msg);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送带附件的邮件
	 * @param mail
	 */
	public void sendMultipartMail(Email mail,String filename){
		Message msg=new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(mail.getFromAddress()));
			msg.setRecipient(RecipientType.TO, new InternetAddress(mail.getToAddress()));
			msg.setSentDate(new Date());
			msg.setSubject(mail.getSubject());
			// MimeMultipart类是一个容器类，包含MimeBodyPart类型的对象  
			Multipart multipart=new MimeMultipart();
			// 设置邮件内容 
			BodyPart contentPart=new MimeBodyPart();
			contentPart.setText(mail.getContent());
			multipart.addBodyPart(contentPart);
			// 添加附件内容
			BodyPart wordPart=new MimeBodyPart();
			String path=PathUtil.getInstance().getWebRoot()+"report\\/"+filename;
			// 检查文件是否存在
			File file=new File(path);
			if(!file.exists()){
				sendErrorMail("文件不存在："+path);
				return;
			}
			
			DataSource ds=new FileDataSource(path);
			wordPart.setDataHandler(new DataHandler(ds));
			sun.misc.BASE64Encoder enc=new sun.misc.BASE64Encoder();
			wordPart.setFileName("=?GBK?B?"+enc.encode(filename.getBytes("gbk"))+"?=");
			multipart.addBodyPart(wordPart);
			
			msg.setContent(multipart);
			Transport.send(msg);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送预警信息
	 * @param info
	 */
	public void sendErrorMail(String info){
		Message msg=new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress("@163.com"));
			msg.setRecipient(RecipientType.TO, new InternetAddress("@qq.com"));
			msg.setSentDate(new Date());
			msg.setSubject("金融舆情系统错误信息");
			// MimeMultipart类是一个容器类，包含MimeBodyPart类型的对象  
			Multipart multipart=new MimeMultipart();
			// 设置邮件内容 
			BodyPart contentPart=new MimeBodyPart();
			contentPart.setText(info);
			multipart.addBodyPart(contentPart);
			
			msg.setContent(multipart);
			Transport.send(msg);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MailSender sms=new MailSender();
		
		Email email=new Email();
		email.setFromAddress("发送者邮箱账号");
		email.setToAddress("接受者邮箱账号");
		
		email.setSubject("标题");
		
		StringBuffer sb=new StringBuffer();
		sb.append("<table width=1000px border=1px bgcolor=\"#f3f3f2\" bordercolorlight=\"#000000\" bordercolordark=\"#eeeeee\" cellspacing=1 cellpadding=0 align=\"center\">");
		sb.append("<tr align=\"center\"><td width=\"20%\"><b>5124121</b></td></tr>");
//		
//			sb.append("<tr align=\"center\">");
//			sb.append("<td onmouseout=\"this.style.backgroundColor=''\" onmouseover=\"this.style.backgroundColor='white'\">");
//			sb.append("媒体名称");
//			sb.append("</td>");
//			sb.append("<td title=\"点击查看详情\" onmouseout=\"this.style.backgroundColor=''\" onmouseover=\"this.style.backgroundColor='white'\">");
//			sb.append("<a target=\"_blank\" href=\"www.4399.com")
//			.append("www.4399.com").append("\">");//链接
//			
//			sb.append("标题");
//			sb.append("</a>");
//			sb.append("</td>");
//			sb.append("<td onmouseout=\"this.style.backgroundColor=''\" onmouseover=\"this.style.backgroundColor='white'\">");
//			sb.append("2014-10-31");
//			sb.append("</td>");
//			sb.append("</tr>");
		
		sb.append("</table>");
		email.setContent(sb.toString());
		
		sms.sendHtmlMail(email);
		System.out.println("发送成功");
	}

}

