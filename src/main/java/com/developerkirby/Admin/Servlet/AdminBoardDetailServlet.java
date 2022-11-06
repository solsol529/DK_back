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
import com.developerkirby.Admin.DAO.AdminBoardDAO;
import com.developerkirby.Admin.VO.AdminBoardVO;
import com.developerkirby.Admin.VO.AdminWriteVO;

@WebServlet("/AdminBoardDetailServlet")
public class AdminBoardDetailServlet extends HttpServlet {
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

		String reqCmd = (String)jsonObj.get("target"); // 요청된 명령어(cmd)를 받음
		PrintWriter out = response.getWriter(); // 출력을 위해 만듦, 출력 스트림에 텍스트를 보내겠다는 뜻

		AdminBoardDAO dao = new AdminBoardDAO();
		List<AdminBoardVO> list = dao.boardDetail(reqCmd);
		JSONArray writeArray = new JSONArray();

		for(AdminBoardVO e : list) {
			JSONObject boardInfo = new JSONObject();

			JSONArray writesArray = new JSONArray();
			for(AdminWriteVO el : e.getWrites()) {
				JSONObject writesInfo = new JSONObject();
				writesInfo.put("nickname", el.getNickname());
				writesInfo.put("writeDate", el.getWriteDateStr());
				writesInfo.put("writeName", el.getWriteName());
				writesArray.add(writesInfo);
			}
			boardInfo.put("countWrite", e.getCountWrite());
			boardInfo.put("writes", writesArray);
			writeArray.add(boardInfo);
		}
		out.print(writeArray);
	}
}
