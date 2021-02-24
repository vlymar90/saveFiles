package ServerPack;

import Message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HandlerSerializable extends SimpleChannelInboundHandler <Message> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
      if(message instanceof AuthMessage) {
          System.out.println("Привет");
      }
      channelHandlerContext.writeAndFlush(new AuthMessage("", ""));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
