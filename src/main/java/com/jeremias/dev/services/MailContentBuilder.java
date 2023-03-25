package com.jeremias.dev.services;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MailContentBuilder {
	private final TemplateEngine template;
	
	public String buildConfirmMail(String message) {
		 Context context = new Context();
	     context.setVariable("message", message);
	     return template.process("confirmMailTemplate", context);
	}
	
	public String buildResetPassword(String message) {
		 Context context = new Context();
	     context.setVariable("message", message);
	     return template.process("resetPasswordTemplate", context);
	}
	
}
