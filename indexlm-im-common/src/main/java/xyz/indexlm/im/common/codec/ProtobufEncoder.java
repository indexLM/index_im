package xyz.indexlm.im.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.indexlm.im.common.ProtoInstant;
import xyz.indexlm.im.common.protobuf.ProtoMsg;

import java.io.Serializable;

/**
 * @Description
 * @Author LiuMing
 * @Date 2020/6/12
 */
public class ProtobufEncoder extends MessageToByteEncoder<ProtoMsg.Message> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(ProtobufEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ProtoMsg.Message message, ByteBuf out) throws Exception {
        log.info("开始编码");
        //写入魔数
        out.writeShort(ProtoInstant.MAGIC_CODE);
        //写入版本
        out.writeShort(ProtoInstant.VERSION_CODE);
        //写入长度
        byte[] bytes = message.toByteArray();
        int length = bytes.length;
        out.writeInt(length);
        //消息体中包含我们要发送的数据
        out.writeBytes(message.toByteArray());
        log.info("编码结束");
    }
}
