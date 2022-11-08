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
import com.developerkirby.Main.DAO.WriteDAO;
import com.developerkirby.Main.VO.WriteVO;



@WebServlet("/WriteSearchServlet")
public class WriteSearchServlet extends HttpServlet {
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

		String getQuery = (String)jsonObj.get("query"); // 요청된 target를 받음
		String offsetNum = (String)jsonObj.get("offsetNum");
		String limitNum = (String)jsonObj.get("limitNum");
		PrintWriter out = response.getWriter(); // 출력을 위해 만듦, 출력 스트림에 텍스트를 보내겠다는 뜻

		System.out.print(getQuery);

		WriteDAO dao = new WriteDAO();
		List<WriteVO> list = dao.writeSearchSelect(getQuery, offsetNum, limitNum);
		JSONArray writeArray = new JSONArray();

		for(WriteVO e : list) {
			JSONObject writeInfo = new JSONObject();
			writeInfo.put("writeNum", e.getWriteNum());
			writeInfo.put("writeName", e.getWriteName());
			writeInfo.put("writeDate", e.getWriteDateStr());
			writeInfo.put("nickname", e.getNickname());
			writeInfo.put("writeContent", e.getWriteContents());
			writeInfo.put("countGood", e.getCountGood());
			writeInfo.put("countComments", e.getCountComment());

			writeArray.add(writeInfo);
		}
		out.print(writeArray);
		System.out.println(writeArray);
	}

}