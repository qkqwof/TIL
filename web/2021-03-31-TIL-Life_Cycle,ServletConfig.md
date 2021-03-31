## Servlet의 Life Cycle

1. DD파일을 읽어들인다. 
 
**DD(Deployment Descriptor : 배포서술자)는 WEB-INF폴더 아래 web.xml이라는 파일명**

2. Servlet Instance가 생성된다. 여기서 기본 생성자가 호출된다.

3. init()메서드를 호출하여 초기화에 필요한 작업을 실행한다.


**이렇게 3번까지의 작업이 Ready-on상태이다.(단 한번만 진행)**

4. 요청이 들어오면 service()가 먼저 호출되고 doGet() 혹은 doPost() 중 하나가 재호출된다.

**동시에  Request, Response객체가 생성되고 passing into 된다.**

5. 클라이언트의 요청을 처리하고 응답을 내보낸다.

6. 쓰레드(service()), Request, Response객체가 소멸하고 메모리에서 unbound된다.

**서블릿 객체가 소멸할 때는 Servlet클래스의 destroy()메서드가 호출되어야 하는데 이는 서버가 stop되면 실행된다.
즉 서버 구동이 멈출 때 Servlet 객체가 소멸한다.**

``` html
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2 align="center">Life cycle...CallBack Method....By Container</h2>
<a href="LIFE2">Life Cycle Test</a>
</body>
</html>
```

클라이언트의 요청을 처리한 결과의 자바 파일이다. 필드에 지정된 count값을 서블릿이 출력하지 않고
정보를 jsp에게 넘기고
결과페이지 처리를 jsp가 하도록 로직을 작성하였다. 그리고 페이지 연결은 a태그로 하였다.

출력을 해보면 콘솔에 Life Cycle의 순서대로 생성자 생성 이후에 init(), service() 순으로 출력되고 마지막에 연결을 끊으면 destroy()가 출력된다.
loadOnStartup라는 명령어로 원래는 2,3,4가 함께 출력이 되었는데 이후에는 따로 출력된다는 것을 알 수 있었다.

``` java
package servlet.life;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/LIFE2"}, loadOnStartup = 1)
public class LifeCycleServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private int count = 0; //필드...   
    
    public LifeCycleServlet2() {
        System.out.println("1.LifeCycleServlet1 생성자 호출...by Container...");
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("3. service()-->doget() or doPost() 호출....by Container...");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		count++;//
		
//      이 부분의 내용이 jsp에 들어가는 것이 역할 분담에 더 맞다.	
//		out.println("<html><body bgcolor='yellow'>");
//		out.println("<h3>LifeCycle Callback Method...</h3>");
//		out.println("<b>count :: " + ++count + "</b>");
//		out.println("</body></html>");
		
		out.println("<a href=life2.jsp?cnt="+count+">여기를 클릭하시면 JSP페이지로 이동합니다.</a>");//이떄 count값을 가지고 JSP로 가려면?
		
		out.close();
		
	}//
	
	@Override
	public void init() throws ServletException {
		  System.out.println("2. init() 호출...by Container...");
	}
	
	@Override
	public void destroy() {
		System.out.println("4. destroy() 호출...by Container...");
	}
}
```
이 jsp 파일을 생성하여 페이지가 sevlet으로부터 jsp파일로 이동되고, 서버와의 연결이 될 때마다 카운트가 늘어난다. 

``` jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body bgcolor='cyan'>
<h3>JSP Result Page</h3>
<b>Count Value :: </b>
<%= request.getParameter("cnt") %> 
</body>
</html>
```

``` html
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2 align="center">Life cycle...CallBack Method....By Container</h2>
<a href="LIFE3">Life Cycle Test</a>
</body>
</html>
```

``` java
@WebServlet(urlPatterns = {"/LIFE3"}, loadOnStartup = 1)
public class LifeCycleServlet3step extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private int count = 0; //필드... 
    private String path = "C:\\encore_PJY\\util\\web_util\\life\\count.txt";
    
    
    public LifeCycleServlet3step() {
        System.out.println("1.LifeCycleServlet1 생성자 호출...by Container...");
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("3. service()-->doget() or doPost() 호출....by Container...");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		
		PrintWriter out = response.getWriter();
		
		
		//이 부분의 내용이 jsp에 들어가는 것이 역할 분담에 더 맞다.	
				out.println("<html><body bgcolor='yellow'>");
				out.println("<h3>LifeCycle Callback Method...</h3>");
				out.println("<b>count :: " + ++count + "</b>");
				out.println("</body></html>");
				
				out.close();
	}//
	
	@Override
	public void init() throws ServletException {
		  System.out.println("2. init() 호출...by Container...");
		  //파일에 저장된 내용을 읽어들이는 로직은 Initialization 부분인 init에서 해준다.
		  try {
			  BufferedReader br = new BufferedReader(new FileReader(path));
			  String str = br.readLine();
			  //읽어들인 count값을 필드에 다시 할당함!!
			  count = Integer.parseInt(str);
			  System.out.println("count.txt의 값을 읽어서 필드 count에 다시 할당!!업데이트 성공!!");
		  }catch(Exception e) {
			  System.out.println("파일을 읽어들이는데 문제가 발생했습니다...fail...");
		  }
	}
	
	@Override
	public void destroy() {
		System.out.println("4. destroy() 호출...by Container...");
		//인스턴스가 메모리에서 제거되기 전에 필드값을 영구적으로 보관...파일에 저장
		//출력용 스트림을 생성..이곳에 필드값을 출력
		
		File f = new File(path);
		//디렉토리가 현재 존재하지 않는다면..디렉토리를 먼저 만들어 놓아야 그곳에 count.txt 파일이 생성
		f.getParentFile().mkdirs();
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(f));
			pw.println(count);
			pw.close();
			System.out.println(path+" Count값 :: "+count+" 파일에 값 영구적으로 생성");
		}catch(Exception e) {
			System.out.println("출력용 스트림 생성 실패");
		}
	}
}
```

외부의 field의 값을 영구적으로 저장하기 위해서는 디비에 연결하거나 File을 저장하는 방법을 사용한다.

해당 예제에서는 count된 값을 지정된 경로로 해서 txt파일로 저장해보는 예제였다...
결과적으로 지정한 경로에 파일이 저장되었다.

![count](https://user-images.githubusercontent.com/76687078/113162760-85008000-927a-11eb-9316-b836ab8d276a.png)