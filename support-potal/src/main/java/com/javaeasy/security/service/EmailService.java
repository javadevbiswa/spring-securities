package com.javaeasy.security.service;

import static com.javaeasy.security.constant.EmailConstants.CC_EMAIL;
import static com.javaeasy.security.constant.EmailConstants.DEFAULT_PORT;
import static com.javaeasy.security.constant.EmailConstants.EMAIL_SUBJECT;
import static com.javaeasy.security.constant.EmailConstants.FROM_EMAIL;
import static com.javaeasy.security.constant.EmailConstants.GMAIL_SMTP_SERVER;
import static com.javaeasy.security.constant.EmailConstants.PASSWORD;
import static com.javaeasy.security.constant.EmailConstants.SIMPLE_MAIL_TRANSFER_PROTOCOL;
import static com.javaeasy.security.constant.EmailConstants.SMTP_AUTH;
import static com.javaeasy.security.constant.EmailConstants.SMTP_HOST;
import static com.javaeasy.security.constant.EmailConstants.SMTP_PORT;
import static com.javaeasy.security.constant.EmailConstants.SMTP_STARTTLS_ENABLE;
import static com.javaeasy.security.constant.EmailConstants.SMTP_STARTTLS_REQUIRED;
import static com.javaeasy.security.constant.EmailConstants.USERNAME;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.sun.mail.smtp.SMTPTransport;

@Service
public class EmailService {

	public void sendEmail(String firstName, String password, String toEmailAddress) {

		try {
			Message message = createMessage(firstName, password, toEmailAddress);

			SMTPTransport transport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);

			transport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);

			transport.sendMessage(message, message.getAllRecipients());

			transport.close();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	public Message createMessage(String firstName, String password, String email) throws MessagingException {

		Message message = new MimeMessage(getEmailSession());
		message.setSubject(EMAIL_SUBJECT);
		message.setFrom(new InternetAddress(FROM_EMAIL));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
		message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(CC_EMAIL, false));

		message.setText("Hello " + firstName + ", \n \n Your new account password is: " + password
				+ "\n \n Biswa JavaTechie Team");
		message.setSentDate(new Date());
		message.saveChanges();
		return message;

	}

	public Session getEmailSession() {

		Properties props = System.getProperties();

		props.put(SMTP_HOST, GMAIL_SMTP_SERVER);
		props.put(SMTP_AUTH, true);
		props.put(SMTP_PORT, DEFAULT_PORT);

		props.put(SMTP_STARTTLS_ENABLE, true);
		props.put(SMTP_STARTTLS_REQUIRED, true);
		return Session.getInstance(props, null);
	}
	
	public static void main(String[] args) {
		new EmailService().sendEmail("Ashru", "tdtrfyvyfyrfyuutv", "ashru.sahoo06@gmail.com");
	}

}
