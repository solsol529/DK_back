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
import com.developerkirby.Main.DAO.MemberDAO;
import com.developerkirby.Main.VO.MemberVO;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	// 옵션을 만드는 이유 -> 여기에도 CORS 허용 해주어야 정상 작동함
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Common.corsResSet(response);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8"); // 한글이 들어올수도 있으니 utf-8
		// CORS 접근 허용, 해킹의도로 판단하는것을 풀기 위해서
		Common.corsResSet(response);
		// 요청 메시지 스트링으로 받기
		StringBuffer sb = Common.reqStringBuff(request);
		// 파싱
		JSONObject jsonObj = Common.getJsonObj(sb);
		
		// 프론트엔드에서 const loginObj = nickname: nickname, pwd: pwd } 해주었기 때문에 가능함
		String getNickname = (String)jsonObj.get("nickname");
		String getPwd = (String)jsonObj.get("pwd");
		System.out.println("닉네임: "+getNickname);
		System.out.println("비밀번호: "+getPwd);
		
		PrintWriter out = response.getWriter();
		// json object에 넣어줘야함
		JSONObject resJson = new JSONObject();
		
		MemberDAO dao = new MemberDAO();
		MemberVO memberInfo = dao.logingCheck(getNickname, getPwd);
		
		System.out.println("db닉네임: "+ memberInfo.getNickname());
		System.out.println("db비밀번호: "+ memberInfo.getPwd());
		
		if(getNickname.equals(memberInfo.getNickname()) && 
				getPwd.equalsIgnoreCase(memberInfo.getPwd())) {
			resJson.put("result", "OK");
			resJson.put("memberNum", memberInfo.getMemberNum());
			System.out.println("로그인 성공");
		} else {
			resJson.put("result", "NOK");
			System.out.println("로그인 실패");
		}
		out.print(resJson);		
	}
}
