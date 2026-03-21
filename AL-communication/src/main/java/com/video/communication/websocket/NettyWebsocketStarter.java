package com.video.communication.websocket;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * ApplicationRunner接口，允许我们在应用程序完全启动后由spring自动执行自定义的逻辑。
 * 启动类启动后顺带启动netty
 * */
@Slf4j
@Component
public class NettyWebsocketStarter implements ApplicationRunner {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    private Channel serverChannel;

    @Value("${netty.port:8024}")
    private int port;

    @Override
    public void run(ApplicationArguments args) {
        Thread nettyThread = new Thread(this::startNetty, "netty-websocket-thread");
        nettyThread.setDaemon(false);
        nettyThread.start();
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
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ConnectionCloseHandler());
                            // HTTP 编解码器
                            pipeline.addLast(new HttpServerCodec());
                            // HTTP 消息聚合器
                            pipeline.addLast(new HttpObjectAggregator(64 * 1024));
                            // 心跳检测
                            pipeline.addLast(new IdleStateHandler(30, 60, 0, TimeUnit.SECONDS));
                            // 自定义心跳处理器
                            pipeline.addLast(new HandlerHeartBeat());
                            // WebSocket 业务路由器（处理握手和业务分发）
                            pipeline.addLast(new WebSocketRouter());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            serverChannel = channelFuture.channel();
            log.info("Netty WebSocket 服务器启动成功，端口：{}", port);
            serverChannel.closeFuture().sync();
            log.info("Netty WebSocket 服务器已关闭");
        } catch (Exception e) {
            log.error("Netty WebSocket 服务器启动失败", e);
        } finally {
            destroy();
        }
    }

    @PreDestroy
    public void destroy() {
        log.info("正在关闭 Netty 服务...");
        if (serverChannel != null && serverChannel.isActive()) {
            serverChannel.close();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully(1, 3, TimeUnit.SECONDS);
            log.info("bossGroup 已关闭");
        }
        if (workGroup != null) {
            workGroup.shutdownGracefully(1, 3, TimeUnit.SECONDS);
            log.info("workGroup 已关闭");
        }
        log.info("Netty 服务已关闭");
    }
}