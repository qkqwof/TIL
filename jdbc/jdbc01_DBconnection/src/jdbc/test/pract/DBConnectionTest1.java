package jdbc.test.pract;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * 1. 서버와 연결하기 위해 Driver를 메모리에 로딩
 * 2. 디비 서버와 연결하기(연결에 성공하면 Connection 객체가 반환)
 * 3. PreparedStatement를 반환 받음(2번에서 생성된 connection이 그 기능을 가짐)
 * 4. executeQuery()를 이용해서 SQL문 실행(3번에서 생성된 ps의 기능...)
 */

public class DBConnectionTest1 {

	public static void main(String[] args) {
		
		try {
			//1.드라이버 로딩
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver Loading Success!!");
			
			//2.디비서버와 연결
			String url = "jdbc:mysql://127.0.0.1:3306/scott?serverTimezone=UTC&useUnicode=yes&characterEncoding=UTF-8";
			Connection conn = DriverManager.getConnection(url,"root","1234");
			System.out.println("DB Server Connection..ok");
			
			//3.PreparedStatement 실행
			String sql = "select * from myscott";
			PreparedStatement ps = conn.prepareStatement(sql);
			System.out.println("PreparedStatement...Creating..");
			
			//4.SQL문 실행...executeQuery()
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				System.out.println(rs.getString(1) + "," + rs.getInt(2));
			}
			
		} catch (ClassNotFoundException e) {
			System.out.println("Driver Loading Fail!!");
		}catch(SQLException e) {
			System.out.println("DB Server Connection..fail..");
		}
		

	}

}
