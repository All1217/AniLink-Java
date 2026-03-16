package com.video.web.main.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI().info(
                new Info()
                        .title("Video弹幕视频网站API")
                        .version("1.0")
                        .description("Video弹幕视频网站API"));
    }

    @Bean
    public GroupedOpenApi loginAPI() {

        return GroupedOpenApi.builder().group("登录接口测试").
                pathsToMatch(
                        "/main/login/**",
                        "/main/info"
                ).
                build();
    }

    @Bean
    public GroupedOpenApi chatAPI() {

        return GroupedOpenApi.builder().group("聊天室").
                pathsToMatch(
                        "/main/chat/getChatVoPage",
                        "/main/chat/get/chat/st"
                ).build();
    }

    @Bean
    public GroupedOpenApi userAPI() {

        return GroupedOpenApi.builder().group("用户相关").
                pathsToMatch(
                        "/main/user/getById",
                        "/main/user/register"
                ).build();
    }

    @Bean
    public GroupedOpenApi videoAPI() {

        return GroupedOpenApi.builder().group("视频与视频数据相关").
                pathsToMatch(
                        "/main/video",
                        "/main/video/interact/like",
                        "/main/video/getInterActLike",
                        "/main/video/getInterActionStats",
                        "/main/video/interact/coin",
                        "/main/video/getInterActionCoin"
                ).build();
    }

    @Bean
    public GroupedOpenApi danmuAPI() {

        return GroupedOpenApi.builder().group("弹幕相关").
                pathsToMatch(
                        "/main/danmu/getSimilarUsers",
                        "/main/danmu/filterUserByTags"
                ).build();
    }

    @Bean
    public GroupedOpenApi fansAPI() {

        return GroupedOpenApi.builder().group("粉丝管理相关").
                pathsToMatch(
                        "/main/fans/follow",
                        "/main/fans/get/fans"
                ).build();
    }

    @Bean
    public GroupedOpenApi favoriteAPI() {

        return GroupedOpenApi.builder().group("收藏管理相关").
                pathsToMatch(
                        "/main/favorite/getFavoriteListPage",
                        "/main/favorite/getVideoByFavor",
                        "/main/favorite/getVideoFavoritePage"
                ).build();
    }

    @Bean
    public GroupedOpenApi dataCenterAPI() {

        return GroupedOpenApi.builder().group("数据中心").
                pathsToMatch(
                        "/main/dataCenter/total/get/play"
                ).build();
    }

    @Bean
    public GroupedOpenApi historyAPI() {

        return GroupedOpenApi.builder().group("历史记录").
                pathsToMatch(
                        "/main/history/getHistory/page"
                ).build();
    }
}
