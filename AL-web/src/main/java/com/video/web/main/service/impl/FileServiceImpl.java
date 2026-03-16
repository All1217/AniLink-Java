package com.video.web.main.service.impl;

import com.video.common.properties.MinIOProperties;
import com.video.common.result.Result;
import com.video.web.main.service.FileService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.video.common.constant.MinioConstant.*;
import static com.video.common.result.ResultCodeEnum.APP_SERVER_ERROR;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinIOProperties minIOProperties;

    private Result doUpload(MultipartFile file, String bucketName){
        //原始文件名
        String oriFileName = file.getOriginalFilename();
        //截取原始文件名后缀
        String ext = oriFileName.substring(oriFileName.lastIndexOf("."));
        String objectName = UUID.randomUUID() + ext;
        try {
            ObjectWriteResponse response = minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build()
            );
        } catch (Exception e) {
            log.error("上传文件失败！");
            System.out.println(e);
            return Result.fail(APP_SERVER_ERROR.getCode(), "上传文件失败！");
        }

        String filePath;
        try {
            //非公开策略下获取访问链接
//            filePath = minioClient.getPresignedObjectUrl(
//                    GetPresignedObjectUrlArgs.builder()
//                            .method(Method.GET)
//                            .bucket(bucketName)
//                            .object(objectName)
//                            .expiry(3, TimeUnit.HOURS)
//                            .build()
//            );
            //公开策略
            filePath = String.format("%s/%s/%s",
                    minIOProperties.getEndpoint(),
                    bucketName,
                    objectName
            );
        } catch (Exception e) {
            log.error("上传文件失败！");
            System.out.println(e);
            return Result.fail(APP_SERVER_ERROR.getCode(), "上传文件失败！");
        }
        return Result.ok(filePath);
    }

    @Override
    public Result uploadAvatar(MultipartFile file) {
        return doUpload(file, AVATAR_BUCKET);
    }

    @Override
    public Result uploadVideoCover(MultipartFile file) {
        return doUpload(file, VIDEO_COVER_BUCKET);
    }

    @Override
    public Result uploadVideo(MultipartFile file) {
        return doUpload(file, VIDEO_BUCKET);
    }
}
