package com.developerkirby.Main.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.developerkirby.Main.Common;
import com.developerkirby.Main.VO.WriteVO;


public class WriteDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	// 게시판 게시글 목록 서블릿
	public List<WriteVO> writeListSelect(String reqBoardName, String offsetNum, String limitNum) {
		List<WriteVO> list = new ArrayList<>();
        String sql = "SELECT * FROM (SELECT ROWNUM NUM, TEMP.* FROM (SELECT DISTINCT COUNT(*) \"좋아요수\", W.WRITE_NUM \"게시글번호\", BOARD_NAME \"게시판명\",\r\n"
        		+ "WRITE_NAME \"게시글명\", NICKNAME \"글작성자\", \r\n"
        		+ "WRITE_DATE \"작성일\", WRITE_CONTENTS \"글내용\",\r\n"
        		+ "(SELECT COUNT(*) FROM COMMENTS C WHERE C.WRITE_NUM = W.WRITE_NUM) \"댓글수\"\r\n"
        		+ "FROM MEMBER M, (SELECT * FROM WRITE WHERE BOARD_NAME = ?) W, (SELECT DISTINCT * FROM GOOD) G\r\n"
        		+ "WHERE M.MEMBER_NUM = W.MEMBER_NUM AND G.WRITE_NUM = W.WRITE_NUM\r\n"
        		+ "GROUP BY W.WRITE_NUM, BOARD_NAME, WRITE_NAME, NICKNAME, WRITE_DATE, WRITE_CONTENTS\r\n"
        		+ "ORDER BY WRITE_DATE DESC) TEMP)\r\n"
        		+ "WHERE NUM <= ? AND NUM > ?";
	    try {
	        conn = Common.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        System.out.println(offsetNum);
	        pstmt.setString(1, reqBoardName);
	        pstmt.setString(2, limitNum);
	        pstmt.setString(3, offsetNum);
	        rs = pstmt.executeQuery(); 
	        
	        while(rs.next()) {
	        	String writeName = rs.getString("게시글명");
	        	String writeContents = rs.getString("글내용");
	            Date writeDate = rs.getDate("작성일");
	            SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
	            String dateToStr = sdf.format(writeDate);
	        	String nickName = rs.getString("글작성자");
	        	int countGood = rs.getInt("좋아요수");
	        	int countComment = rs.getInt("댓글수");
	        	int writeNum = rs.getInt("게시글번호");
	        	String[] writeArray = writeContents.split("\n");
	        	String wrtieContents1="";
	        	if(writeArray.length==1) {
	        		wrtieContents1 = writeArray[0].substring(0,300);
	        	} else if(writeArray.length == 2) {
	        		wrtieContents1 = writeArray[0] + "\n" + writeArray[1];
	        	} else if (writeArray.length == 3) {
	        		wrtieContents1 = writeArray[0] +"\n" + writeArray[1] + "\n" + writeArray[2];
	        	}
	        	if(writeArray.length>3) {
	        		wrtieContents1 = writeArray[0] +"\n" + writeArray[1] + "\n" + writeArray[2] + "\n...";
	        	}
	        	
	            WriteVO vo = new WriteVO();
	            vo.setWriteName(writeName);
	            vo.setWriteContents(wrtieContents1);
	            vo.setWriteDateStr(dateToStr);
	            vo.setNickname(nickName);
	            vo.setCountGood(countGood);
	            vo.setCountComment(countComment);
	            vo.setWriteNum(writeNum);
	            list.add(vo);
	        }
	        
	        Common.close(rs);
	        Common.close(stmt);
	        Common.close(conn);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	
	public List<WriteVO> writeDetailSelect(int reqWriteNum) {
		List<WriteVO> list = new ArrayList<>();
		String sql = "SELECT COUNT(*) \"좋아요수\", W.WRITE_NUM \"게시글번호\", BOARD_NAME \"게시판명\", \r\n"
				+ "WRITE_NAME \"게시글명\", NICKNAME \"글작성자\", \r\n"
				+ "WRITE_DATE \"작성일\", WRITE_CONTENTS \"글내용\", M.PF_IMG \"프로필\",\r\n"
				+ "(SELECT COUNT(*) FROM COMMENTS C WHERE C.WRITE_NUM = W.WRITE_NUM) \"댓글수\"\r\n"
				+ "FROM MEMBER M, WRITE W, (SELECT DISTINCT * FROM GOOD) G\r\n"
				+ "WHERE M.MEMBER_NUM = W.MEMBER_NUM AND G.WRITE_NUM = W.WRITE_NUM AND W.WRITE_NUM = ?\r\n"
				+ "GROUP BY W.WRITE_NUM, BOARD_NAME, WRITE_NAME, NICKNAME, WRITE_DATE, WRITE_CONTENTS, M.PF_IMG";
		
		try {
			conn = Common.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reqWriteNum);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String boardName = rs.getString("게시판명");
				String writeName = rs.getString("게시글명");
				String pfImg = rs.getString("프로필");
				Date writeDate = rs.getDate("작성일");
                SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
                String dateToStr = sdf.format(writeDate);
                String nickname = rs.getString("글작성자");
                String writeContent = rs.getString("글내용");
                int countGood = rs.getInt("좋아요수");
                int countComments = rs.getInt("댓글수");
                
                WriteVO vo = new WriteVO();
                vo.setBoardName(boardName);
                vo.setWriteName(writeName);
                vo.setPfImg(pfImg);
                vo.setWriteDateStr(dateToStr);
                vo.setNickname(nickname);
                vo.setWriteContents(writeContent);
                vo.setCountGood(countGood);
                vo.setCountComment(countComments);
                list.add(vo);
			}
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void deleteWrite(String writeNum) {
		try {
			String sql = "DELETE FROM WRITE WHERE WRITE_NUM = ?";
			conn = Common.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, writeNum);
			pstmt.executeUpdate();
//			System.out.println(cnt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Common.close(pstmt);
		Common.close(conn);
//		return cnt;
	}
	
	// 게시판 게시글 목록 서블릿
		public List<WriteVO> writeSearchSelect(String query, String offsetNum, String limitNum) {
			List<WriteVO> list = new ArrayList<>();
	        String sql = "SELECT * FROM (SELECT ROWNUM NUM, TEMP.* FROM (SELECT DISTINCT COUNT(*) \"좋아요수\", W.WRITE_NUM \"게시글번호\", BOARD_NAME \"게시판명\",\r\n"
	        		+ "WRITE_NAME \"게시글명\", NICKNAME \"글작성자\", \r\n"
	        		+ "WRITE_DATE \"작성일\", WRITE_CONTENTS \"글내용\",\r\n"
	        		+ "(SELECT COUNT(*) FROM COMMENTS C WHERE C.WRITE_NUM = W.WRITE_NUM) \"댓글수\"\r\n"
	        		+ "FROM MEMBER M, WRITE W , (SELECT DISTINCT * FROM GOOD) G\r\n"
	        		+ "WHERE M.MEMBER_NUM = W.MEMBER_NUM AND G.WRITE_NUM = W.WRITE_NUM\r\n"
	        		+ "GROUP BY W.WRITE_NUM, BOARD_NAME, WRITE_NAME, NICKNAME, WRITE_DATE, WRITE_CONTENTS\r\n"
	        		+ "ORDER BY WRITE_DATE DESC) TEMP \r\n"
	        		+ "WHERE \"글작성자\" LIKE ? OR \"게시글명\" LIKE ? OR \"글내용\" LIKE ?) \r\n"
	        		+ "WHERE NUM <= ? AND NUM > ?";
		    try {
		        conn = Common.getConnection();
		        pstmt = conn.prepareStatement(sql);
		        System.out.println(offsetNum);
		        pstmt.setString(1, '%'+query+'%');
		        pstmt.setString(2, '%'+query+'%');
		        pstmt.setString(3, '%'+query+'%');
		        pstmt.setString(4, limitNum);
		        pstmt.setString(5, offsetNum);
		        rs = pstmt.executeQuery(); 
		        
		        while(rs.next()) {
		        	String writeName = rs.getString("게시글명");
		        	String writeContents = rs.getString("글내용");
		            Date writeDate = rs.getDate("작성일");
		            SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
		            String dateToStr = sdf.format(writeDate);
		        	String nickName = rs.getString("글작성자");
		        	int countGood = rs.getInt("좋아요수");
		        	int countComment = rs.getInt("댓글수");
		        	int writeNum = rs.getInt("게시글번호");
		            
		        	String[] writeArray = writeContents.split("\n");
		        	String wrtieContents1="";
		        	if(writeArray.length==1) {
		        		wrtieContents1 = writeArray[0].substring(0,300);
		        	} else if(writeArray.length == 2) {
		        		wrtieContents1 = writeArray[0] + "\n" + writeArray[1];
		        	} else if (writeArray.length == 3) {
		        		wrtieContents1 = writeArray[0] +"\n" + writeArray[1] + "\n" + writeArray[2];
		        	}
		        	if(writeArray.length>3) {
		        		wrtieContents1 = writeArray[0] +"\n" + writeArray[1] + "\n" + writeArray[2] + "\n...";
		        	}
		        	
		            WriteVO vo = new WriteVO();
		            vo.setWriteName(writeName);
		            vo.setWriteContents(wrtieContents1);
		            vo.setWriteDateStr(dateToStr);
		            vo.setNickname(nickName);
		            vo.setCountGood(countGood);
		            vo.setCountComment(countComment);
		            vo.setWriteNum(writeNum);
		            list.add(vo);
		        }
		        Common.close(rs);
		        Common.close(stmt);
		        Common.close(conn);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return list;
		}
		
		// 내 게시글 목록 서블릿
		public List<WriteVO> myWriteSelect(String reqBoardName, String offsetNum, String limitNum) {
			List<WriteVO> list = new ArrayList<>();
	        String sql = "SELECT * FROM (SELECT ROWNUM NUM, TEMP.* FROM (SELECT DISTINCT COUNT(*) \"좋아요수\", W.WRITE_NUM \"게시글번호\", BOARD_NAME \"게시판명\",\r\n"
	        		+ "WRITE_NAME \"게시글명\", NICKNAME \"글작성자\", \r\n"
	        		+ "WRITE_DATE \"작성일\", WRITE_CONTENTS \"글내용\",\r\n"
	        		+ "(SELECT COUNT(*) FROM COMMENTS C WHERE C.WRITE_NUM = W.WRITE_NUM) \"댓글수\"\r\n"
	        		+ "FROM MEMBER M, (SELECT * FROM WRITE WHERE BOARD_NAME = ?) W, (SELECT DISTINCT * FROM GOOD) G\r\n"
	        		+ "WHERE M.MEMBER_NUM = W.MEMBER_NUM AND G.WRITE_NUM = W.WRITE_NUM\r\n"
	        		+ "GROUP BY W.WRITE_NUM, BOARD_NAME, WRITE_NAME, NICKNAME, WRITE_DATE, WRITE_CONTENTS\r\n"
	        		+ "ORDER BY WRITE_DATE DESC) TEMP)\r\n"
	        		+ "WHERE NUM <= ? AND NUM > ?";
		    try {
		        conn = Common.getConnection();
		        pstmt = conn.prepareStatement(sql);
		        System.out.println(offsetNum);
		        pstmt.setString(1, reqBoardName);
		        pstmt.setString(2, limitNum);
		        pstmt.setString(3, offsetNum);
		        rs = pstmt.executeQuery(); 
		        
		        while(rs.next()) {
		        	String writeName = rs.getString("게시글명");
		        	String writeContents = rs.getString("글내용");
		            Date writeDate = rs.getDate("작성일");
		            SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
		            String dateToStr = sdf.format(writeDate);
		        	String nickName = rs.getString("글작성자");
		        	int countGood = rs.getInt("좋아요수");
		        	int countComment = rs.getInt("댓글수");
		        	int writeNum = rs.getInt("게시글번호");
		        	String[] writeArray = writeContents.split("\n");
		        	String wrtieContents1="";
		        	if(writeArray.length==1) {
		        		wrtieContents1 = writeArray[0].substring(0,300);
		        	} else if(writeArray.length == 2) {
		        		wrtieContents1 = writeArray[0] + "\n" + writeArray[1];
		        	} else if (writeArray.length == 3) {
		        		wrtieContents1 = writeArray[0] +"\n" + writeArray[1] + "\n" + writeArray[2];
		        	}
		        	if(writeArray.length>3) {
		        		wrtieContents1 = writeArray[0] +"\n" + writeArray[1] + "\n" + writeArray[2] + "\n...";
		        	}
		        	
		            WriteVO vo = new WriteVO();
		            vo.setWriteName(writeName);
		            vo.setWriteContents(wrtieContents1);
		            vo.setWriteDateStr(dateToStr);
		            vo.setNickname(nickName);
		            vo.setCountGood(countGood);
		            vo.setCountComment(countComment);
		            vo.setWriteNum(writeNum);
		            list.add(vo);
		        }
		        Common.close(rs);
		        Common.close(stmt);
		        Common.close(conn);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return list;
		}
		
		// 내가 댓글 단 게시물
		public List<WriteVO> commentWriteListSelect(int reqmemberNum, String offsetNum, String limitNum) {
			List<WriteVO> list = new ArrayList<>();
	        String sql = "SELECT * FROM (SELECT ROWNUM NUM, TEMP.* FROM \r\n"
	        		+ "(SELECT DISTINCT * FROM (SELECT \r\n"
	        		+ "WRITE_NAME \"게시글명\", \r\n"
	        		+ "WRITE_CONTENTS \"게시글내용\",\r\n"
	        		+ "COMMENT_CONTENT \"댓글내용\",\r\n"
	        		+ "W.WRITE_NUM \"게시글번호\", \r\n"
	        		+ "W.WRITE_DATE \"작성일\",\r\n"
	        		+ "C.MEMBER_NUM \"댓글작성자\",\r\n"
	        		+ "C.WRITE_DATE \"댓글작성일\",\r\n"
	        		+ "(SELECT NICKNAME FROM MEMBER M WHERE W.MEMBER_NUM = M.MEMBER_NUM) \"원글작성자\",\r\n"
	        		+ "(SELECT COUNT(*) FROM COMMENTS C WHERE C.WRITE_NUM = W.WRITE_NUM) \"댓글수\",\r\n"
	        		+ "(SELECT COUNT(*) FROM GOOD G WHERE G.WRITE_NUM = W.WRITE_NUM GROUP BY M.MEMBER_NUM) \"좋아요수\"\r\n"
	        		+ "FROM COMMENTS C, MEMBER M, WRITE W, (SELECT DISTINCT * FROM GOOD) G\r\n"
	        		+ "WHERE C.WRITE_NUM = W.WRITE_NUM AND C.MEMBER_NUM = M.MEMBER_NUM \r\n"
	        		+ ") WHERE \"댓글작성자\" = ? ORDER BY \"댓글작성일\" DESC) TEMP) WHERE NUM <= ? AND NUM > ?";
		    try {
		        conn = Common.getConnection();
		        pstmt = conn.prepareStatement(sql);
		        
		        System.out.println(offsetNum);
		        
		        pstmt.setInt(1, reqmemberNum);
		        pstmt.setString(2, limitNum);
		        pstmt.setString(3, offsetNum);
		        rs = pstmt.executeQuery(); 
		        
		        while(rs.next()) {
		        	String writeName = rs.getString("게시글명");
		        	String writeContents = rs.getString("게시글내용");
		            Date writeDate = rs.getDate("작성일");
		            SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
		            String dateToStr = sdf.format(writeDate);
		        	String nickName = rs.getString("원글작성자");
		        	int countGood = rs.getInt("좋아요수");
		        	int countComment = rs.getInt("댓글수");
		        	int writeNum = rs.getInt("게시글번호");
		        	String[] writeArray = writeContents.split("\n");
		        	String wrtieContents1="";
		        	if(writeArray.length==1) {
		        		wrtieContents1 = writeArray[0].substring(0,300);
		        	} else if(writeArray.length == 2) {
		        		wrtieContents1 = writeArray[0] + "\n" + writeArray[1];
		        	} else if (writeArray.length == 3) {
		        		wrtieContents1 = writeArray[0] +"\n" + writeArray[1] + "\n" + writeArray[2];
		        	}
		        	if(writeArray.length>3) {
		        		wrtieContents1 = writeArray[0] +"\n" + writeArray[1] + "\n" + writeArray[2] + "\n...";
		        	}
		        	
		            
		            WriteVO vo = new WriteVO();
		            vo.setWriteName(writeName);
		            vo.setWriteContents(wrtieContents1);
		            vo.setWriteDateStr(dateToStr);
		            vo.setNickname(nickName);
		            vo.setCountGood(countGood);
		            vo.setCountComment(countComment);
		            vo.setWriteNum(writeNum);
		            list.add(vo);
		        }
		        Common.close(rs);
		        Common.close(stmt);
		        Common.close(conn);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return list;
		}
	
}

