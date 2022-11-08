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


@WebServlet("/AdminUpdateInfoServlet")
public class AdminAdUpdateInfoServlet extends HttpServlet {
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
	     
	      request.setCharacterEncoding("utf-8");
	      Common.corsResSet(response);
	      StringBuffer sb = Common.reqStringBuff(request);
	      JSONObject jsonObj = Common.getJsonObj(sb);
	      
	      String reqCmd = (String)jsonObj.get("cmd");
	      String getAd_num = (String)jsonObj.get("ad_num");
	      
	      PrintWriter out = response.getWriter();

	      AdminAdDAO dao = new AdminAdDAO();
	      System.out.println("전달 받은 번호 : " + getAd_num );
	      	      
	      List<AdminAdVO> list = dao.AdUpdateInfo(getAd_num);
	      

	      JSONObject adminAdUpdateInfo = new JSONObject();
	      for(AdminAdVO e : list) {

	         adminAdUpdateInfo.put("ad_name", e.getAd_name());
	         adminAdUpdateInfo.put("ad_url", e.getAd_url());
	      }
	      out.print(adminAdUpdateInfo);
	   }
}
