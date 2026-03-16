package com.video.web.main.config;

import com.video.common.properties.AliOssProperties;
import com.video.common.properties.MinIOProperties;
import com.video.common.utils.AliOssUtil;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@Slf4j
public class OssConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public MinioClient minioClient(MinIOProperties minIOProperties){
        log.info("开始创建Minio文件上传工具类对象：{}", minIOProperties);
        return MinioClient.builder()
                .endpoint(minIOProperties.getEndpoint())
                .credentials(minIOProperties.getAccessKey(), minIOProperties.getSecretKey())
                .build();
    }
}
