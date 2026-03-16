package com.video.web.main.service.impl;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MinioServiceImpl {
    @Autowired
    private MinioClient minioClient;

    public void printMinioClient(){
        System.out.println(minioClient);
    }
}
