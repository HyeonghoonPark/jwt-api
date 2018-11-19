package com.jwt.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface NoLoginCheck{
	// 접근시 로그인 검사가 필요없는 기능에 사용하는 어노테이션
}
