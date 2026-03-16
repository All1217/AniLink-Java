package com.video.web.main.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/*
* TODO: 新改动有问题，暂时搁置
* */
@Slf4j
@Component
public class NettyWebsocketStarter implements ApplicationRunner {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    @Autowired
    private HandlerVideo handlerVideo;

    @Value("${netty.port:5051}")
    private int port;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        startNetty();
    }

    public void startNetty() {
        bossGroup = new NioEventLoopGroup(1);
        workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(64 * 1024));
                            pipeline.addLast(new IdleStateHandler(60 * 60 * 24, 0, 0, TimeUnit.SECONDS));
                            pipeline.addLast(new HandlerHeartBeat());
//                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws",
//                                    null, true, 64 * 1024,
//                                    true, true, 10000L));
//                            pipeline.addLast(handlerVideo);
                            pipeline.addLast(new WebSocketRouter());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 异步启动 Netty
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            log.info("Netty WebSocket 服务器启动成功，端口：{}", port);

            // 添加关闭监听器
            channelFuture.channel().closeFuture().addListener(future -> {
                log.info("Netty WebSocket 服务器已关闭");
                destroy();
            });

        } catch (Exception e) {
            log.error("Netty WebSocket 服务器启动失败", e);
            destroy();
        }
    }

    @PreDestroy
    public void destroy() {
        log.info("正在关闭 Netty 服务...");
        if (bossGroup != null) {
            bossGroup.shutdownGracefully(1, 3, TimeUnit.SECONDS);
        }
        if (workGroup != null) {
            workGroup.shutdownGracefully(1, 3, TimeUnit.SECONDS);
        }
        log.info("Netty 服务已关闭");
    }
}
