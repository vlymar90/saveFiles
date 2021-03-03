package ServerPack;


import BaseData.SqLite;
import Message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HandlerSerializable extends SimpleChannelInboundHandler <Message> {
    private static final Logger LOGGER = LogManager.getLogger(HandlerSerializable.class);
    private Path dirServer;
    private String dirServerPath;
    private ClickOperation clickOperation;
    private static final int SIZE_BUFFER = 100000;
    private byte[] buffer = new byte[SIZE_BUFFER];

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
             clickOperation = new ClickOperation();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        if (message instanceof AuthMessage) {
            AuthMessage auth = (AuthMessage) message;
            SqLite.connect();
            String dir = SqLite.getDirectory(auth.getNick(), auth.getPassword());
            if (dir != null) {
                LOGGER.info("Client is authorisation");
                SqLite.disConnect();
                dirServer = Paths.get("ServerSaveFiles/ServerClientDirectory/" + dir);
                dirServerPath = dirServer.toString();
                channelHandlerContext.writeAndFlush(new AuthMessage(true));
                channelHandlerContext.writeAndFlush(new ListServer(clickOperation.getArrays(dirServer.toFile()), dirServer.toString()));
            } else {
                channelHandlerContext.writeAndFlush(new AuthMessage(false));
                LOGGER.info("Client is not authorisation");
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
                LOGGER.info("Client is registration");
            } else {
                channelHandlerContext.writeAndFlush(new RegMessage(false));
                LOGGER.info("Client is not registration");
            }
            SqLite.disConnect();
        }

        if (message instanceof ClickMessage) {
            ClickMessage click = (ClickMessage) message;
            String nameFile = click.getPath();
            File file = new File(nameFile);
            if (file.isDirectory()) {
                channelHandlerContext.writeAndFlush(new ListServer(clickOperation.getArrays(file), file.getPath()));
                dirServer = Paths.get(dirServer.toString() + nameFile);
            }
        }

        if (message instanceof BackMessage) {
            BackMessage back = (BackMessage) message;
            File childFile = new File(back.getChildPath());
            if (!(childFile.getPath().equals(dirServerPath))){
                dirServer = Paths.get(childFile.getParentFile().getPath());
                channelHandlerContext.writeAndFlush(new ListServer(clickOperation.getArrays(
                        dirServer.toFile()), dirServer.toString()));
            }
        }

        if (message instanceof RenameMessage) {
            RenameMessage rename = (RenameMessage) message;
            File oldFile = new File(rename.getOldPath());
            File newFile = new File(oldFile.getParentFile().toString() +"/" + rename.getNewPath());
            oldFile.renameTo(newFile);
            dirServer = Paths.get(newFile.getParentFile().getPath());
            channelHandlerContext.writeAndFlush(new ListServer(clickOperation.getArrays(dirServer.toFile()), dirServer.toString()));
            LOGGER.info("File " + rename.getOldPath() + " rename " + newFile.getName()) ;
        }

        if (message instanceof DeleteMessage) {
            DeleteMessage deleteFile = (DeleteMessage) message;
            File removeFile = new File(deleteFile.getLineOne());
            dirServer = Paths.get(removeFile.getParentFile().getPath());
            if (removeFile.isFile()) {
                removeFile.delete();
                channelHandlerContext.writeAndFlush(new ListServer(clickOperation.getArrays(dirServer.toFile()), dirServer.toString()));
                LOGGER.info("file " + deleteFile.getLineOne() + " is delete");
            }
        }
        if (message instanceof SendMessage) {
            SendMessage sendMessage = (SendMessage) message;
            try (OutputStream out = new FileOutputStream("ServerSaveFiles\\ServerClientDirectory\\newFile\\"
                    + sendMessage.getSendFile(), true)) {
                out.write(sendMessage.getBuffer());
                channelHandlerContext.writeAndFlush(new ListServer(clickOperation.getArrays(dirServer.toFile()), dirServer.toString()));
                LOGGER.info("get file is " + sendMessage.getSendFile());
            }
        }

        if (message instanceof DownloadMessage) {
            DownloadMessage downloadMessage = (DownloadMessage) message;
            File downLoadFile = new File(downloadMessage.getFileNameDownload());
            try (InputStream in = new FileInputStream(downLoadFile)) {
                int count = (int) (downLoadFile.length() - 1)/ SIZE_BUFFER + 1;
                for(int i = 0; i < count; i++) {
                    in.read(buffer);
                    channelHandlerContext.writeAndFlush(new DownloadMessage(buffer, downLoadFile.getName()));
                }
            }
            LOGGER.info(downLoadFile.getName() + " is send to client");
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
           LOGGER.info(cause.getMessage());
    }
}
