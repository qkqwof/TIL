## FrontController

![FrontController](https://user-images.githubusercontent.com/76687078/113718183-5cb7cc00-9727-11eb-8fb1-89211de0e5be.jpg)

기존의 MVC 패턴은 Servlet이 너무 많아서 복잡하고, logic이 만들어질 때마다 Servlet이 만들어져서 logic이 많아질수록 무거워지는 문제점이 있다. 

따라서 이런 문제점을 극복할 수 있게 Servlet 하나만으로 Biz logic을 커버할 수 있는 구조인 Front Controller 패턴이 있다.

``` html
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	#wrap{
		text-align: center;
		border: 2px dotted green;
	}
</style>
</head>
<body>
<div id="wrap">
<!--모든 요청을 하나의 서블릿으로 보낸다..front.do -->
	<h2>HttpSession으로 Login 하는  Cafe Member</h2><p>
	<li><a href="register.html">회원 가입 하기</a><p>
	<li><a href="search_member.html">회원 검색 하기</a><p>
	<li><a href="front.do?command=allmember">전체회원 명단보기</a><p>
	<li><a href="login.html">로그인 하기</a><p>
</div>
</body>
</html>
```

여러가지 요청을 서블릿으로 넘길 때 front.do라는 것으로 command라는 파라미터에 값을 넣어서 보낸다.

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
import javax.servlet.http.HttpSession;

import servlet.model.MemberDAOImpl;
import servlet.model.MemberVO;


@WebServlet(urlPatterns = "/front.do", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.가장먼저 해야하는 일은 클라이언트로부터 어떤 요청이 들어오는지를 알아야 한다...command값 받아온다.
		
		//1. RegisterServlet의 doProcess(){ 안에 있는 코드를 직접 여기에 작성...권장하지 않는다...
		//2. method로 하나 뽑아내서 그 메소드 안에서 RegisterServlet의 doProcess(){ 안에 있는 코드를 작성...이 방법을 추천한다.
		String command  =  request.getParameter("command");
		String path = "index.html";//error page, home
		
		if(command.equals("register")){ //회원가입 
			path=register(request,response);
		}else if(command.equals("find")) { //회원검색
			path=find(request,response);
		}else if(command.equals("login")) { //로그인
			path=login(request,response);
		}else if(command.equals("allmember")) { //모든 회원 보기
			path=allmember(request,response);
		}
		
		//모든 메소드가 수행을 한 뒤 다시 이곳으로 돌아온다...
		request.getRequestDispatcher(path).forward(request, response);
	}//doProcess
	
	protected String register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//AllMember--> register_result.jsp
		//로직은 여기서 작성..
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String path="register_fail.jsp";
		//2.
		MemberVO pvo = new MemberVO(id, password, name, address);
		
		//3. 4. (5) 6. 
		try {
			MemberDAOImpl.getInstance().registerMember(pvo);			
			path = "register_result.jsp";	
		}catch(SQLException e) {
			
		}
		
		return path;
	}
	
	protected String find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		
		
		return path;
	}
	protected String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id=  request.getParameter("id");
		String password = request.getParameter("password");
		String path = "";
		try {
			MemberVO rvo=MemberDAOImpl.getInstance().login(id, password);
			
			//세션에 저장해야지만 로그인 동안 인증을 계속 유지할 수 있다.
			HttpSession session = request.getSession();
			if(rvo!=null) {
				session.setAttribute("vo", rvo);
				System.out.println("JSESSIONID :: "+session.getId());				
				path = "login_result.jsp";
			}
		}catch(Exception e) {
			
		}
		return path;
	}
	protected String allmember(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = "";
		try{
			ArrayList<MemberVO> list=MemberDAOImpl.getInstance().showAllMember();
			request.setAttribute("list", list);			
			path = "allView.jsp";
		}catch(SQLException e) {
			
		}
		return path;
	}
}
```

doProcess 메소드에 command라는 변수를 만들고 앞에 html에서 나타낸 command를 파라미터로 받아서 메소드를 호출한다. 기존에 따로 떨어져 있던 하나하나의 logic들을 method로 하나 뽑아내서 그 메서드 안에서 하나의 큰 서블릿의 doProcess() 안에 있는 코드를 작성하여 유지보수성을 높여준다.

이 때, 다른 페이지로 돌려주는 부분은 중복이 되니까 위에서 중복을 처리하게 설정을 해준다. 그 부분이 바로

``` java
request.getRequestDispatcher(path).forward(request, response);
```

이 부분이다. 그렇게 설정을 해주고 doProcess에서 path를 받아 지정된 페이지로 이동하게 된다.

## EL

![EL](https://user-images.githubusercontent.com/76687078/113721787-c9809580-972a-11eb-81f0-e19f16131078.jpg)

그 전까지 결과를 나타내는 페이지인 JSP에서 <% %>를 사용해서 자바 코드를 넣었다. 그리고 Servlet에서는 Tag 기반 언어를 사용했었다. 하지만 원래 Servlet은 logic이 중심이 되므로 Java code로만 작성을 하여야 하고, JSP는 결과를 보여주는 것이 주목적이여서 Tag가 중심이 되면서 java code가 없어야 한다. 

따라서 EL이라는 표현식을 사용해서 JSP에서의 자바 코드들을 없애는 작업을 진행할 것이다. 

``` html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>Servlet...에서 진행되는 코드라고 이해합시다..</h2>
<%
int sum = 0;
for(int cnt = 0; cnt<=50;cnt++) sum += cnt;

request.setAttribute("RESULT", sum);
session.setAttribute("RESULT", "ENCORE");

