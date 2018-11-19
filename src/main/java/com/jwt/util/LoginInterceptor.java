package com.jwt.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jwt.util.NoLoginCheck;

public class LoginInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
			System.out.println("인터셉터!");
			
			NoLoginCheck noLoginCheck = ((HandlerMethod) handler).getMethodAnnotation(NoLoginCheck.class);
			
			System.out.println("로그인체크는?" +noLoginCheck);
			if(noLoginCheck!=null) {
				return true;
			}
				System.out.println("인터셉터 나갔음");
			
				return true;
	}

	
}
