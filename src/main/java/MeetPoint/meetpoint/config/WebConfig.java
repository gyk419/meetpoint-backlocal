package MeetPoint.meetpoint.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /*******************
     * 날짜 : 2024.04.02
     * 이름 : 김준식
     * 내용 : CORS 허용
     * *****************/
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST");
    }

    /*******************
     * 날짜 : 2024.05.25
     * 이름 : 김준식
     * 내용 : 비밀 키
     * *****************/
    @Value("${aes.secret.key")
    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }
}
