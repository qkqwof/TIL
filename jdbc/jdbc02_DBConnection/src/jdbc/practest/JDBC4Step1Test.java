package jdbc.practest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * 디비 Access 하기 위한 4단계 작업을 작성하는 로직...
 * 1.디비서버에 대한 파편적인 정보들(서버 실제값) --> driver, url, user, password를 상수로 지정
 */

public class JDBC4Step1Test {
	public static String DRIVER = "com.mysql.cj.jdbc.Driver";
	public static String URL = "jdbc:mysql://127.0.0.1:3306/scott?serverTimezone=UTC&useUnicode=yes&characterEncoding=UTF-8";
	public static String USER = "root";
	public static String PASSWORD = "1234";
	
	public JDBC4Step1Test() throws ClassNotFoundException,SQLException{
		Class.forName(DRIVER);
		System.out.println("1.드라이버 로딩 성공...");
	
		Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
		System.out.println("2.디비 연결 성공..");
		
		//update문 실행...111인 사람의 이름과 주소를 변경
		
		String queryup = "update mytable set name = ?, address =? where num=?";
		PreparedStatement ps2 = conn.prepareStatement(queryup);
		System.out.println("3.PreparedStatement 생성...");
		
		ps2.setString(1, "정우재");//첫번쨰 물음표 - > name임
		ps2.setString(2, "방배동");
		ps2.setInt(3, 111);
		
		System.out.println(ps2.executeUpdate() + " row update...ok");
		
		//mytable에 있는 모든 정보를 다 가져와서 출력
		String querysel = "select * from mytable";
		PreparedStatement ps = conn.prepareStatement(querysel);
		
		ResultSet rs = ps.executeQuery();
		System.out.println("========================================");
		while(rs.next()) {
			System.out.println(rs.getInt("num") + "\t"
					 		  +rs.getString("name") + "\t"
					 		  +rs.getString("address"));
		}
		
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		new JDBC4Step1Test();
		
		
	}

}
