package xyz.indexlm.client.imClient;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.indexlm.client.command.ClientCommandMenu;
import xyz.indexlm.client.command.LoginConsoleCommand;
import xyz.indexlm.im.cocurrent.FutureTaskScheduler;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author LiuMing
 * @Date 2020/6/12
 */
@Component
public class CommandController implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(CommandController.class);

    //菜单命令收集类
    @Autowired
    ClientCommandMenu clientCommandMenu;

    //登录命令收集类
    @Autowired
    LoginConsoleCommand loginConsoleCommand;

    @Autowired
    private NettyClient nettyClient;

    private Channel channel;



    private boolean connectFlag = false;

    public void startCommandThread(){
        Thread.currentThread().setName("主线程...");
        while (true){
            //建立连接
            while (connectFlag==false){
                //开始连接
                this.startConnectServer();
                this.waitCommandThread();
            }
            //处理命令

        }
    }

    private void startConnectServer() {
        FutureTaskScheduler.add(()->{
            nettyClient.setConnectedListener(connectedListener);
            nettyClient.doConnect();
        }) ;
    }
    public synchronized void notifyCommandThread() {
        //唤醒，命令收集程
        this.notify();

    }
    private void waitCommandThread() {

    }

    GenericFutureListener<ChannelFuture> connectedListener= new GenericFutureListener<ChannelFuture>() {
        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
            final EventLoop eventLoop=channelFuture.channel().eventLoop();
            if (!channelFuture.isSuccess()){
                log.info("连接失败!在10s之后准备尝试重连!");
                eventLoop.schedule(()->nettyClient.doConnect(), 10, TimeUnit.SECONDS );
                connectFlag = false;
            }else {
                connectFlag = true;
                log.info("疯狂创客圈 IM 服务器 连接成功!");
                channel = channelFuture.channel();

                //唤醒用户线程
                notifyCommandThread();
            }
        }
    };
}
