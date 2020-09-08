package kr.pe.timeorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class TimeorderApplication implements WebMvcConfigurer {
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
