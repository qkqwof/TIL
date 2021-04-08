## Factory Method 패턴 - Redirect 페이지 이동

기존이 Factory Method는 페이지를 이동할 때, forward방식만으로 하였다. 이번에는 형태를 약간 바꿔서 Redirect와 Forward방식을 선택하여 응답을 보낼 수 있도록 해볼 것이다.

``` html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
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
<h1>CafeMember :: FrontControllerPattern + EL, JSTL </h1>
<%--
jstl -- choose
           when
           
           otherwise
index페이지에서는 클라이언트가 6가지의 서비스를 요청할수 있다.
이때 인증과 권한에 따라서 요청할 수 있는 서비스가 달라져야 한다.

authentification(인증)
				 회원 검색 하기
로그인 한 사용자 -  권한(authorization)
   	             로그아웃 / 전체회원정보보기 / 정보수정

로그인 안한 사용자 - 권한(authorization)
				  회원가입 / 로그인
 --%>
<div id="wrap">	
	<a href="find.jsp">회원 검색 하기</a><p>
	<c:choose>
		<c:when test="${!empty vo}">
			<a href="allmember.do">모든 회원 보기</a><p>	
			<a href=update.jsp>회원 정보 수정</a><p>
			<a href="logout.do">로그아웃</a><p>		
		</c:when>
		<c:otherwise>
			<a href="register.jsp">회원 가입 하기</a><p>	
			<a href="login.jsp">로그인 하기</a><p>		
		</c:otherwise>	
	</c:choose>
</div>
</body>
</html>
```

첫 화면을 나타내는 jsp이다. 여기서는 로그인에 성공했을 때와 실패했을 때를 구분하여 기능을 만들어 놓았다. 지난번에는 '< a href >' 에서 경로를 "/front.do?commmand=..." 이런식으로 요청을 하여 dispatcher 서블릿으로 보내고 command에 값을 넣어 요청을 구분하여 처리하였다. 

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


@WebServlet(urlPatterns = "*.do", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1. command 값 받기
		//2. factory한테 command 값을 준다... controller 타입을 반환
		//3. Controller의 메소드를 호출...Component의 메소드가 가동...path를 리턴
		//4. path를 가지고 페이지 이동
		
		//String command = request.getParameter("command");
		//hidden값으로 들어온 요청을 받지 않고...들어온 요청의 전체 주소를 직접 받아온다... 앞부분만 자른다.
		String requestURI = request.getRequestURI();//web22_FactoryPattern/find.do
		System.out.println("RequestURI :: " + requestURI);
		
		String contextPath = request.getContextPath();
		System.out.println("ContextPath :: " + contextPath);//web22_FactoryPattern
		
		String command = requestURI.substring(contextPath.length()+1);// /find.do
		
		String path = "index.jsp";
		ModelAndView mv = null;
		Controller controller = HandlerMapping.getInstance().createController(command);
		
		try {
			mv = controller.execute(request, response);
			path = mv.getPath();
		}catch(Exception e) {
			
		}
		if(mv!=null){
			if(mv.isRedirect()) response.sendRedirect(path);
			else request.getRequestDispatcher(path).forward(request, response);
		}
		
	}
}
```

해당하는 하나의 서블릿에서 urlpattern을 "*.do"로 하여서 .do로 끝나는 모든 요청을 처리할 수 있다. 이때, hidden값으로 들어온 요청을 받지 않고 들어온 요청의 전체 주소를 직접 받아온다. requestURL로 전체 경로를 받아오고 contextPath로 root 경로를 받는다. 그 후에 substring으로 문자를 잘라서 command를 뽑아낼 수 있다.

``` java
package servlet.controller;
/*
 * 결과페이지의 이름과 이동방식(forward | rediect)에 대한 정보를
 * 담고 있는 객체
 */
public class ModelAndView {
	private String path;
	private boolean isRedirect;
	
	public ModelAndView(String path, boolean isRedirect) {
		super();
		this.path = path;
		this.isRedirect = isRedirect;
	}
	
	public ModelAndView() {}
	
	public ModelAndView(String path) {
		super();
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isRedirect() {
		return isRedirect;
	}

	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}
}
```

이 코드는 결과 페이지의 이름과 이동방식에 대한 정보를 객체로 담았다.

모든 필드를 가진 생성자, 기본 생성자, path를 인자값으로 받는 생성자 이렇게 3가지를 가지고 있다.  페이지를 이동할 때 80% 정도가 foward로 이동하기 때문에 isRedirect가 true면 redirect로, false면 forward방식으로 사용한다. (프로그래밍은 보수적인 성향을 선호하기 때문에 기본값을 false로 하는 경향이 있는데 그 대표적인 타입이 boolean이다.)

``` java
package servlet.controller;
/*
 * Controller를 생성하는 공장...
 */
public class HandlerMapping {
	private static HandlerMapping factory = new HandlerMapping();
	private HandlerMapping() {
		
	}
	public static HandlerMapping getInstance() {
		return factory;
	}
	
	//Component를 생성...command하는 기능...factory 본연의 역할
	public Controller createController(String command) {
		Controller controller = null;
		if(command.equals("find.do")) {
			controller = new FindController();
		}else if(command.equals("register.do")) {
			
		}
		return controller;
	}
		
}
```

이 코드는 전에 command와 controller를 주고 받던 일종의 ControllerFactory 클래스이다. 요청 command가 find.do일 때 FindController라는 객체를 생성한다.

``` java
package servlet.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servlet.model.MemberDAOImpl;
import servlet.model.MemberVO;

public class FindController implements Controller{

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) {
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
		
		
		return new ModelAndView(path);
	}

}
```

``` java
package servlet.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {
	ModelAndView execute(HttpServletRequest request, HttpServletResponse response);//정하기 나음
	//화면 연결하기 위해 인자값에 request와 response를 받는다.
}
```

마지막으로 find를 수행하는 컴포넌트에서는 id파라미터를 받아서 dao로 비즈니스 로직을 수행한 후 MemberVO를 리턴한다. 여기서 마지막에 ModelAndView로 path가 감싸져야 하는 것은 해당 컴포넌트가 Controller라는 인터페이스라는 것을 상속을 받는데 이곳에서 ModelAndView 타입의 execute 메소드가 실행이 되고 기존의 ModelAndView 객체에서 3개의 생성자를 만든 것 중 하나가 이곳에서 실행된다. 여기서 path 옆에 true가 나온다면 Redirect방식으로 페이지를 이동하는 것이고, 옆에 false를 표시하거나 그냥 path만 적으면 그냥 forward 방식으로 페이지를 이동한다. 