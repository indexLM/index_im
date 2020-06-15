package xyz.indexlm.client.sender;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.indexlm.client.command.ClientSession;
import xyz.indexlm.im.common.bean.User;
import xyz.indexlm.im.common.protobuf.ProtoMsg;

import java.io.Serializable;

/**
 * @Description
 * @Author LiuMing
 * @Date 2020/6/15
 */
public abstract class BaseSender implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(BaseSender.class);
    private User user;
    private ClientSession session;


    public boolean isConnected() {
        if (null == session) {
            log.info("session is null");
            return false;
        }

        return session.isConnected();
    }

    public boolean isLogin() {
        if (null == session) {
            log.info("session is null");
            return false;
        }

        return session.isLogin();
    }

    public void sendMsg(ProtoMsg.Message message) {

        if (null == getSession() || !isConnected()) {
            log.info("连接还没成功");
            return;
        }

        Channel channel = getSession().getChannel();
        ChannelFuture f = channel.writeAndFlush(message);
        f.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future)
                    throws Exception {
                // 回调
                if (future.isSuccess()) {
                    sendSucced(message);
                } else {
                    sendfailed(message);

                }
            }

        });

/*

        try {
            f.sync();
        } catch (InterruptedException e) {

            e.printStackTrace();
            sendException(message);
        }

*/
    }

    protected void sendSucced(ProtoMsg.Message message) {
        log.info("发送成功");
    }

    protected void sendfailed(ProtoMsg.Message message) {
        log.info("发送失败");
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ClientSession getSession() {
        return session;
    }

    public void setSession(ClientSession session) {
        this.session = session;
    }
}
