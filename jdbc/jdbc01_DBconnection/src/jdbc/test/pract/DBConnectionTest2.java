package jdbc.test.pract;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.naming.ldap.PagedResultsControl;

/*
 * 디비 서버 정보에 해당하는 것들을 상수값으로 세팅
 * 1. 드라이버,FQCN --이 부분이 먼저 메모리에 로딩된 다음에 나머지 일들이 일어나야 한다.
 * 2. 서버주소, 계정이름, 비번...
 */

public class DBConnectionTest2 {

	public static String DRIVER = "com.mysql.cj.jdbc.Driver";
	public static String URL = "jdbc:mysql://127.0.0.1:3306/scott?serverTimezone=UTC&useUnicode=yes&characterEncoding=UTF-8";
	public static String USER = "root";
	public static String PASSWORD = "1234";
	
	public static void main(String[] args) {
		//jdbc 기본작업진행...2,3,4단계 작업을 진행
		try {
			//2.
//			Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
//			System.out.println("디비 연결 성공");
//			
//			//3.
//			String query = "insert into mytable(num,name,address) values(?,?,?)";
//			PreparedStatement ps = conn.prepareStatement(query);
//			System.out.println("PreparedStatement 실행");
//			
//			//4.일단 바인드화
//			ps.setInt(1, 111);
//			ps.setString(2, "박나래");
//			ps.setString(3, "방배동");
//			System.out.println(ps.executeUpdate() + "명 추가되었습니다.");
			
			//2.
			Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
			System.out.println("디비 연결 성공");
			
			//3.
			String query = "delete from mytable where name = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			
			//4.
			ps.setString(1, "정우성");
			System.out.println(ps.executeUpdate() + "명 삭제됨!!!");
		}catch(Exception e) {
			System.out.println("디비 연결 실패");
		}

	}
	static {
		try {
			Class.forName(DRIVER);
			System.out.println("Driver 생성");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver 생성 X");
		}
	}
}
