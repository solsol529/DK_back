package com.DK.admin.adSendEmail;

import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

public class EmailSender {
	// 메일을 보내는 이의 메일주소, 보내는 이름, 패스워드
	private String emailAddress, senderName, emailPassword;
	
	public EmailSender(String emailAddress, String senderName, String emailPassword) {
		this.emailAddress = emailAddress;
		this.senderName = senderName;
		this.emailPassword = emailPassword;
	}


	public String getEmailAddress() {
		return emailAddress;
	}


	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


	public String getSenderName() {
		return senderName;
	}


	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}


	public String getEmailPassword() {
		return emailPassword;
	}


	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}
	
	//메일보내기
	void sendSimple(String email, String name, String title, String content) {
		//메일 수신자 이메일, 이름
		SimpleEmail mail = new SimpleEmail();
		mail.setCharset("utf-8");
		mail.setDebug(true);
		mail.setHostName("smtp.naver.com");
		mail.setAuthentication("developerkirby@naver.com","Devkirby1234!");
		mail.setSmtpPort(587);
		mail.setStartTLSEnabled(true);
		
		try {
			// 보내는 사람 이메일 주소와 이름 
			mail.setFrom("developerkirby@naver.com","developerkirby");
			mail.addTo(email, name); // 받을사람 지정
			
			// 제목지정
			mail.setSubject(title);
			mail.setMsg(name + content);
			
			
			mail.send(); // 보내기 버튼 클릭
		}catch(EmailException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void sendMulti(List<String> emails, List<String> names, List<String> titles, List<String> contents) {
		for(int i = 0; i < emails.size() ; i++) {
			// email값을 sendSimple매개변수에 넣기 
			sendSimple(emails.get(i), names.get(i), titles.get(i), contents.get(i));
		}
	}
	

		
}

