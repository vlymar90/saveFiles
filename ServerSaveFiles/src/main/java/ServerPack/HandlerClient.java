package ServerPack;

import io.netty.channel.ChannelHandlerContext;
import java.nio.file.Path;

public class HandlerClient {
    private String nick;
    private Path clientDir;
    private ChannelHandlerContext channel;

    public HandlerClient(String nick, Path clientDir, ChannelHandlerContext channel) {
        this.nick = nick;
        this.clientDir = clientDir;
        this.channel = channel;
    }

    public String getNick() {
        return nick;
    }

    public Path getClientDir() {
        return clientDir;
    }

    public ChannelHandlerContext getChannel() {
        return channel;
    }
}
