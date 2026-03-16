package com.video;

import com.video.common.utils.CodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class CodeTest {
    @Test
    public void testGeneCode(){
        System.out.println(CodeUtil.geneVisibleCode(1, 100));
    }
}
