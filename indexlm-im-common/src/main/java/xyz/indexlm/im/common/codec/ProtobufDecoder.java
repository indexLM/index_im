package xyz.indexlm.im.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.indexlm.im.common.ProtoInstant;
import xyz.indexlm.im.common.exception.InvalidFrameException;
import xyz.indexlm.im.common.protobuf.ProtoMsg;


import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @Author LiuMing
 * @Date 2020/6/12
 */
public class ProtobufDecoder extends ByteToMessageDecoder implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(ProtobufDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();
        if (in.readableBytes() < 8) {
            // 不够包头
            return;
        }
        //读取魔数
        short magic = in.readShort();
        if (magic != ProtoInstant.MAGIC_CODE) {
            String error = "客户端口令不对:" + ctx.channel().remoteAddress();
            log.error(error);
            throw new InvalidFrameException(error);
        }
        //读取版本
        short version = in.readShort();
        // 读取传送过来的消息的长度。
        int length = in.readInt();

        // 长度如果小于0
        if (length < 0) {
            // 非法数据，关闭连接
            ctx.close();
        }
        // 读到的消息体长度如果小于传送过来的消息长度
        if (length > in.readableBytes()) {
            // 重置读取位置
            in.resetReaderIndex();
            return;
        }


        byte[] array;
        if (in.hasArray()) {
            //堆缓冲
            ByteBuf slice = in.slice();
            array = slice.array();
        } else {
            //直接缓冲
            array = new byte[length];
            in.readBytes(array, 0, length);
        }

//        if(in.refCnt()>0)
//        {
////            log.debug("释放临时缓冲");
//            in.release();
//        }
        log.info(array.toString());
        // 字节转成对象
        ProtoMsg.Message outmsg = ProtoMsg.Message.parseFrom(array);

        if (outmsg != null) {
            // 获取业务消息
            out.add(outmsg);
        }
    }
}
