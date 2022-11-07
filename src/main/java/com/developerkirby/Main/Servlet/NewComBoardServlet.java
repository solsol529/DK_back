package com.developerkirby.Main.Servlet;

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

import com.developerkirby.Main.Common;
import com.developerkirby.Main.DAO.NewComBoardDAO;
import com.developerkirby.Main.VO.WriteVO;

@WebServlet("/NewComBoardServlet")
public class NewComBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Common.corsResSet(response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		// CORS 접근 허용
		Common.corsResSet(response);
		// 요청 메시지 받기
		StringBuffer sb = Common.reqStringBuff(request);
		// 요청 받은 메시지 JSON 파싱
		JSONObject jsonObj = Common.getJsonObj(sb);
		
		String reqCmd = (String)jsonObj.get("cmd");
		System.out.println("전달 받은 cmd : " + reqCmd);
		
		PrintWriter out = response. getWriter();
		if(!reqCmd.equals("NewComBoardInfo")) {
			JSONObject resJson = new JSONObject();
			resJson.put("result", "NOK");
			out.print(resJson);
			return;
		} 
		
		NewComBoardDAO dao = new NewComBoardDAO();
		List<WriteVO> list = dao.newComBoardSelelct();
		JSONArray writeArray = new JSONArray();
		for(WriteVO e : list) {
			JSONObject writeInfo = new JSONObject();
			
			writeInfo.put("writeName", e.getWriteName());
			writeInfo.put("writeDate", e.getWriteDateStr());
            writeInfo.put("writeNum", e.getWriteNum());
            writeArray.add(writeInfo);
		}
		System.out.println(writeArray);
		out.print(writeArray);
	}
}