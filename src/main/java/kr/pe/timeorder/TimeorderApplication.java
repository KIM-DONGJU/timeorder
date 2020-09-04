package kr.pe.timeorder;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.pe.timeorder.interceptor.JwtInterceptor;


@SpringBootApplication
public class TimeorderApplication implements WebMvcConfigurer {
	/* 포함 url : /api/**, /api/employee/**
	 * 제외 url : /api/employee/signin/**
	 * 		정적 파일 제외 "/css/**", "/scripts/**", "/plugin/**"
	 */

	@Autowired
	private JwtInterceptor jwtInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		System.out.println("addInterceptors() --");
		//기본 적용 경로로, 적용 제외 경로
		registry.addInterceptor(jwtInterceptor).addPathPatterns("/api/**")
				.excludePathPatterns(Arrays.asList("/api/employee/**", "/css/**", "/scripts/**", "/plugin/**"));
		
	}
	
	//CORS(Cross-Origin Resource Sharing)
	//웹 브라우저에서 외부 도메인 서버와 통신하기 위한 방식을 표준화한 스펙
	/* jwt-auth-token 키로 타 도메인 요청시에도 또는 응답을 사용하기 위한 설정
	 * 
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		System.out.println("addCorsMappings() --");
		registry.addMapping("/**")
		.allowedOrigins("*")
		.allowedMethods("*")
		.allowedHeaders("*");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(TimeorderApplication.class, args);
	}

}
