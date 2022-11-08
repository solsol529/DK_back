package com.DK.admin.adSendEmail;

import java.util.ArrayList;
import java.util.List;

import com.DK.admin.dao.AdminAdDAO;
import com.DK.admin.vo.AdminAdVO;

public class AdminEmailMain {
	public static void main(String[] args) {
		EmailSender mail = new EmailSender("developerkirby@naver.com","developerkirby","Devkirby1234!");
//		mail.sendSimple("heej0258@naver.com","heeju");
		
	
//		mail.sendHtml("heej0258@naver.com","heeju");
		System.out.println("message sent successfully...");
	}
	  
}
