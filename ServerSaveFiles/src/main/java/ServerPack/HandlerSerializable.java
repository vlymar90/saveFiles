package ServerPack;

import BaseData.SqLite;
import Friend.*;
import Message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;

public class HandlerSerializable extends SimpleChannelInboundHandler <Message> {
    private static final Logger LOGGER = LogManager.getLogger(HandlerSerializable.class);
    private Path dirServer;
    private String dirServerPath;
    private ClickOperation clickOperation;
    private static final int SIZE_BUFFER = 100000;
    private byte[] buffer = new byte[SIZE_BUFFER];
    private Server server;

    HandlerSerializable(Server server) {
        this.server = server;
    }

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
                server.getListClient().add(new HandlerClient(auth.getNick(), dirServer, channelHandlerContext));
                dirServerPath = dirServer.toString();
                channelHandlerContext.writeAndFlush(new AuthMessage(true));
                channelHandlerContext.writeAndFlush(new ListServer(clickOperation.getArrays(dirServer.toFile()), dirServer.toString()));
                for (HandlerClient c : server.getListClient()) {
                    c.getChannel().writeAndFlush(new ListClientMessage(getListClient()));
                }
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
                dirServer = Paths.get(dirServer.toString() + "/" + file.getName());
            }
        }

        if (message instanceof BackMessage) {
            BackMessage back = (BackMessage) message;
            File childFile = new File(back.getChildPath());
            if (!(childFile.getPath().equals(dirServerPath))) {
                dirServer = Paths.get(childFile.getParentFile().getPath());
                channelHandlerContext.writeAndFlush(new ListServer(clickOperation.getArrays(
                        dirServer.toFile()), dirServer.toString()));
            }
        }

        if (message instanceof RenameMessage) {
            RenameMessage rename = (RenameMessage) message;
            File oldFile = new File(rename.getOldPath());
            if (oldFile.isFile()) {
                File newFile = new File(oldFile.getParentFile().toString() + "/" + rename.getNewPath());
                oldFile.renameTo(newFile);
                dirServer = Paths.get(newFile.getParentFile().getPath());
                channelHandlerContext.writeAndFlush(new ListServer(clickOperation.getArrays(dirServer.toFile()), dirServer.toString()));
                LOGGER.info("File " + rename.getOldPath() + " rename " + newFile.getName());
            }
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
            try (OutputStream out = new FileOutputStream(dirServer.toAbsolutePath() + "\\"
                    + sendMessage.getSendFile(), true)) {
                out.write(sendMessage.getBuffer());
                channelHandlerContext.writeAndFlush(new ListServer(clickOperation.getArrays(dirServer.toFile()), dirServer.toString()));
                LOGGER.info("get file is " + sendMessage.getSendFile());
            }
        }

        if (message instanceof DownloadMessage) {
            DownloadMessage downloadMessage = (DownloadMessage) message;
            File downLoadFile = new File(downloadMessage.getFileNameDownload());
            if (downLoadFile.isFile()) {
                try (InputStream in = new FileInputStream(downLoadFile)) {
                    int count = (int) (downLoadFile.length() - 1) / SIZE_BUFFER + 1;
                    for (int i = 0; i < count; i++) {
                        int read = in.read(buffer);
                        if (read < SIZE_BUFFER) {
                            byte[] tmp = new byte[read];
                            System.arraycopy(buffer, 0, tmp, 0, read);
                            channelHandlerContext.writeAndFlush(new DownloadMessage(tmp, downLoadFile.getName()));
                        } else {
                            channelHandlerContext.writeAndFlush(new DownloadMessage(buffer, downLoadFile.getName()));
                        }
                    }
                }
                LOGGER.info(downLoadFile.getName() + " is send to client");
            }
        }

        if (message instanceof FriendAddMessage) {
            FriendAddMessage mess = (FriendAddMessage) message;
            ChannelHandlerContext channel = getChannel(mess.getNickFriend());
            channel.writeAndFlush(message);
        }

        if (message instanceof FriendConsent) {
            FriendConsent consent = (FriendConsent) message;
            ChannelHandlerContext channel = getChannel(consent.getNickFriend());
            channel.writeAndFlush(message);
        }

        if (message instanceof ShowDirFriend) {
            ShowDirFriend dir = (ShowDirFriend) message;
            Path dirFriend = getPath(dir.getNick());
            File file = new File(dirFriend.toString());
            channelHandlerContext.writeAndFlush(new ShowDirFriend(clickOperation.getArrays(file)));
        }

        if (message instanceof DownloadFileFriendInfo) {
            DownloadFileFriendInfo friend = (DownloadFileFriendInfo) message;
            for (HandlerClient c : server.getListClient()) {
                if (friend.getNickFriend().equals(c.getNick())) {
                    Files.walkFileTree(c.getClientDir(), new HashSet<>(), 4, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            if (friend.getNameFile().equals(file.getFileName().toString())) {
                                File downloadFile = new File(file.toAbsolutePath().toString());
                                channelHandlerContext.writeAndFlush(new DownloadFileFriendInfo(downloadFile.getName(), downloadFile.length()));
                                try (InputStream inputStream = new FileInputStream(downloadFile)) {
                                    int read = 0;
                                    while ((read = inputStream.read(buffer)) != -1) {
                                        if (read < SIZE_BUFFER) {
                                            byte[] tmp = new byte[read];
                                            System.arraycopy(buffer, 0, tmp, 0, read);
                                            channelHandlerContext.writeAndFlush(new Buffer(tmp));
                                        } else {
                                            channelHandlerContext.writeAndFlush(new Buffer(buffer));
                                        }
                                    }
                                }
                                return super.visitFile(file, attrs);
                            }

                            return super.visitFile(file, attrs);
                        }
                    });
                }
            }
        }

        if (message instanceof ClickFriend) {
            ClickFriend click = (ClickFriend) message;
            Files.walkFileTree(getPath(click.getNickFriend()), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path file, BasicFileAttributes attrs) throws IOException {
                    if (click.getFileDirName().equals(file.getFileName().toString())) {
                        File childDir = new File(file.toString());
                        channelHandlerContext.writeAndFlush(new ShowDirFriend(clickOperation.getArrays(childDir)));
                    }
                    return super.visitFile(file, attrs);
                }
            });
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
           LOGGER.info(cause.getMessage());
           server.getListClient().removeIf(c -> ctx.equals(c.getChannel()));
           for(HandlerClient c : server.getListClient()) {
               c.getChannel().writeAndFlush(new ListClientMessage(getListClient()));
           }
    }

    public ArrayList<String> getListClient() {
       ArrayList<String> list = new ArrayList<>();
       for(HandlerClient c : server.getListClient()) {
           list.add(c.getNick());
       }
        return list;
    }

    public ChannelHandlerContext getChannel(String nick) {
        return server.getListClient().stream().filter(c -> nick.equals(c.getNick()))
                .findFirst().map(HandlerClient::getChannel).orElse(null);
    }

    public Path getPath(String nick) {
       return server.getListClient().stream().filter(c -> nick.equals(c.getNick()))
                .findFirst().map(HandlerClient::getClientDir).orElse(null);
    }
}
