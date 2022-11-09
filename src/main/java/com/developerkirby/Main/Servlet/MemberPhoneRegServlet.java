package com.developerkirby.Main.Servlet;

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


@WebServlet("/MemberPhoneRegServlet")
public class MemberPhoneRegServlet extends HttpServlet {

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

		
		String getPhone = (String)jsonObj.get("phone");
		MemberDAO dao = new MemberDAO();
		boolean isAvailable = dao.memberPhoneCheck(getPhone);
		
		System.out.println("문자 보낼수 있음(중복이 아님)?" + isAvailable);
		
		PrintWriter out = response.getWriter();
		JSONObject resJson = new JSONObject();
		
		if (isAvailable) {
			int verifyCode = sens_sms.makeVerifyCode();
			resJson.put("result", "OK");
			resJson.put("code", verifyCode);
			System.out.println("인증번호 : "+verifyCode);
			String match = "[^0-9]";
			getPhone = getPhone.replaceAll(match, "");
			boolean isSend = sens_sms.sendSms(getPhone, verifyCode);

			if(isSend) {
				resJson.put("result", "OK");
				resJson.put("code", verifyCode);
				System.out.println("인증번호 : "+verifyCode);
			}
			else resJson.put("result", "NOK");
		} else resJson.put("result", "DUP");
		
		out.print(resJson);
	}

}
