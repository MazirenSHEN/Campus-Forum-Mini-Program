package com.nuoquan.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
public class EmailTool {
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}") // 发送人的邮箱
	private String from;
	
	public void simpleSend(String toAddr) {
		
	    SimpleMailMessage message = new SimpleMailMessage(); 
	    message.setTo(toAddr);
	    message.setSubject("标题：HelloWorld");
	    message.setText("Hello World!");
	    message.setFrom(from); //确保发送邮件地址与认证地址，如果不一致， 一些邮箱服务器会退信
	     
	    javaMailSender.send(message);
	}

	/**
	 * 测试用发送验证码
	 */

	public void sendSimpleMail() {
		MimeMessage message = null;
		try {
			message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom("nottinghome_it@163.com");
			helper.setTo("jerrio20@outlook.com");
			helper.setSubject("标题：发送Html内容");

			StringBuffer sb = new StringBuffer();
			sb.append("<h1>大标题-h1</h1>").append("<p style='color:#F00'>红色字</p>")
					.append("<p style='text-align:right'>右对齐</p>");
			helper.setText(sb.toString(), true);
//            FileSystemResource fileSystemResource=new FileSystemResource(new File("D:\76678.pdf"));
//            helper.addAttachment("电子发票", fileSystemResource);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送验证码模板
	 * @param toAddr
	 * @param code
	 */
	public void sendCodeToMail(String toAddr, String code) {
		MimeMessage message = null;
		try {
			message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from); //确保发送邮件地址与认证地址，如果不一致， 一些邮箱服务器会退信
			helper.setTo(toAddr);
			helper.setSubject(code + " - 您的 Stoa 验证码");
			
			StringBuffer content = new StringBuffer();
			
			content.append("<p>亲爱的同学：</p>")
				.append("<p>欢迎你加入【 Stoa™ 】共创社区，以下是您的验证码：</p>")
				.append("<p><b>["+ code +"]</b> 有效期十分钟</p>")
				.append("<p>期待您与聚像科技一起创造价值，分享价值。</br></p>");
			helper.setText(content.toString(), true);
		     
		    javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}