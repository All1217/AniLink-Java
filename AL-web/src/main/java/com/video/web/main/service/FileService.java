package com.video.web.main.service;

import com.video.common.result.Result;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    Result uploadAvatar(MultipartFile file);

    Result uploadVideoCover(MultipartFile file);

    Result uploadVideo(MultipartFile file);
}
