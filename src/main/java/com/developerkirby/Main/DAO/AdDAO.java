package com.developerkirby.Main.DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.developerkirby.Main.Common;
import com.developerkirby.Main.VO.AdVO;

public class AdDAO {
	
	 private Connection conn = null;
	 private Statement stmt = null; // 표준 SQL문을 수행하기 위한 Statement 객체 얻기
	 private ResultSet rs = null; // Statement의 수행 결과를 여러 행으로 받음
	   // SQL문을 미리 컴파일해서 재사용하므로 Statement 인터페이스보다 훨씬 빠르게 데이터베이스 작업을 수행
	 private PreparedStatement pstmt = null; 
	
	 public List<AdVO> AdSelect() {
	      List<AdVO> list = new ArrayList<>();
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
	            
	            AdVO vo = new AdVO();
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
	 
}