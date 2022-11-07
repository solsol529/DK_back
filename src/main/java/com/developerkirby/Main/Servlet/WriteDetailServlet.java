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
import com.developerkirby.Main.DAO.WriteDAO;
import com.developerkirby.Main.VO.WriteVO;


@WebServlet("/WriteDetailServlet")
public class WriteDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Common.corsResSet(response);
	}
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// 한글 깨짐 방지를 위해서 설정
		request.setCharacterEncoding("utf-8");
		// CORS 접근 허용
		Common.corsResSet(response);
		// 요청 메시지 받기
		StringBuffer sb = Common.reqStringBuff(request);
		// 요청 받은 메시지 JSON 파싱
		JSONObject jsonObj = Common.getJsonObj(sb);
		
		String reqCmd = (String)jsonObj.get("cmd");
		int reqWriteNum = Integer.valueOf(jsonObj.get("writeNum").toString());
	
		PrintWriter out = response. getWriter();
		if(!reqCmd.equals("WriteDetailInfo")) {
			JSONObject resJson = new JSONObject();
			resJson.put("result", "NOK");
			out.print(resJson);
			return;
		} 
		
		WriteDAO dao = new WriteDAO();
		List<WriteVO> list = dao.writeDetailSelect(reqWriteNum);
		JSONArray writeArray = new JSONArray();
		for(WriteVO e : list) {
			JSONObject writeInfo = new JSONObject();
			writeInfo.put("boardName", e.getBoardName());
			writeInfo.put("writeName", e.getWriteName());
			writeInfo.put("pfImg", e.getPfImg());
			writeInfo.put("writeDate", e.getWriteDateStr());
			writeInfo.put("nickname", e.getNickname());
			writeInfo.put("writeContent", e.getWriteContents());
			writeInfo.put("countGood", e.getCountGood());
			writeInfo.put("countComments", e.getCountComment());
			
			writeArray.add(writeInfo);
			
		}
		System.out.println(writeArray);
		out.print(writeArray);
	}
}
