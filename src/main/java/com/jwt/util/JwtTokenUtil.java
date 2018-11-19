package com.jwt.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JwtTokenUtil {

	private static final String SALT =  "intranetsecret";	// 로그인 토큰의 암호화 키. 외부 노출되지 않도록 주의.
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
	

	public int checkToken(String jwt){
		int status = HttpServletResponse.SC_UNAUTHORIZED;	// 401
		try{
		// 유효한 토큰일 때,
            Jws<Claims> claims = Jwts.parser()
            						.setSigningKey(this.generateKey())
            						.parseClaimsJws(jwt);
            status = HttpServletResponse.SC_ACCEPTED;	// 202
        }catch(ExpiredJwtException eje){
        //JWT를 생성할 때 지정한 유효기간 초과할 때.
        	logger.debug("JWT 유효기간 초과");
        	status = HttpServletResponse.SC_UNAUTHORIZED;	// 401
        }catch(UnsupportedJwtException uje){
        //예상하는 형식과 일치하지 않는 특정 형식이나 구성의 JWT일 때
        	logger.debug("JWT 형식 불일치");
        	status = HttpServletResponse.SC_NOT_ACCEPTABLE;	// 406
        }catch(MalformedJwtException mje){
        //JWT가 올바르게 구성되지 않았을 때
        	logger.debug("+ 구성");
        	status = HttpServletResponse.SC_NOT_ACCEPTABLE;	// 406
        }catch(SignatureException se){
        //JWT의 기존 서명을 확인하지 못했을 때
        	logger.debug("JWT 서명 확인 불가");
        	status = HttpServletResponse.SC_NOT_ACCEPTABLE;	// 406
        }catch(IllegalArgumentException iae){
        	logger.debug("JWT IllegalArgumentException");
        	status = HttpServletResponse.SC_NOT_ACCEPTABLE;	// 406
        }catch (Exception e) {
        	logger.debug("JWT 검증 중, 알 수 없는 오류 발생 {}", e);
        	status = HttpServletResponse.SC_NOT_ACCEPTABLE;	// 406
        }
		
		return status;
	}
	
}
