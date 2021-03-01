package ServerPack;


import BaseData.SqLite;
import Message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HandlerSerializable extends SimpleChannelInboundHandler <Message> {
    private Path dirServer;
    private String dirServerPath;
    private ClickOperation clickOperation;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
             clickOperation = new ClickOperation();
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
                dirServerPath = dirServer.toString();
                channelHandlerContext.writeAndFlush(new AuthMessage(true));
                channelHandlerContext.writeAndFlush(new ListServer(clickOperation.getArrays(dirServer.toFile()), dirServer.toString()));
            }
            else {
                channelHandlerContext.writeAndFlush(new AuthMessage(false));
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

        if(message instanceof ClickMessage) {
            ClickMessage click = (ClickMessage) message;
            String nameFile = clickOperation.getName(click.getPath());
            File file = new File(dirServer + nameFile);
            if(file.isDirectory()) {
                channelHandlerContext.writeAndFlush(new ListServer(clickOperation.getArrays(file), file.getPath()));
                dirServer = Paths.get(dirServer + nameFile);
            }
        }

        if(message instanceof BackMessage) {
            BackMessage back = (BackMessage) message;
            File childFile = new File(back.getChildPath());
            if(!(childFile.getPath().equals(dirServerPath))) {
                dirServer = Paths.get(childFile.getParentFile().getPath());
                channelHandlerContext.writeAndFlush(new ListServer(clickOperation.getArrays(dirServer.toFile()), dirServer.toString()));
            }
        }

        if(message instanceof RenameMessage) {
            RenameMessage rename = (RenameMessage) message;
            File oldFile = new File(dirServer.toString() + clickOperation.getName(rename.getOldPath()));
            oldFile.renameTo(new File(dirServer.toString() + clickOperation.getName(rename.getNewPath())));
            channelHandlerContext.writeAndFlush(new ListServer(clickOperation.getArrays(dirServer.toFile()), dirServer.toString()));
        }

        if(message instanceof DeleteMessage) {
            DeleteMessage deleteFile = (DeleteMessage) message;
            File removeFile = new File(dirServer.toString() + clickOperation.getName(deleteFile.getLineOne()));
            if(removeFile.isFile()) {
                removeFile.delete();
                channelHandlerContext.writeAndFlush(new ListServer(clickOperation.getArrays(dirServer.toFile()), dirServer.toString()));
            }
        }

        if(message instanceof SendMessage) {
            SendMessage sendMessage = (SendMessage) message;
            byte[] buffer = new byte[1028];
            try (InputStream in = new FileInputStream(sendMessage.getSendFile());
                    OutputStream out = new FileOutputStream(dirServer.toString() + clickOperation.getName(
                            sendMessage.getSendFile().getName()))) {
                while (in.read(buffer) != -1) {
                    out.write(buffer);
                }
            }
        }

        if (message instanceof DownloadMessage) {
            DownloadMessage downloadMessage = (DownloadMessage) message;
            channelHandlerContext.writeAndFlush(new DownloadMessage
                    (new File(dirServer + clickOperation.getName(downloadMessage.getFileNameDownload()))));
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }
}
