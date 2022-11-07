package com.developerkirby.Main.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
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
import com.developerkirby.Main.DAO.BoardDAO;
import com.developerkirby.Main.VO.BoardVO;
import com.developerkirby.Main.VO.WriteVO;

@WebServlet("/BoardServlet")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Common.corsResSet(response);
	}
	
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
		System.out.println("전달 받은 cmd : " + reqCmd);
		PrintWriter out = response. getWriter();
		if(!reqCmd.equals("BoardInfo")) {
			JSONObject resJson = new JSONObject();
			resJson.put("result", "NOK");
			out.print(resJson);
			return;
		} 
		
		BoardDAO dao = new BoardDAO();
		List<BoardVO> list = dao.boardSelect();
		JSONArray boardArray = new JSONArray();
		for(BoardVO e : list) {
			JSONObject boardInfo = new JSONObject();
			JSONArray writesArray = new JSONArray();
			 for(WriteVO el : e.getWrites()) {
		            JSONObject writesInfo = new JSONObject();
		            writesInfo.put("writeName", el.getWriteName());
		            writesInfo.put("writeDate", el.getWriteDateStr());
		            writesInfo.put("writeNum", el.getWriteNum());
		            writesArray.add(writesInfo);
		         }

			boardInfo.put("boardName", e.getBoardName());
			boardInfo.put("mainWrites", writesArray);
			boardArray.add(boardInfo);
		}
		System.out.println(boardArray);
		out.print(boardArray);
	}

}
