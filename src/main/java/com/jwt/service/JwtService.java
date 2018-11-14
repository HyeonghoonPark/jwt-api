package com.jwt.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface JwtService {

	public HashMap<String, Object> SignUp(HashMap<String, Object> param);

	public HashMap<String, Object> SignIn(HashMap<String, Object> param, HttpServletResponse response);

}
