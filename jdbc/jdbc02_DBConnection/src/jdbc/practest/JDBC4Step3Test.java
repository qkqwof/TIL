package jdbc.practest;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import config.ServerInfo;

/*
 * 디비 Access 하기 위한 4단계 작업을 작성하는 로직...
 * 1.디비서버에 대한 파편적인 정보들(서버 실제값) --> driver, url, user, password를 상수로 지정
 * 
 * 문제점
 * 소스코드에 서버정보가 그대로 노출되어져 있다.
 * 메타데이터화 시키자
 * 
 * 해결책
 * 2.상수값과 추상메소드를 구성요소로 가지는 인터페이스를 별도의 모듈로 생성하고 그 안에 서버 정보를 메타데이터화 시킴
 * 
 * 3.자바 진영에서 가장 많이 사용하는 Meta Data는 properties 파일이다.
 * 	 key - value 값을 모두 스트링으로 저장할 수 있다는 특징에 착안되어서 서버 정보를 저장하는 메타데이터로 가장 많이 사용
 *   config < db.properties
 */

public class JDBC4Step3Test {
	
	static String driver;
	static String url;
	static String user;
	static String pass;
	static String query;
	
	
	public JDBC4Step3Test() throws ClassNotFoundException, SQLException{
		Class.forName(ServerInfo.DRIVER);
		System.out.println("1.드라이버 로딩 성공...");
		
		Connection conn = DriverManager.getConnection(ServerInfo.URL,ServerInfo.USER,ServerInfo.PASSWORD);
		System.out.println("2.디비 연결 성공..");
		
		//mytable에 있는 모든 정보를 다 가져와서 출력
		String querysel = "select * from mytable";
		PreparedStatement ps4 = conn.prepareStatement(querysel);//이 떄 값이 들어가진 않음
		System.out.println("3.PreparedStatement 생성...");
		
		ResultSet rs = ps4.executeQuery();
		System.out.println("========================================");
		while(rs.next()) {
			System.out.println(rs.getInt("num") + "\t" 
						     + rs.getString("name") + "\t" 
							 + rs.getString("address"));
		}
		
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		new JDBC4Step3Test();
		
		
	}
	static {
		//1. properties 파일의 내용을 로드해옴
		try {
			Properties p = new Properties();
			p.load(new FileInputStream("src/pracconfig/db2.properties"));
			driver = p.getProperty("jdbc.mysql.driver");
			url = p.getProperty("jdbc.mysql.url");
			user = p.getProperty("jdbc.mysql.user");
			pass = p.getProperty("jdbc.mysql.pass");
			query = p.getProperty("jdbc.sql.selectAll");
		}catch(Exception e) {
			
		}
	}
	
}
