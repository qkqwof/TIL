## Cookie

서버 상에서 Data를 저장하려면 Attribute로 map방식으로 저장해야 되는데 setAttribute나 getAttribute는 request, httpSession, ServletContext에서 사용된다. 이것들의 차이점은 scope가 다르다는 것인데 그 중에 httpSession은 로그인 진행 시에 사용된다. 이때 Session과 cookie가 함께 사용이 된다.

![cookie](https://user-images.githubusercontent.com/76687078/113569372-5c4b0280-964d-11eb-9d81-74558bad1cf3.jpg)

예전에는 하드웨어의 성능이 안 좋았기 때문에 객체를 담아서 전달하는 Attribute를 사용하기 힘들었고 쿠키를 사용했다.

**Cookie의 특징**
1. 작은 양의 정보로 저장
    - String 형태로 저장되어 Object 방식으로 저장되지 않는다.
2. 서버에서 브라우저에 저장
    - 보안상의 문제가 발생하였다.
3. 서버로 다시 돌아간다.

**Cookie의 용도**
- 서버와 브라우저 사이를 오가면서 HTTP Protocol에서 처음 접속하는 Client를 JsessionId(일종의 클라이언트 아이디...)값을 통해 알려준다.

## Session

세션은 클라이언트가 서버에 요청을 할 때 만들어지는데 JsessionId라는 클라이언트 정보가 들어있다. 세션은 클라이언트가 접속을 한 상황에서의 정보를 저장을 한다. 클라이언트가 요청을 주고 받는데 접속이 끊어져버리면 서버가 다음번에 요청을 할 때 클라이언트에 대한 기억이 없다.

따라서 세션이 처음 만들어질 떄 쿠키도 JsessionId를 가지고 생성이 되는데 클라이언트와 서버가 서로 요청과 응답을 받을 때 쿠키를 계속 주고 받으면서 접속이 끊어지지 않는 효과가 있다. 만약 Servlet에서 로그아웃하는 명령어를 주면 그때는 세션이 죽어서 접속을 다시 하면 새로운 세션이 생성된다.

``` html
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>Login Page</h2>
<form action="LoginServlet" method="post">
	ID : <input type="text" name="id" required="required"><br><br>
	PASSWORD : <input type="password" name="password" required="required"><br><br>
	<input type="submit" value="Login">
</form>
</body>
</html>
```

``` java
package servlet.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import servlet.model.MemberVO;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		//클라이언트가 요청하는 순간 세션이 만들어진다...우리는 이걸 받아서 사용하면 된다.
		HttpSession session = request.getSession();
		
		//클라이언트를 구분하는 값도 함께 생성....JSESSIONID값
		out.println("JSESSIONID :: "+session.getId()+"<br>");
		
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		
		//위 값을 비즈니스 로직에 넣어서 호출...MemberVO vo=login(id,pass);
		MemberVO rvo = new MemberVO(id,password,"홀길동","여의도");
		
		session.setAttribute("vo", rvo);
		
		out.println("<hr>");
		out.println("<a href=ProductServlet>ProductServlet이동...두번쨰 다시 요청하는 서블릿...</a>");
		
		
	}

}
```

``` java
package servlet.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import servlet.model.MemberVO;


@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession();//새롭게 만들어진 세션이 아니라 기존의 세션을 계속 사용한다...로그아웃해서 세션을 죽이지 않았다.
		if(session.getAttribute("vo")!=null) {//로그인 되었다면...
			MemberVO vo = (MemberVO)session.getAttribute("vo");
			out.println("JSESSIONID :: " + session.getId()+"<br>");//앞페이지에서 출려된 값과 동일한 값이 출력...같은 세션을 계속 사용
			out.println("VO :: "+vo);
			
		//지금 로그인한 사용자가 상품을 구매...상품정보(책)까지 다시 세션에 추가...
			session.setAttribute("book", "web개발에 대해서");
			out.println("<a href=CartServlet>CartServlet이동...세번째 다시 요청하는 서블릿...</a>");
			
		}else {//로그인 실패...
			out.println("<a href=login.html>로그인부터 다시 하세요</a>");
		}
	}

}
```

```java
package servlet.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession();//이것도 새로 만든 세션이 아니라...앞에서 계속 사용하던 세션
		out.println("JSESSIONID :: " + session.getId()+"<br>");//확인용
		out.println("VO :: "+session.getAttribute("vo")+"<br>");
		out.println("Book Info :: " + session.getAttribute("book")+"<br>");
		//해당하는 상품에 대한 결제 로직...다 진행되었다면...
		if(session.getAttribute("vo")!=null) {//로그인 된 상태라면...
			//이제 세션을 죽인다..로그아웃 로직
			session.invalidate();//세션을 죽이는 기능...로그아웃 로직에 반드시 이 코드가 들어가야 한다.
			out.println("<script>");
			out.println("alert('로그아웃 하시겠습니까?')");
			out.println("location.href='login.html'");//자바스크립트에서 다른 페이지로 연결...
			out.println("</script>");
		}else {//로그인 안된 상태라면...
			response.sendRedirect("login.html");
		}
	}

}
```

클라이언트와 요청과 응답을 주고 받으면서 계속해서

```java
out.println("JSESSIONID :: "+session.getId()+"<br>");
```

를 사용하면서 일치하는지를 비교해봤는데 일치하였고, 마지막 CartServlet에서는 로그아웃 로직을 이용해서 자바스크립트로 세션을 끊었다.