package com.DK.admin.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.DK.admin.adSendEmail.EmailSender;
import com.DK.admin.common.Common;
import com.DK.admin.dao.AdminAdDAO;
import com.DK.admin.vo.AdminAdVO;



@WebServlet("/AdminADNotiSendServlet")
public class AdminADNotiSendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	         throws ServletException, IOException {
	      response.getWriter().append("Served at: ").append(request.getContextPath());
	   }

	   // CORS 처리
	   protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
	         throws ServletException, IOException {
	      Common.corsResSet(response);
	   }
	   
	   @SuppressWarnings("unchecked")
	   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			request.setCharacterEncoding("utf-8");
			Common.corsResSet(response);
			StringBuffer sb = Common.reqStringBuff(request);
			JSONObject jsonObj = Common.getJsonObj(sb);
		
			// 구별자 받아오기
			String getMail = (String)jsonObj.get("mail");
			String getTitles = (String)jsonObj.get("title");
			String getContents = (String)jsonObj.get("content");
			
			
			AdminAdDAO dao = new AdminAdDAO();
			dao.AdNotiSend1();
			if(getMail.equals("notice")) {
				dao.AdNotiSend1();
			}else {
				dao.AdNotiSend2();
			}
			
			PrintWriter out = response.getWriter();
			List<String> titles = new ArrayList<>();
			List<String> contents = new ArrayList<>();
			
			List<String> emails = new ArrayList<>();
			List<String> names = new ArrayList<>();
			EmailSender sender = new EmailSender("developerkirby@naver.com","developerkirby","Devkirby1234!");
			
			if(getMail.equals("notice")) {
				List<AdminAdVO> list = dao.AdNotiSend1();
				JSONArray adArray = new JSONArray();
				
				for(AdminAdVO e1 : list) {
					JSONObject notiSend1 = new JSONObject();
					notiSend1.put("nickName", e1.getNickName());
					notiSend1.put("email", e1.getEmail());
					notiSend1.put("title", getTitles);
					notiSend1.put("contents", getContents);
					
			        adArray.add(notiSend1);
			        
			        emails.add(e1.getEmail());
			        names.add(e1.getNickName());
			        titles.add(getTitles);
			        contents.add(getContents);
				}
				
				
				
				out.print(adArray);
				System.out.println(emails);
				sender.sendMulti(emails, names, titles, contents);	
				
			}else {
				List<AdminAdVO> list = dao.AdNotiSend2();
				JSONArray adArray = new JSONArray();
				for(AdminAdVO e1 : list) {
					JSONObject notiSend2 = new JSONObject();
					notiSend2.put("nickName", e1.getNickName());
					notiSend2.put("email", e1.getEmail());
					
					notiSend2.put("titles", getTitles);
					notiSend2.put("contents", getContents);
					
			        adArray.add(notiSend2);
			        
			        emails.add(e1.getEmail());
			        names.add(e1.getNickName());
			        titles.add(getTitles);
			        contents.add(getContents);
				}
				out.print(adArray);
				sender.sendMulti(emails, names, titles, contents);
			}
	   }
}

