package com.jwt.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jwt.util.NoLoginCheck;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.RequiredTypeException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class LoginInterceptor extends HandlerInterceptorAdapter{

	private static final String HEADER_AUTH = "loginToken";
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
			System.out.println("인터셉터!");
			
			JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
			
			NoLoginCheck noLoginCheck = ((HandlerMethod) handler).getMethodAnnotation(NoLoginCheck.class);
			
			System.out.println("로그인체크는?" +noLoginCheck);
			if(noLoginCheck!=null) {
				return true;
			}
			
			boolean result = false;
			String token = null;
			Cookie[] cookies = request.getCookies();
			
			if(cookies != null) {
				for(Cookie cookie : cookies) {
					if(HEADER_AUTH.equals(cookie.getName())) {
						token = cookie.getValue();
					}
				}
			}
			
			System.out.println("token = "+token );
			if(token != null && !token.equals("undefined")) {
				

				System.out.println("널이나 언디파인아님");
				
				int status = jwtTokenUtil.checkToken(token);
				
				switch(status) {
				case HttpServletResponse.SC_ACCEPTED:
					
					System.out.println(HttpServletResponse.SC_ACCEPTED);
					result = authCheck(token, response, handler);
					System.out.println("나온 리설트는? =" + result);
					break;
				default:
					response.setStatus(status);
					result = false;
				}
			}else {
				
					response.setStatus(499);
					
					result = false;
					
			}
			
				return result;
	}
	
	private boolean authCheck(String token, HttpServletResponse response, Object handler){
		
		RequireAuthorize requireAuthorize = ((HandlerMethod) handler).getMethodAnnotation(RequireAuthorize.class);
		boolean result = false;
		 
		if(requireAuthorize != null){
		// 권한 검증이 필요한 메서드라면
			int authLevel = get(token, JwtTokenUtil.JWT_ROLE_KEYWORD, Integer.class);
			
			switch(authLevel){
			case 2:
				result = true;
				break;
			default:
				result = false;
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);	// 403 : 접근 권한 없음
			}
		}else{
			result = true;
		}
		
		return result;
	}
	
	public <T> T get(String token, String key, Class<T> requiredType){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		Jws<Claims> claims = null;
		T value = null;
		try {
			claims = Jwts.parser()
						.setSigningKey(JwtTokenUtil.SALT.getBytes("UTF-8"))
						.parseClaimsJws(token);
			value = claims.getBody().get(key, requiredType);
		}catch(ExpiredJwtException eje){
        //JWT를 생성할 때 지정한 유효기간 초과할 때.
        }catch(UnsupportedJwtException uje){
        //예상하는 형식과 일치하지 않는 특정 형식이나 구성의 JWT일 때
        }catch(MalformedJwtException mje){
        //JWT가 올바르게 구성되지 않았을 때
        }catch(SignatureException se){
        //JWT의 기존 서명을 확인하지 못했을 때
        }catch(IllegalArgumentException iae){
        }catch(RequiredTypeException rte){
        // token에서 key값으로 뽑아낸 value가 requiredType과 불일치 할 때 
        }catch (Exception e) {
        }
		return value;
	}

	
}
