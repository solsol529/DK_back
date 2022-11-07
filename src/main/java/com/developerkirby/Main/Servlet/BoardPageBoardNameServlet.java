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
import com.developerkirby.Main.DAO.BoardDAO;
import com.developerkirby.Main.VO.BoardVO;


@WebServlet("/BoardPageNameServlet")
public class BoardPageBoardNameServlet extends HttpServlet {
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
		request.setCharacterEncoding("utf-8");
		// CORS 접근 허용
		Common.corsResSet(response);
		// 요청 메시지 받기
		StringBuffer sb = Common.reqStringBuff(request);
		// 요청 받은 메시지 JSON 파싱
		JSONObject jsonObj = Common.getJsonObj(sb);
		
		String reqCmd = (String)jsonObj.get("cmd");
		String reqBoardName = (String)jsonObj.get("boardName");
		
		PrintWriter out = response. getWriter();
		if(!reqCmd.equals("BoardNameInfo")) {
			JSONObject resJson = new JSONObject();
			resJson.put("result", "NOK");
			out.print(resJson);
			return;
		} 
		
		BoardDAO dao = new BoardDAO();
		List<BoardVO> list = dao.boardNameSelect(reqBoardName);
		JSONArray boardArray = new JSONArray();
		for(BoardVO e : list) {
			JSONObject boardInfo = new JSONObject();
			boardInfo.put("boardName", e.getBoardName());
			boardArray.add(boardInfo);
		}
		System.out.println(boardArray);
		out.print(boardArray);
	}

}
