package com.jeremias.dev.services;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.jeremias.dev.exceptions.MailedException;
import com.jeremias.dev.models.NotificationEmail;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
	private final MailContentBuilder mailBuilder;
	private final JavaMailSender mailSender;
	 @Async
	  public  void sendMail(NotificationEmail notificationEmail) {
	        MimeMessagePreparator messagePreparator = mimeMessage -> {
	            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
	            messageHelper.setFrom("springreddit@email.com");
	            messageHelper.setTo(notificationEmail.getRecipient());
	            messageHelper.setSubject(notificationEmail.getSubject());
	            //Before messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
	// After
	messageHelper.setText(notificationEmail.getBody());
	        };
	        try {
	            mailSender.send(messagePreparator);
	           // log.info("Activation email sent!!");
	        } catch (MailException e) {
	            throw new MailedException("Exception occurred when sending mail to " + notificationEmail.getRecipient());
	        }
	    }
	
}
