package com.jwt.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jwt.service.JwtService;

@Controller
public class JwtController {

	@Autowired JwtService jwtService;

	@RequestMapping(value = "/SignUp", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> SignUp(@RequestBody HashMap<String, Object> param) {

		HashMap<String, Object> resultMap = new HashMap<String,Object>();
		
		resultMap.putAll(jwtService.SignUp(param));
		
		return resultMap;
		
	}
	
	@RequestMapping(value ="/SignIn", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> SignIn(@RequestBody HashMap<String, Object>param, 
			HttpServletResponse response){
		
		HashMap<String, Object> resultMap = new HashMap<String,Object>();
		
		resultMap.putAll(jwtService.SignIn(param, response));
		
		return resultMap;
		
	}
	
}
