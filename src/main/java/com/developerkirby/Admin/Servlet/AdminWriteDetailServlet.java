package com.developerkirby.Admin.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.developerkirby.Admin.Common;
import com.developerkirby.Admin.DAO.AdminWriteDAO;
import com.developerkirby.Admin.VO.AdminWriteVO;

@WebServlet("/AdminWriteDetailServlet")
public class AdminWriteDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Common.corsResSet(response);
	}
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		// CORS 접근 허용
		Common.corsResSet(response);
		// 요청 메시지 받기
		StringBuffer sb = Common.reqStringBuff(request);
		// 요청 받은 메시지 JSON 파싱
		JSONObject jsonObj = Common.getJsonObj(sb); // 여기까지가 공통 루틴
		
		String reqCmd = (String)jsonObj.get("writeNum"); // 요청된 명령어(cmd)를 받음
		PrintWriter out = response.getWriter(); // 출력을 위해 만듦, 출력 스트림에 텍스트를 보내겠다는 뜻
		
		AdminWriteDAO dao = new AdminWriteDAO();
		List<AdminWriteVO> list = dao.writeSelect();
		JSONArray writeArray = new JSONArray();
		
		for(AdminWriteVO e : list) {
			JSONObject writeInfo = new JSONObject();
			writeInfo.put("writeNum", e.getWriteNum());
			writeInfo.put("writeName", e.getWriteName());
			writeInfo.put("writeDate", e.getWriteDate());
			writeInfo.put("nickname", e.getNickname());
			
			writeArray.add(writeInfo);
		}
		out.print(writeArray);
	}
}
