package com.jwt.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenUtil {

	private static final String SALT =  "testToken";	// 로그인 토큰의 암호화 키. 외부 노출되지 않도록 주의.
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
	
	public String create(Date exp, HashMap<String, Object> customClaimSet){ 
		
		String jwt =	Jwts.builder()
				.setHeaderParam("type", "JWT")
				.setHeaderParam("regDate", System.currentTimeMillis())
				.setClaims(customClaimSet)
				.setExpiration(exp)
				.signWith(SignatureAlgorithm.HS256, this.generateKey())
				.compact();
		return jwt;
		
	}
	
	private byte[] generateKey(){
		byte[] key = null;
		try {
			key = SALT.getBytes("UTF-8");
		}catch(UnsupportedEncodingException e){
			if(logger.isInfoEnabled()){
				e.printStackTrace();
			}else{
				logger.error("Making JWT Key Error ::: {}", e.getMessage());
			}
		}
		return key;
	}
}
