package com.video.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "video.minio")
@Data
public class MinIOProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