request.getRequestDispatcher("EL1_result.jsp").forward(request, response);
%>
</body>
</html>
```

위에서는 EL을 확인하기 위해서 간단히 만든 것이라 자바 코드가 들어갔다.

``` html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>Attribute에 바인딩된 내용을 찾아옵니다...</h2>
<b>1~50까지의 합산 결과 1)::</b>
<% request.getAttribute("RESULT"); %><br>
<% session.getAttribute("RESULT"); %>
<hr>
<b>1~50까지의 합산 결과 2) EL :: </b>
${RESULT}<br>
${requestScope.RESULT}<br>
${sessionScope.RESULT}<br>
<hr>
${RESULT}<br>
${RESULT+100000000}<br>
${NAME}<br>
</body>
</html>
```

![el1](https://user-images.githubusercontent.com/76687078/113723348-3d6f6d80-972c-11eb-81a8-b1f938b59e49.png)

출력된 결과를 보면 마지막에 연산 기능을 넣었는데 연산이 되는 것을 알 수 있고, 없는 Attribute 이름을 넣어도 에러가 안나고 출력이 안된다는 것을 볼 수 있다.

무엇보다 알고 있어야 되는 것이 있다. Attibute는 request, session, ServletContext에 포함되어 있다. scope만 다를 뿐이다.
EL에서는 scope를 지정할 수도 있고, 지정을 안할 수도 있는데 지정을 안한다면 Attribute에 바인딩된 이름인 REUSLT가 나올 때까지 탐색을 한다. 이 때, 순서는 request -> session -> ServletContext 순이다.

```html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>Form 값을 EL로 받아오기.</h2>
<form action="EL2_view.jsp" method="post">

ID <input type="text" name="userId"><br/><br/>

<strong>좋아하는 메뉴....</strong><p>
<input type="checkbox" name="menu" value="spagetii">spagetii
<input type="checkbox" name="menu" value="haser">haser
<input type="checkbox" name="menu" value="curry">curry
<input type="checkbox" name="menu" value="noodle">noodle
<br/><br/>

<input type="submit" value="DataSend">
</form>
</body>
</html>
```

``` html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<b>1. jsp 기본 앨러먼트로 폼값 받아오기</b>
ID <%=
	request.getParameter("userId")
%>
<hr>
<b>2. EL로 기본 앨러먼트로 폼값 받아오기</b>
${param.userId}
<hr>
<b>3. jsp 기본 앨러먼트로 checkbox 받아오기</b>
<%
String[] menus = request.getParameterValues("menu");
for(String menu:menus){
%>		
	<%=menu %>
<% 
}
%>
<hr>
<b>2. EL로 checkbox 폼값 받아오기</b>
선택한 메뉴 :: <br>
${paramValues.menu[0]}
${paramValues.menu[1]}
${paramValues.menu[2]}
${paramValues.menu[3]}
</body>
</html>
```

![form값](https://user-images.githubusercontent.com/76687078/113724607-79ef9900-972d-11eb-99e3-54ace9b50c79.png)

![form값2](https://user-images.githubusercontent.com/76687078/113724697-912e8680-972d-11eb-8263-e92968c05d58.png)

form 값을 받을 때, EL 표현으로 하려면은 param.을 붙여준다. 만약에 다중 파라미터 값을 받을 떄는 paramValues.를 쓴다.
다중값에 인덱스를 붙일 수 있는데 이렇게 인덱스로 한 것은 JSTL을 사용하지 않아 조건,반복을 실행할 수 없기 때문이다.

``` html
<%@page import="servlet.model.MemberVO"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%--<%

	MemberVO vo = new MemberVO("11","11","하바리","서현동");
	request.setAttribute("vo", vo);
	request.getRequestDispatcher("EL3_view.jsp").forward(request, response);	
--%>


<%
ArrayList<MemberVO> list = new ArrayList<>();
MemberVO vo1 = new MemberVO("11","11","하바리","서현동");
MemberVO vo2 = new MemberVO("22","22","하바리2","서현동2");
list.add(vo1);  list.add(vo2);

request.setAttribute("list", list);
request.getRequestDispatcher("EL3_view.jsp").forward(request, response);
%>
</body>
</html>
```

``` html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>Attribute에 바인딩된 VO정보를 출력...</h2>
아이디 : ${vo.id}<br>
이름 : ${vo.name}<br>
주소 : ${vo.address}<br>
<hr>
<h2>Attribute에 바인딩된 List정보를 출력...</h2>
아이디 : ${list[0].id}<br>
이름 : ${list[0].name}<br>
주소 : ${list[0].address}<br>
1VO :: ${list[0]}<br>

아이디 : ${list[1].id}<br>
이름 : ${list[1].name}<br>
주소 : ${list[1].address}<br>
1VO :: ${list[1]}<br>
</body>
</html>
```

**1.단일 객체 부분에서 vo라는 이름으로 넘어온 attribute에서 id,name,address를 가져온다. .id부분은 .getId()와 같으며 값을 가져온다는 의미이다.**

**2.객체 리스트 부분에서 2개의 객체를 리스트에 담아 attribute로 바인딩했다. 따라서 리스트의 첫번째 인덱스를 list[0], 두번째 인덱스를 list[1]로 표현하였다.**

![list](https://user-images.githubusercontent.com/76687078/113725812-a7891200-972e-11eb-9305-7635b4b5fdab.png)

단일 결과가 출력 안된것은 el3_Form에서 주석으로 막았다. 왜냐하면 단일, 다중 다 수행될 경우 단일 객체 부분에서 request.getRequestDispatcher가 실행되어 페이지가 넘어가기 때문이다. 