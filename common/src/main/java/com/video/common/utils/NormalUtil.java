package com.video.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class NormalUtil {
    //生成9位纯数字ID
    public static int randomUid() {
        Random random = new Random();
        int number = random.nextInt(900000000) + 100000000;
        log.info("生成的9位uid:{}", number);
        return number;
    }
}
