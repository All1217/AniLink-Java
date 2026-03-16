package com.video.common.utils;

import lombok.Getter;

/*
 * TODO: 完善URL解析算法
 * */
@Getter
public class WSUrlUtil {
    private final String url;

    private String uid;

    private String vid;

    public WSUrlUtil(String url) {
        this.url = url;
        parseUrl(url);
    }

    private void parseUrl(String url) {
        String[] origin = url.split("\\?");
        String[] uv = origin[1].split("&");
        this.uid = uv[0].substring(4);
        if (uv.length >= 2) {
            this.vid = uv[1].substring(4);
        }
    }
}
