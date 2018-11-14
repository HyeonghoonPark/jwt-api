package com.jwt.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("Jwt")
public interface JwtDao {

	public abstract int signupd(HashMap<String, Object> param);

	public abstract int checkUser(HashMap<String, Object> param);
	
}
