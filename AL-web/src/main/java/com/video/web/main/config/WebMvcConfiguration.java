package com.video.web.main.config;

import com.video.web.main.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.authenticationInterceptor)
                .addPathPatterns("/main/**")
                .excludePathPatterns("/main/login/**")
                .excludePathPatterns("/main/user/register")
                .excludePathPatterns("/main/video/home/**")
                .excludePathPatterns("/main/**/public/**")
        ;
    }
}
