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
import com.developerkirby.Main.DAO.MemberDAO;
import com.developerkirby.Main.VO.MemberVO;

import oracle.net.aso.m;

@WebServlet("/MemberServlet")
public class MemberServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
       
    public MemberServlet() {
        super();
    }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      // TODO Auto-generated method stub
      response.getWriter().append("Served at: ").append(request.getContextPath());
   }
   protected void doOptions(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
      response = Common.corsResSet(response);
   }

   @SuppressWarnings("unchecked")
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      request.setCharacterEncoding("utf-8");
      response = Common.corsResSet(response);

      StringBuffer sb = Common.reqStringBuff(request);
      JSONObject jsonObj = Common.getJsonObj(sb);
      
      System.out.println("Command : " + (String)jsonObj.get("cmd"));
      System.out.println("Nickname : " + (String)jsonObj.get("nickname"));
      String reqCmd = (String)jsonObj.get("cmd");
      String reqId = (String)jsonObj.get("nickname");
      System.out.println(reqId);
      
      PrintWriter out = response.getWriter();
      if(!reqCmd.equals("MemberInfo")) {
         JSONObject resJson = new JSONObject();
         resJson.put("result", "NOK");
         out.print(resJson);
      }
      
      MemberDAO dao = new MemberDAO();
      List<MemberVO> list = dao.memberSelect(reqId);
      
      JSONArray memberArray = new JSONArray();
      
      for(MemberVO e : list) {
         JSONObject memberInfo = new JSONObject();
         memberInfo.put("member_num", e.getMemberNum());
         memberInfo.put("nickname", e.getNickname());
         memberInfo.put("pwd", e.getPwd());
         memberInfo.put("email", e.getEmail());
         memberInfo.put("phone", e.getPhone());
         DateFormat dateFormat = new SimpleDateFormat("YYYY/dd/MM HH:mm:ss");
         String dateToStr = dateFormat.format(e.getRegDate());
         memberInfo.put("join", dateToStr);
         memberInfo.put("grade", e.getGrade());
         memberArray.add(memberInfo);
      }   
      out.print(memberArray);
   }
   
}