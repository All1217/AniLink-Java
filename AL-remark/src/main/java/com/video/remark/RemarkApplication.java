package com.video.remark;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@SpringBootApplication
@MapperScan("com.video.remark.mapper")
@Slf4j
@EnableScheduling
public class RemarkApplication {
    public static void main(String[] args) {
        SpringApplication.run(RemarkApplication.class, args);
//
//        // 以下用于测试
//        ConfigurableApplicationContext context = SpringApplication.run(RemarkApplication.class, args);
//
//        // 打印所有注册的拦截器（调试用）
//        String[] interceptorBeans = context.getBeanNamesForType(HandlerInterceptor.class);
//        log.info("注册的拦截器: {}", Arrays.toString(interceptorBeans));
//
//        // 检查配置类是否被加载
//        log.info("ResourceInterceptorConfiguration 是否加载: {}",
//                context.containsBean("resourceInterceptorConfiguration"));
//        log.info("FeignRelayUserAutoConfiguration 是否加载: {}",
//                context.containsBean("feignRelayUserAutoConfiguration"));
    }
}
