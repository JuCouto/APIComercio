package com.residencia.comercio.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MailService {

		@Value("${spring.mail.host}")
		private String mailHost;
		
		@Value("${spring.mail.port}")
		private String mailPort;
		
		@Value("${spring.mail.username}")
		private String mailUserName;
		
		@Value("${spring.mail.password}")
		private String mailPassword;
		
		@Autowired
		JavaMailSender emailSender;
		
		public MailService (JavaMailSender javaMailSender) {
			this.emailSender = javaMailSender;
		}
		
		public void enviarEmailTexto (String destinatarioEmail, String assunto, String mensagemEmail) {
			SimpleMailMessage sMailMessage = new SimpleMailMessage();
			
			sMailMessage.setTo(destinatarioEmail);
			sMailMessage.setSubject(assunto);
			sMailMessage.setText(mensagemEmail);
			
			sMailMessage.setFrom("juliana.couto@aluno.senai.br");
			
			emailSender.send(sMailMessage);
		}
		
		/*public void enviarEmailHtml(MultipartFile file, String destinatarioEmail, String assunto, String mensagemEmail)throws Exception {
			
			helper.setText(htmlMsg,true);
			helper.setTa()
		}*/
}

