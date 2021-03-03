package ServerPack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerStart {
    private static final Logger LOGGER = LogManager.getLogger(ServerStart.class);
    public static void main(String[] args) {
        LOGGER.info("Server start");
        new Server();

    }

}
