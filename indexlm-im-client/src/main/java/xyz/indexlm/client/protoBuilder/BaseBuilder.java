package xyz.indexlm.client.protoBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.indexlm.client.command.ClientSession;
import xyz.indexlm.im.common.protobuf.ProtoMsg;

import java.io.Serializable;

/**
 * @Description
 * @Author LiuMing
 * @Date 2020/6/15
 */
public class BaseBuilder implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(BaseBuilder.class);

    protected ProtoMsg.HeadType type;
    private long seqId;
    private ClientSession session;

    public BaseBuilder(ProtoMsg.HeadType type, ClientSession session) {
        this.type = type;
        this.session = session;
    }

    /**
     * 构建消息 基础部分
     */
    public ProtoMsg.Message buildCommon(long seqId) {
        this.seqId = seqId;

        ProtoMsg.Message.Builder mb =
                ProtoMsg.Message
                        .newBuilder()
                        .setType(type)
                        .setSessionId(session.getSessionId())
                        .setSequence(seqId);
        return mb.buildPartial();
    }

    public ProtoMsg.HeadType getType() {
        return type;
    }

    public void setType(ProtoMsg.HeadType type) {
        this.type = type;
    }

    public long getSeqId() {
        return seqId;
    }

    public void setSeqId(long seqId) {
        this.seqId = seqId;
    }

    public ClientSession getSession() {
        return session;
    }

    public void setSession(ClientSession session) {
        this.session = session;
    }
}
