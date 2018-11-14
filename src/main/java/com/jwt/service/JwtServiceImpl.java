package com.jwt.service;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwt.dao.JwtDao;
import com.jwt.util.JwtTokenUtil;

@Service
public class JwtServiceImpl implements JwtService{

	public static final int TOKEN_EXP_TIME = 24 * 60 * 60 * 1000;	// 로그인 토큰 유효 시간(ms 단위)
	
	@Autowired JwtDao jwtDao;
	
	@Override
	public HashMap<String, Object> SignUp(HashMap<String, Object> param) {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		System.out.println("파람은? = "+param);
		
		HashMap<String, Object> equalsMap = new HashMap<String, Object>();
		equalsMap.put("id", param.get("id"));
		
		try {
		
			if(jwtDao.checkUser(equalsMap)==0){
				jwtDao.signupd(param);
				resultMap.put("status", "success");				
			}else{
				resultMap.put("status", "usedId");
			}
			
		}catch(Exception e) {
			
			resultMap.put("status", "fail");
			resultMap.put("message", e.getMessage());
			
		}
				
		return resultMap;
	}

	@Override
	public HashMap<String, Object> SignIn(HashMap<String, Object> param, HttpServletResponse response) {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		System.out.println("파람은? = "+param);
		try {
		
			if(jwtDao.checkUser(param)==1) {
				
				HashMap<String, Object> customClaims = new HashMap<String, Object>();
				
				customClaims.put("nickname", param.get("id")); // 로그인 요청자의 아이디
				Date exp = new Date(System.currentTimeMillis() + TOKEN_EXP_TIME); // 토큰 만료일자(유효기간)
				
				JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
				
				String loginToken = jwtTokenUtil.create(exp, customClaims);
				
				Cookie loginTokenCookie = new Cookie("loginToken", loginToken);
				loginTokenCookie.setPath("/");
				//loginTokenCookie.setSecure(true);	// HTTPS 프로토콜 에서만 로그인 토큰 전송
				loginTokenCookie.setMaxAge(60 * 60 * 24);	// JWT유효시간과 별개로 동작하는 클라이언트측 쿠키 유효 시간(단위: sec)
				//loginTokenCookie.setHttpOnly(true);
				response.addCookie(loginTokenCookie);
				
				resultMap.put("status", "success");
				
				
			}else {
				
				resultMap.put("status", "NotFindUser");
				
			}
			
			
		}catch(Exception e) {
		
			resultMap.put("status", "fail");
		}
		
		return resultMap;
	
	}
}
