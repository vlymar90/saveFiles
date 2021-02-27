package ServerPack;


import BaseData.SqLite;
import Message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static BaseData.SqLite.*;

public class HandlerSerializable extends SimpleChannelInboundHandler <Message> {
    private Path dirServer;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {

        if (message instanceof AuthMessage) {
            AuthMessage auth = (AuthMessage) message;
            SqLite.connect();
            String dir =  SqLite.getDirectory(auth.getNick(), auth.getPassword());
            if(dir != null) {
                SqLite.disConnect();
                dirServer = Paths.get("ServerSaveFiles/ServerClientDirectory/" + dir);
                File file = new File(dirServer.toString());
                channelHandlerContext.writeAndFlush(new AuthMessage(file , true));
            }
            else {
                channelHandlerContext.writeAndFlush(new AuthMessage(null, false));
            }

        }

        if (message instanceof RegMessage) {
            RegMessage reg = (RegMessage) message;
            SqLite.connect();
            if (SqLite.registration(reg.getLogin(), reg.getPassword(), reg.getDirectory())) {
                dirServer = Files.createDirectories(Paths.get("ServerSaveFiles/ServerClientDirectory/" + reg.getDirectory()));
                Path music = Files.createDirectories(Paths.get(dirServer + "/music"));
                Path images = Files.createDirectories(Paths.get(dirServer + "/images"));
                Path documents = Files.createDirectories(Paths.get(dirServer + "/documents"));
                Path others = Files.createDirectories(Paths.get(dirServer + "/others"));
                channelHandlerContext.writeAndFlush(new RegMessage(true));
            }
            else {
                channelHandlerContext.writeAndFlush(new RegMessage(false));
            }
            SqLite.disConnect();
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
