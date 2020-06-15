package xyz.indexlm.client.imClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.indexlm.client.handler.LoginResponceHandler;
import xyz.indexlm.im.common.codec.ProtobufDecoder;
import xyz.indexlm.im.common.codec.ProtobufEncoder;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Description
 * @Author LiuMing
 * @Date 2020/6/12
 */
@Component
public class NettyClient implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(NettyClient.class);
    // 服务器ip地址
    @Value("${imServer.ip}")
    private String host;
    // 服务器端口
    @Value("${imServer.port}")
    private int port;
    private Channel channel;
    /**
     * 唯一标记
     */
    private boolean initFalg = true;


    @Autowired
    private LoginResponceHandler loginResponceHandler;

    private GenericFutureListener<ChannelFuture> connectedListener;
    private Bootstrap b;
    private EventLoopGroup g;
    public NettyClient() {
        /**
         * 客户端的是Bootstrap，服务端的则是 ServerBootstrap。
         * 都是AbstractBootstrap的子类。
         **/
        /**
         * 通过nio方式来接收连接和处理连接
         */
        g = new NioEventLoopGroup();
    }
    /**
     * 连接Server
     */
    public void doConnect() {
        try {
            b = new Bootstrap();
            b.group(g);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.remoteAddress(host, port);
            // 设置通道初始化
            b.handler(
                    new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast("decoder", new ProtobufDecoder());
                            ch.pipeline().addLast("encoder", new ProtobufEncoder());
                            ch.pipeline().addLast(loginResponceHandler);
//                            ch.pipeline().addLast(chatMsgHandler);
//                            ch.pipeline().addLast(exceptionHandler);
                        }
                    }
            );
            log.info("客户端开始连接 [indexlm聊天室]");
            ChannelFuture f = b.connect();
            f.addListener(connectedListener);
            // 阻塞
            // f.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("客户端连接失败!" + e.getMessage());
        }
    }

    public void close() {
        g.shutdownGracefully();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public boolean isInitFalg() {
        return initFalg;
    }

    public void setInitFalg(boolean initFalg) {
        this.initFalg = initFalg;
    }

    public GenericFutureListener<ChannelFuture> getConnectedListener() {
        return connectedListener;
    }

    public void setConnectedListener(GenericFutureListener<ChannelFuture> connectedListener) {
        this.connectedListener = connectedListener;
    }

    public Bootstrap getB() {
        return b;
    }

    public void setB(Bootstrap b) {
        this.b = b;
    }

    public EventLoopGroup getG() {
        return g;
    }

    public void setG(EventLoopGroup g) {
        this.g = g;
    }

}
