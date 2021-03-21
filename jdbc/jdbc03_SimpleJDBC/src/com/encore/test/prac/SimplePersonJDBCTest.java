package com.encore.test.prac;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.encore.vo.Person;

import config.ServerInfo;

/*
 * DAO + Test
 * JDBC 4단계(드라이버 로딩은 제외)
 * 2. 디비연결
 * 3. PreparedStatement 생성
 * 4. 쿼리문 실행 및 바인딩
 * 5. 자원 닫기
 * ---> 이 중에서 메소드마다 고정적으로 바뀌지 않고 로직이 작성되는 부분은 2와 5번
 * 	    이런 부분은 위에다 메소드를 새롭게 정의하고
 * 	    메소드마다 호출해서 재사용하는 방법으로 로직을 다시 작성
 */

public class SimplePersonJDBCTest {
	public SimplePersonJDBCTest() throws ClassNotFoundException, SQLException{
		//1.드라이버 로딩
		Class.forName(ServerInfo.DRIVER);
		System.out.println("Driver Loading...");
	}
	
	//고정적으로 반복되는 부분을 공통적인 로직으로 정의
	public Connection getConnection() throws SQLException{
		Connection conn = DriverManager.getConnection(ServerInfo.URL,ServerInfo.USER,ServerInfo.PASSWORD);
		System.out.println("디비 서버 연결...");
		return conn;
	}
	
	public void closeAll(PreparedStatement ps, Connection conn) throws SQLException{
		if(ps!=null)ps.close();
		if(conn!=null)conn.close();
	}
	
	public void closeAll(ResultSet rs,PreparedStatement ps, Connection conn) throws SQLException{
		if(rs!=null) rs.close();
		closeAll(ps,conn);
	}
	
	//비즈니스 로직(DB Access하는 로직) 정의
	public void addPerson(Person p) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		
		conn = getConnection();
		
		String query = "insert into person values(?,?,?)";
		ps = conn.prepareStatement(query);
		System.out.println("PreparedStatement 객체 생성");
		
		ps.setInt(1, p.getSsn());
		ps.setString(2, p.getName());
		ps.setString(3, p.getAdress());
		
		int row = ps.executeUpdate();
		System.out.println(row + "명 추가되었습니다.");
		
		closeAll(ps,conn);
	}
	
	public void deletePerson(int ssn) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		
		conn = getConnection();
		
		String query = "delete from person where ssn = ?";
		ps = conn.prepareStatement(query);
		System.out.println("PreparedStatement 객체 생성...");
		
		ps.setInt(1, ssn);
		System.out.println(ps.executeUpdate() + "명 삭제되었습니다.");
		
		closeAll(ps, conn);	
	}
	
	public void updatePerson(Person p) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		
		conn = getConnection();
		
		String query = "update Person set name=?,address=? where ssn=?";
		ps = conn.prepareStatement(query);
		System.out.println("PreparedStatement 객체 생성...");
		
		ps.setString(1, p.getName());
		ps.setString(2, p.getAdress());
		ps.setInt(3, p.getSsn());
		
		System.out.println(ps.executeUpdate() + "명의 정보가 바뀌었습니다.");
		
		closeAll(ps, conn);
	}
	
	//select...conn,ps,rs..close?
	public ArrayList<Person> findAllPerson() throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		ArrayList<Person> list = new ArrayList<Person>();
		
		conn = getConnection();
		
		String query = "select * from Person";
		ps = conn.prepareStatement(query);
		System.out.println("PreparedStatement 객체 생성...");
		
		rs = ps.executeQuery();
		while(rs.next()) {
			list.add(new Person(rs.getInt("ssn"),
					rs.getString("name"),
					rs.getString("address")));
		}
		closeAll(rs,ps,conn);
		return list;
	}
	public static void main(String[] args) throws ClassNotFoundException,Exception{
		SimplePersonJDBCTest jdbc = new SimplePersonJDBCTest();
		jdbc.addPerson(new Person(333,"Tomas","Canada"));
		jdbc.deletePerson(222);
		jdbc.updatePerson(new Person(333,"Tomson","Brandon"));
		ArrayList<Person>returnList = jdbc.findAllPerson();
		for(Person p:returnList)System.out.println(p);
	}

}
