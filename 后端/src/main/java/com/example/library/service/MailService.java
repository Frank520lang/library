package com.example.library.service;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.library.exception.CustomizeErrorCode;
import com.example.library.exception.CustomizeException;

@Service
public class MailService {

	@Autowired
	private JavaMailSender mailSender;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${spring.mail.from}")
    private String from;
	
	public void sendHtmlMail(String to,String subject,String content) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
			helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setSubject(subject);
			helper.setTo(to);
			helper.setText(content, true);
			mailSender.send(message);
			logger.info("邮件已经发送。");
		} catch (MailSendException e) {
			// TODO Auto-generated catch block
			throw new CustomizeException(CustomizeErrorCode.NO_EMAIL);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			logger.error("发送邮件时发生异常！", e);
		}
    }
}
