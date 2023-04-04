package com.study.servlet.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")	// 매개변수 안의 값으로 필터 유무를 결정
public class AuthFilter extends HttpFilter implements Filter {
       
    public AuthFilter() {

    }

	public void destroy() {
		// 객체가 소멸 될 때 딱 한번 실행
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 전처리
		System.out.println("전처리");
		/*
		 * 1. URL : role요청이 들어왔을때 로그인이 되어 있는지 확인
		 * 2. 로그인이 되어 있으면 doFilter를 통해 서블릿에 접근 허용
		 * 3. 로그인이 되어 있지않으면 response를 통해 로그인 페이지로 이동 
		 */
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		String uri = httpRequest.getRequestURI();
		
		httpRequest.getSession().setMaxInactiveInterval(10);
		
		List<String> antMatchers = new ArrayList<>();
		antMatchers.add("/user");
		antMatchers.add("/mypage");
		
		for(String antMatcher : antMatchers) {
			if(uri.startsWith(antMatcher)) {
				// antMatcher에 등록 된 URI로 시작하면 인증을 거친다.
				HttpSession session = httpRequest.getSession();
				if(session.getAttribute("AuthenticationPrincipal") == null) {	// Attirube() 서버 저장소 set, get으로 사용
					httpResponse.sendRedirect("/login.html");
					return;					
				}				
			}
		}
		
		
		chain.doFilter(request, response); // 다음으로 실행 될 필터나 서블릿
		System.out.println("후처리");
		// 후처리
	}

	public void init(FilterConfig fConfig) throws ServletException {
		// 객체가 생성 될 때 딱 한번 실행
	}

}
