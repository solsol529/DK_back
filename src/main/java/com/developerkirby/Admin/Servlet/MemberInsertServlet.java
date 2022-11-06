package com.developerkirby.Admin.Servlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.developerkirby.Main.Common;
import com.developerkirby.Main.sens_sms;
import com.developerkirby.Main.DAO.MemberDAO;


@WebServlet("/MemberInsertServlet")
public class MemberInsertServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Common.corsResSet(response);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 한글 깨짐 방지를 위해서 설정
		request.setCharacterEncoding("utf-8");
		// CORS 접근 허용
		Common.corsResSet(response);

		// 요청 메시지 받기
		StringBuffer sb = Common.reqStringBuff(request);

		// 요청 받은 메시지 JSON 파싱
		JSONObject jsonObj = Common.getJsonObj(sb);

		//String nickname, String pwd, String phone, String email, String adOk 받아야함
		String getNickname = (String)jsonObj.get("nickname");
		String getPwd = (String)jsonObj.get("pwd");
		String getPhone = (String)jsonObj.get("phone");
		String getEmail = (String)jsonObj.get("email");
		String getAdOk = (String)jsonObj.get("adOk");
		MemberDAO dao = new MemberDAO();
		int memberNum = dao.memberInsert(getNickname, getPwd, getPhone, getEmail, getAdOk);
		
		PrintWriter out = response.getWriter();
		JSONObject resJson = new JSONObject();
		
		if(memberNum == 0) {
			resJson.put("result", "NOK");
		}
		else {
			resJson.put("result", "OK");
			resJson.put("memberNum", memberNum);
		}
		
		out.print(resJson);
	}

}
