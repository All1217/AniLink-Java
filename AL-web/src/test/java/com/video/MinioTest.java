package com.video;

import com.video.web.main.service.impl.MinioServiceImpl;
import io.minio.MinioClient;
import io.minio.MinioProperties;
import io.minio.UploadObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class MinioTest {
    @Autowired
    private MinioServiceImpl minioService;
    @Autowired
    private MinioClient minioClient;

    @Test
    public void MinioBuildTest(){
        String localFile = "E:\\Program files (x86)\\DownKyi-1.6.1\\Media\\10分钟游遍南京：除了北京，同样适合当首都的城市！\\14-10分钟游遍南京：除了北京，同样适合当首都的城市！-1080P 高清-AVC.Cover.jpg";
        String extension = localFile.substring(localFile.lastIndexOf("."));
        String objectName = UUID.randomUUID() + extension;
        try{
            System.out.println(minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("mybucket1")
                            .object(objectName)
                            .filename(localFile)
                            .build()
            ));
        }catch (Exception e){
            log.error("上传文件失败！");
            System.out.println(e);
        }
    }
}
