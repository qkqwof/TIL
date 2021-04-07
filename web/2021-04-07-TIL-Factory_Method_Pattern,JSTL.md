## Factoty Method Pattern

프로그램 설계에서 크게 3가지를 고려하면서 만들어야 한다. **재사용성**, **확장성**, **유지보수성** 이 그 3가지이다.

FrontController 패턴이 MVC보다 확실히 간편해지는 구조는 맞지만 더 많은 요청이 있을 때 무거워지는 느낌을 준다. 따라서 Fat Client Model이라고도 한다. 실제 기업에서 사용하기에는 그 한계가 보인다고 할 수 있다.

이 한계점을 보안하기 위해서 **Factory Method 패턴** 을 사용하게 된다.

![factory](https://user-images.githubusercontent.com/76687078/113866057-cf887c00-97e7-11eb-88f2-e594f6762f2c.jpg)

보게 되면 Controller라는 인터페이스를 만들어서 각각 AddController, DeleteController, UpdateController에 implements를 해준다. 이때, 방금 얘기한 3가지를 컴포넌트라고 한다. 컴포넌트란 인터페이스 기반의 재사용성이 강한 자바 클래스를 의미한다. 즉, 각각의 Controller는 서블릿이 아니다.

기존의 MVC에서 각각의 서블릿으로 하던 기능들을 Front-Controller 패턴에서는 각각의 메서드들이 수행을 하고 Factory Method 패턴에서는 컴포넌트 기반으로 수행되어진다.(CBD: component based developement)

가장 핵심적으로 알아둬야 하는 것은 바로 어떻게 진행되는지이다.

1. command 값 받는다.
2. factory한테 command 값을 주고 controller 타입을 반환 받는다.
3. Controller의 메소드를 호출해서 Component의 메소드가 가동하고 path를 리턴 받는다.
4. path를 가지고 페이지 이동한다.

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
		//1. command 값 받기
		//2. factory한테 command 값을 준다... controller 타입을 반환
		//3. Controller의 메소드를 호출...Component의 메소드가 가동...path를 리턴
		//4. path를 가지고 페이지 이동
		
		String command = request.getParameter("command");
		String path = "index.jsp";
		
		Controller controller = ControllerFactory.getInstance().createController(command);
		
		try {
			path = controller.execute(request, response);
		}catch(Exception e) {
			
		}
		request.getRequestDispatcher(path).forward(request, response);
	}
}
```

``` java
package pattern.factory;

import pattern.action.AddController;
import pattern.action.Controller;
import pattern.action.DeleteController;
import pattern.action.UpdateController;

public class ControllerFactory {
	private static ControllerFactory factory = new ControllerFactory();
	private ControllerFactory() {
		System.out.println("ControllerFactory...Creating...");
	}
	
	public static ControllerFactory getInstance() {
		return factory;
	}
	
	//Controller를 생성하는 기능
	public Controller createController(String command) {
		Controller controller = null;
		if(command.equals("insert")) {
			controller = new AddController();
			System.out.println("AddController...생성");
		}else if(command.equals("update")) {
			controller = new UpdateController();
			System.out.println("UpdateController...생성");
		}else if(command.equals("delete")) {
			controller = new DeleteController();
			System.out.println("DeleteController...생성");
		}
		return controller;
	}
}
```

``` java
package pattern.action;

public interface Controller {
	String requestHandle();//public abstract가 앞에 생략되어 있다.
}
```

나머지 인터페이스를 상속받는 부분은 생략하도록 한다. 그리고 마지막 인터페이스에서 화면단에 표현할 때는 HttpServletRequest request, HttpServletResponse response를 인자값으로 넣어줘야 한다.

## JSTL

1. 2개의 라이브러리가 추가되어야 사용 가능한 기술이다. (프로젝트 단위로 직접 추가)
2. taglib 속성을 지시어에 추가해야 한다.
3. set, if, choose, forEach 속성을 기본적으로 이해해야 한다.

- 이름을 지정할 때(값을 저장할 때 사용하는 이름을 지정)

``` html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="num1" value="7"/>
<c:set var="num2" value="9"/>

<c:set var="multiple" value="${num1 * num2}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<b>${num1}과 ${num2}의 곱은 ${multiple} 이다</b>
</body>
</html>
```

여기서 눈여겨 봐야할 점은 연산이 가능하다는 점이다.

- if문법을 작성

``` html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>    
<!-- 폼 안에 입력 값은 문자로 인식!! -->
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<c:if test="${param.num=='1004'}"> 
	<h1>당신은 당첨되었습니다!!</h1>
</c:if>
<c:if test="${param.num!='1004'}">
	<h1>다음번을 기대해주세요</h1>
</c:if>
</body>
</html>
```

- 조건식이 2개 이상일 때

``` html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>  
<!-- 조건이 2개 이상일 때 choose를 씀 -->  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<c:choose> 
	<c:when test="${param.num=='1' }">
		그럭저럭 지냅니다.
	</c:when>
	<c:when test="${param.num=='2' }">
		네...잘 지내고 있어요
	</c:when>
	<c:otherwise>
		멋진 시간을 보내고 있습니다.
	</c:otherwise>
</c:choose>
</body>
</html>
```

- 반복문을 쓸 때

``` html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<c:forEach var="cnt" begin="1" end="10">
	<font size="${cnt}">Hello~~~Nice day!!!<br></font>
</c:forEach>
</body>
</html>
```

어제 배운 EL은 조건문이나 반복문을 표현하는 것이 불가능하기 때문에 EL과 JSTL을 이용해서 JSP에 남아있는 자바 코드를 없앤다.