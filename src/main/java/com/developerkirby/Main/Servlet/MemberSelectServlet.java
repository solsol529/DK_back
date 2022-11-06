package com.developerkirby.Main.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.developerkirby.Main.Common;
import com.developerkirby.Main.DAO.MemberDAO;
import com.developerkirby.Main.VO.MemberVO;

@WebServlet("/MemberSelectServlet")
public class MemberSelectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Common.corsResSet(response);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		// CORS 접근 허용
		Common.corsResSet(response);
		// 요청 메시지 받기
		StringBuffer sb = Common.reqStringBuff(request);
		// 요청 받은 메시지 JSON 파싱
		JSONObject jsonObj = Common.getJsonObj(sb); // 여기까지가 공통 루틴

		String reqCmd = (String)jsonObj.get("cmd"); // 요청된 명령어(cmd)를 받음
		String target = (String)jsonObj.get("target");
		PrintWriter out = response.getWriter(); // 출력을 위해 만듦, 출력 스트림에 텍스트를 보내겠다는 뜻
		if (!reqCmd.equals("MemberInfo")) { // 비정상적인 경우
			JSONObject resJson = new JSONObject();
			resJson.put("result", "NOK");
			out.print(resJson);
			return;
		}
		// 정상적인 경우
		MemberDAO dao = new MemberDAO();
		List<MemberVO> list = dao.memberSelect(target);
		JSONArray memberArray = new JSONArray();

		for(MemberVO e : list) {
			JSONObject memberInfo = new JSONObject();
			memberInfo.put("memberNum", e.getMemberNum());
			memberInfo.put("nickname", e.getNickname());
			memberInfo.put("grade", e.getGrade());
			memberInfo.put("countWrite", e.getCountWrite());
			memberInfo.put("countComment", e.getCountComment());
			memberInfo.put("phone", e.getPhone());
			memberInfo.put("email", e.getEmail());
			memberInfo.put("pfImg", e.getPfImg());
			memberInfo.put("isAdOk", e.getIsAdOk());
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd");
			String dateToStr = sdf.format(e.getRegDate());
			memberInfo.put("regDate", dateToStr);
			memberArray.add(memberInfo);
		}
		out.print(memberArray);
	}
}
