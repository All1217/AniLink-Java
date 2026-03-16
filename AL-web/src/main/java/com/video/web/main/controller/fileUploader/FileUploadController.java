package com.video.web.main.controller.fileUploader;

import com.video.common.properties.MinIOProperties;
import com.video.common.result.Result;
import com.video.model.entity.Video;
import com.video.web.main.service.FileService;
import com.video.web.main.service.VideoService;
import io.minio.*;
import io.minio.http.Method;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.video.common.result.ResultCodeEnum.APP_SERVER_ERROR;

@Slf4j
@Tag(name = "文件上传")
@RequestMapping("/main/file")
@RestController
public class FileUploadController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private FileService fileService;

    @Operation(summary = "上传头像")
    @PostMapping("upload/avatar")
    public Result uploadAvatar(@RequestParam MultipartFile file) throws IOException {
        return fileService.uploadAvatar(file);
    }

    @Operation(summary = "上传封面")
    @PostMapping("upload/video/cover")
    public Result uploadVideoCover(@RequestParam MultipartFile file) throws IOException {
        return fileService.uploadVideoCover(file);
    }

    @Operation(summary = "上传视频")
    @PostMapping("upload/video")
    public Result uploadVideo(@RequestParam MultipartFile file) throws IOException {
        return fileService.uploadVideo(file);
    }

    @Operation(summary = "投稿")
    @PostMapping("submit")
    public Result submit(@RequestBody Video video) {
        log.info("接收到的参数：{}", video);
        videoService.insertOne(video);
        return Result.ok();
    }

    /**
     * 测试专用
    * */
    @Operation(summary = "上传图片")
    @PostMapping("/public/test")
    public Result uploadAvatarTest(@RequestParam MultipartFile file) throws IOException {
        return fileService.uploadAvatar(file);
    }
}
