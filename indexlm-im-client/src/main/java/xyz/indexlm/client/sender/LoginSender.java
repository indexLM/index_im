package xyz.indexlm.client.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import xyz.indexlm.client.protoBuilder.LoginMsgBuilder;
import xyz.indexlm.im.common.protobuf.ProtoMsg;

import java.io.Serializable;

/**
 * @Description
 * @Author LiuMing
 * @Date 2020/6/15
 */
@Component
public class LoginSender extends BaseSender implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(LoginSender.class);

    public void sendLoginMsg() {
        if (!isConnected()) {
            log.info("还没有建立连接!");
            return;
        }
        log.info("构造登录消息");
        ProtoMsg.Message message =
             LoginMsgBuilder.buildLoginMsg(getUser(), getSession());
        log.info("发送登录消息");
        super.sendMsg(message);
    }
}
