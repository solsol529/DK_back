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

import com.developerkirby.Admin.Common;
import com.developerkirby.Admin.DAO.AdminAdDAO;
import com.developerkirby.Admin.VO.AdminAdVO;

@WebServlet("/AdSelectServlet")
public class AdSelectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	         throws ServletException, IOException {
	      response.getWriter().append("Served at: ").append(request.getContextPath());
	   }

	   // CORS 처리
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
	      
	      String adCmd = (String)jsonObj.get("cmd");
	      
	      PrintWriter out = response. getWriter();
	      	if(!adCmd.equals("AdInfo")) {
	         JSONObject resJson = new JSONObject();
	         // 실패 시 알림창이나 모달로 처리 해야함 
	         resJson.put("result", "NOK");
	         out.print(resJson);
	         return;
	      } 
	      
	      AdminAdDAO dao = new AdminAdDAO();
	      List<AdminAdVO> list = dao.AdSelect();
	      JSONArray adArray = new JSONArray();
	      for(AdminAdVO e : list) {
	         JSONObject adInfo = new JSONObject();
	         adInfo.put("adNum", e.getAd_num());
	         adInfo.put("adName", e.getAd_name());
	         adInfo.put("adUrl", e.getAd_url());
	         adInfo.put("adImg", e.getAd_img());
	         adArray.add(adInfo);
	      }
	      System.out.println(adArray);
	      out.print(adArray);
	   }

}
