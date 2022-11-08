package com.DK.admin.servlet;

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

import com.DK.admin.common.Common;
import com.DK.admin.dao.AdminAdDAO;
import com.DK.admin.vo.AdminAdVO;



@WebServlet("/AdminAdDeleteServlet")
public class AdminAdDeleteServlet extends HttpServlet {
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
	   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			request.setCharacterEncoding("utf-8");
			Common.corsResSet(response);
			StringBuffer sb = Common.reqStringBuff(request);
			JSONObject jsonObj = Common.getJsonObj(sb);
		
			// 구별자 받아오기
			String regCmd = (String)jsonObj.get("target");
			// AdminAdDAO에 변환한 regCmd를 넣어줌 
			AdminAdDAO dao = new AdminAdDAO();
			dao.adminAdDelete(regCmd);
			
			PrintWriter out = response.getWriter();
			
			// 삭제 후 select list를 불러줌 
			List<AdminAdVO> list = dao.AdSelect();
			JSONArray adArray = new JSONArray();
			
			// 삭제 후 리스트틀 다시 보내줌 
			for(AdminAdVO e : list) {
				JSONObject adInfo = new JSONObject();
				   	 adInfo.put("ad_num", e.getAd_num());
			         adInfo.put("ad_name", e.getAd_name());
			         adInfo.put("ad_url", e.getAd_url());
			         adInfo.put("ad_img", e.getAd_img());
			         adArray.add(adInfo);
		}
			out.print(adArray);
	   }
}

