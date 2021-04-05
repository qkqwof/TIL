## MVC패턴+ DataSource로 DB연결

**MVC패턴**은 비즈니스 로직이 들어가는 DAO와 VO(**Model**), Servlet을 통한 요청과 응답(**Controller**), jsp파일로 브라우저에 결과를 보여주는 페이지(**View**)이다.

![mvc패턴](https://user-images.githubusercontent.com/76687078/113566188-2e62bf80-9647-11eb-98ce-d27249aaa971.jpg)

전체적인 아키텍쳐를 그려보면 위에 있는 그림의 형태를 보여준다.


``` html
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
#wrap {
	text-align: center;
	border: 2px dotted green;
}
</style>
</head>
<body>
<div id = "wrap">
	<h2>DB 연동으로 Cafe Member</h2><p>
		<a href="register.html">회원 가입 하기</a><p>
		<a href="search_member.html">회원 검색 하기</a><p>
		<a href="ALLMember">전체 회원 명단 보기</a><p>	
</div>
</body>
</html>
```

코드로 보게 되면 회원가입, 회원검색, 전체회원 명단을 보는데 각각의 링크로 이동할 수 있게 된다. 

그 후에 기존에는 데이터베이스 연결을 DriverManager로 하였는데 이는 계속되는 연결 요청으로 과부하를 가져올 수 있기 때문에 **DataSource방식**으로 연결할 것이다.

따라서 따로 만든 xml파일에 디비에 대한 정보를 넣고, was에게 넘긴다.

xml에서는 외부이름 접근법과 내부이름 접근법 이렇게 2가지의 방법이 있는데 여기서는 외부 접근이니까 lookup에 추가적인 이름을 적어줘야 한다.

DataSource방식은 기존에 하나 하나씩 Connection을 주는 것이 아니라 각각의 Connection들을 Resourcefactory라는 공간에 넣는다. 

``` java
package servlet.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import javax.sql.DataSource;

import config.ServerInfo;

public class MemberDAOImpl implements MemberDAO{
	//추가
	private DataSource ds;
	//싱글톤...
	private static MemberDAOImpl dao = new MemberDAOImpl();
	//DataSource방식...
	private MemberDAOImpl() {
		//Naming Service...JNDI Service...이름으로 객체에 해당하는 주소값 받아온다..
		try {
		InitialContext ic = new InitialContext();
		ds = (DataSource)ic.lookup("java:comp/env/jdbc/mysql");
		System.out.println("DataSource Lookup...Fail...");
		}catch(NamingException e) {
			
		}
	}

	public static MemberDAOImpl getInstance() {
		return dao;
	}
	@Override
	public Connection getConnection() throws SQLException {		
		System.out.println("디비연결 성공....");
		return ds.getConnection(); //인자값은 이미 Context.xml에 다 들어가있기 때문에 
	}

	@Override
	public void closeAll(PreparedStatement ps, Connection conn) throws SQLException{
		if(ps!=null) ps.close();		
		if(conn != null) conn.close();
	}

	@Override
	public void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) throws SQLException{		
		if(rs != null)	rs.close();
		closeAll(ps, conn);		
	}

	@Override
	public void registerMember(MemberVO vo) throws SQLException {		
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			conn=  getConnection();
			String query = "INSERT INTO member VALUES(?,?,?,?)";
			ps = conn.prepareStatement(query);
			System.out.println("PreparedStatement 생성됨...registerMember");
			
			ps.setString(1, vo.getId());
			ps.setString(2, vo.getPassword());
			ps.setString(3, vo.getName());
			ps.setString(4, vo.getAddress());
			
			System.out.println(ps.executeUpdate()+" row INSERT OK!!");
		}finally{
			closeAll(ps, conn);
		}
	}

	@Override
	public ArrayList<MemberVO> showAllMember() throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<MemberVO> list = new ArrayList<>();
		try {
			conn = getConnection();
			String query = "select id,password,name,address from member";
			ps = conn.prepareStatement(query);
			System.out.println("PreparedStatement....showAllMemeber()");
			
			rs = ps.executeQuery();
			while(rs.next()) {
				list.add(new MemberVO(
						rs.getString("id"),
						rs.getString("password"),
						rs.getString("name"),
						rs.getString("address")
						));
			}
		}finally {
			closeAll(rs, ps, conn);
		}
		return list;
	}

	@Override
	public MemberVO findByIdMember(String id) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MemberVO vo = null;
		try{
			conn = getConnection();
			String query = "select id,password,name,address from member where id=?";
			ps = conn.prepareStatement(query);
			System.out.println("PreparedStatement....findByIdMember()");
			
			ps.setString(1, id);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				vo = new MemberVO(id,
								  rs.getString("password"),
								  rs.getString("name"),
								  rs.getString("address"));
						
			}
		}finally{
			closeAll(rs, ps, conn);
		}
		return vo;
	}	

	
	public static void main(String[] args) throws Exception{
		MemberDAOImpl dao=MemberDAOImpl.getInstance();
		//MemberVO VO = dao.registerMember("encore");
		//System.out.println(VO);

		ArrayList<MemberVO>list = dao.showAllMember();
		System.out.println(list);
}
}
```

우선 코드를 먼저 보여주었는데 Resourcefactory가 DAO에 바로 연결되는 것이 아니라 was 안에 이름으로 객체에 해당하는 주소값을 받아오는 JNDI(Naming Service)가 factory와 연결이 되고 그렇게 또 DAO로 연결되는 형태이다.즉, 순서가 
**Naming Service-> Resourcefactory -> DAO**
이렇게 된다. 
이러한 작업은 servlet, 별도의 jsp로 lookup() 이용, DAO로 해서 연결이 된다.

``` html
<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.naming.Context"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>ConnectionPool....DataSource Test</h2>
<%
Connection conn = null;
//1. Naming Service를 통해서 DataSource(type)를 찾아온다.
//2. javax.naming.Context의 lookup() 기능이 이 떄 사용되어진다.

Context ic = new InitialContext();
DataSource ds = (DataSource)ic.lookup("java:comp/env/jdbc/mysql");
out.println("<b>DataSource Lookup...<br>");

conn=ds.getConnection();
out.println("Connection...OK...RETURN</b>");
%>
</body>
</html>
```

이렇게 connection을 사용하고 다시 Resoursefactory로 반환된다. 여기서 중요한 점은 connection.close()...연결을 닫아주지 않으면 요청이 많아지게 되고 계속해서 요청이 된다면 DB와 연결이 끊기게 될 수도 있다.

앞에서 3가지의 기능을 만들었다는 것은 3개의 서블릿을 갖는다는 것을 의미한다. 각각의 코드를 살펴보면 

``` java
package servlet.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servlet.model.MemberDAOImpl;
import servlet.model.MemberVO;

@WebServlet("/RMS")
public class RegisterMemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//로직은 여기서 작성...
		/*
		 * 1. 폼값 받아서
		 * 2. pvo 생성
		 * 3. DAO 리턴 받아서
		 * 4. 비즈니스 로직 호출
		 * 5. 결과값...바인딩(Attribute 잘 선택)
		 * 6. 페이지 이동(네비게이션)
		 * ---> Controller 역할
		 */
		
		//1.
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		
		//2.
		MemberVO pvo = new MemberVO(id,password,name,address);
		
		//3,4,(5),6.
		try {
			MemberDAOImpl.getInstance().registerMember(pvo);
			
			//request.getRequestDispatcher("ALLMember").forward(request, response);//forward??
			response.sendRedirect("ALLMember");//redirect?
		}catch(SQLException e) {
			
		}
	}
}
```

*회원등록 관련 Servlet*

``` java
package servlet.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servlet.model.MemberDAOImpl;
import servlet.model.MemberVO;

@WebServlet("/SMS")
public class SearchMemberSerlvet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String path = "find_fail.jsp";
		try {
			MemberVO rvo=MemberDAOImpl.getInstance().findByIdMember(id);
			if(rvo!=null) {
				request.setAttribute("vo", rvo);
				path = "find_ok.jsp";
			}
		}catch(SQLException e) {
			
		}
		
		//네비게이션...
		request.getRequestDispatcher(path).forward(request, response);
	}
}
```

*회원 검색 기능*

``` java
package servlet.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servlet.model.MemberDAOImpl;
import servlet.model.MemberVO;

@WebServlet("/AllMember")
public class AllMemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 폼값?...없음...
		 * DAO리턴 받아서 비즈니스 로직 호출
		 * 결과값 바인딩
		 * 네비게이션
		 */
		try{
			ArrayList<MemberVO> list=MemberDAOImpl.getInstance().showAllMember();
			request.setAttribute("list", list);
			
			request.getRequestDispatcher("allView.jsp").forward(request, response);
		}catch(SQLException e) {
			
		}
	}
}
```

*모든 회원보기 기능*

물론 위에 그림에서는 성공 또는 실패 시 페이지를 연결해주는 jsp 등등이 있지만 큰 틀에서 봤을 때 유기적인 모습을 보여주고자 여기까지만 코드를 옮겨봤다.