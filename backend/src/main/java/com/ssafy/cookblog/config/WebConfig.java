package com.ssafy.cookblog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.cookblog.util.JwtInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	private static final String[] INCLUDE_PATHS = {
			"/user/**",
			"/recipe/**",
			"/review/**",
			"/versus/register/**",
			"/photo/**",
			"/meet/**",
			"/online/**",
			"/report/**"
//			"/**"
	};
	private static final String[] EXCLUDE_PATHS = {
			"/user/signup/**",
			"/user/login/**",
			"/user/nickname/**",
			"/user/email/**",
			"/user/auth/**",
			
			"/recipe/all/**",
			"/recipe/search/**",
			"/recipe/view/**",
			"/recipe/get/**",
			
			"/review/view/**",
			
//			"/versus/all/**",
//			"/versus/win/**",
//			"/versus/view/**",
//			"/versus/result/**",
			
			"/swagger-ui.html",
			"/swagger-resources/**",
			"/webjars/**",
			"/v2/**",
			
			"/photo/**",
			
			"/meet/all/**",
			"/meet/type/**",
			
			"/online/all/**",
			"/online/type/**",
			
			"/blog/title/**",
			"/blog/tag/**",
			
			"/error"
	};

	@Autowired
	private JwtInterceptor jwtInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtInterceptor)
				.addPathPatterns(INCLUDE_PATHS)
				.excludePathPatterns(EXCLUDE_PATHS);
	}
}