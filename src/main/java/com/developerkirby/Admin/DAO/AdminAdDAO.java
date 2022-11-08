package com.DK.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.DK.admin.common.Common;
import com.DK.admin.vo.AdminAdVO;



public class AdminAdDAO {
	
	 private Connection conn = null;
	 private Statement stmt = null; // 표준 SQL문을 수행하기 위한 Statement 객체 얻기
	 private ResultSet rs = null; // Statement의 수행 결과를 여러 행으로 받음
	   // SQL문을 미리 컴파일해서 재사용하므로 Statement 인터페이스보다 훨씬 빠르게 데이터베이스 작업을 수행
	 private PreparedStatement pstmt = null; 
	
	 public List<AdminAdVO> AdSelect() {
	      List<AdminAdVO> list = new ArrayList<>();
	      try {
	         conn = Common.getConnection();
	         stmt = conn.createStatement();
	         String sql = null;
	         sql = "SELECT * FROM ADMIN_AD ORDER BY AD_NUM ASC";
	         rs = stmt.executeQuery(sql);
	         
	         while(rs.next( )) {
	            int ad_num = rs.getInt("AD_NUM");
	            String ad_name = rs.getString("AD_NAME");
	            String ad_url = rs.getString("AD_URL");
	            String ad_img = rs.getString("AD_IMG");
	            
	            AdminAdVO vo = new AdminAdVO();
	            vo.setAd_num(ad_num);
	            vo.setAd_name(ad_name);
	            vo.setAd_url(ad_url);
	            vo.setAd_img(ad_img);
	            
	            list.add(vo);
	         }
	         Common.close(rs);
	         Common.close(stmt);
	         Common.close(conn);
	      } catch(Exception e) {
	         e.printStackTrace(); // 호출에 대한 메세지 호출. 디버깅 용도.
	      }
	      return list;
	   }
	 
	  public void adminAdDelete(String target) {
		  	// 버튼에 들어온 ad_num값을 ',' 단위로 나눠서 배열을 생성함 
		  	String[] Array = target.split(",");
			// 배열을 하나씩 꺼내오면서 해당 배열의 값을 쿼리문에 넣어줌
			for(String tar : Array) {
			try {
				
				String sql = "DELETE FROM ADMIN_AD WHERE AD_NUM = ?";
				conn = Common.getConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, tar);
				pstmt.executeUpdate();
				
				Common.close(pstmt);
				Common.close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	  }
	  
	  public boolean adminAdUpdate(String ad_num, String ad_name, String ad_url) {
		  String sql = "UPDATE ADMIN_AD SET AD_NAME = ?, AD_URL = ? WHERE AD_NUM = ?";
		  int ad_num_int = Integer.parseInt(ad_num);
		  System.out.println("보낸 UPDATE ad_num 값: " + ad_num_int);
		  try {
			  conn = Common.getConnection();
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, ad_name);
				pstmt.setString(2, ad_url);
				pstmt.setInt(3, ad_num_int); 
				pstmt.executeUpdate();
				
				Common.close(pstmt);
				Common.close(conn);
		  }catch(Exception e) {
			  e.printStackTrace();
		  }
		  return false; // 데이터 베이스 오류
	  }
	  
	  public List<AdminAdVO> AdUpdateInfo(String ad_num) {
	      List<AdminAdVO> list = new ArrayList<>();
	      String sql = "SELECT AD_NAME, AD_URL FROM ADMIN_AD WHERE AD_NUM = ?";
	      
//	      int ad_num_int = Integer.parseInt(ad_num);
		  System.out.println("보낸 UPDATE INFO ad_num 값: " +ad_num);
	      try {
	    	  	
	    	  	conn = Common.getConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, ad_num);
//				pstmt.executeUpdate();
				rs = pstmt.executeQuery();
	         while(rs.next()) {
	            String ad_name = rs.getString("AD_NAME");
	            String ad_url = rs.getString("AD_URL");
//	            String ad_img = rs.getString("AD_IMG");
	            
	            AdminAdVO vo = new AdminAdVO();
	            vo.setAd_name(ad_name);
	            vo.setAd_url(ad_url);
//	            vo.setAd_img(ad_img);
	            
	            list.add(vo);
	         }
	         	Common.close(rs);
				Common.close(pstmt);
				Common.close(conn); 
				
	      } catch(Exception e) {
	         e.printStackTrace(); // 호출에 대한 메세지 호출. 디버깅 용도.
	      }
	      return list;
	   }
	  public boolean adminAdAdd(String ad_name, String ad_url) {
		  String sql = "INSERT INTO ADMIN_AD(AD_NUM, AD_NAME, AD_URL) VALUES (AD_NUM.NEXTVAL, ?, ?)";
		  try {
			  conn = Common.getConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, ad_name);
				pstmt.setString(2, ad_url);
				pstmt.executeUpdate();
				
				Common.close(pstmt);
				Common.close(conn);
		  }catch(Exception e) {
			  e.printStackTrace();
		  }
		  return false; // 데이터 베이스 오류
	  }
	  public List<AdminAdVO> AdNotiSend1() {
	      List<AdminAdVO> list1 = new ArrayList<>();
	      try {
	         conn = Common.getConnection();
	         stmt = conn.createStatement();
	         String sql = null;
	         sql = "SELECT NICKNAME, EMAIL FROM MEMBER WHERE MEMBER_NUM != 1";
	         rs = stmt.executeQuery(sql);
	         
	         while(rs.next()) {
	            String nickName = rs.getString("NICKNAME");
	            String email = rs.getString("EMAIL");
	            
	            AdminAdVO vo = new AdminAdVO();
	            vo.setNickName(nickName);
	            vo.setEmail(email);
	            
	            list1.add(vo);
	         }
	         Common.close(rs);
	         Common.close(stmt);
	         Common.close(conn);
	      } catch(Exception e) {
	         e.printStackTrace(); // 호출에 대한 메세지 호출. 디버깅 용도.
	      }
	      return list1;
	   }
	  public List<AdminAdVO> AdNotiSend2() {
	      List<AdminAdVO> list = new ArrayList<>();
	      try {
	         conn = Common.getConnection();
	         stmt = conn.createStatement();
	         String sql = null;
	         sql = "SELECT NICKNAME, EMAIL FROM MEMBER WHERE IS_ADOK = 'Y' AND MEMBER_NUM != 1";
	         rs = stmt.executeQuery(sql);
	         
	         while(rs.next( )) {
		            String nickName = rs.getString("NICKNAME");
		            String email = rs.getString("EMAIL");
		            
		            AdminAdVO vo = new AdminAdVO();
		            vo.setNickName(nickName);
		            vo.setEmail(email);
		            
		            list.add(vo);
		         }
	         Common.close(rs);
	         Common.close(stmt);
	         Common.close(conn);
	      } catch(Exception e) {
	         e.printStackTrace(); // 호출에 대한 메세지 호출. 디버깅 용도.
	      }
	      return list;
	   }
			
}