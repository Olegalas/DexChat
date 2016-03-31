import org.apache.log4j.Logger;

import java.net.Socket;

/**
 * Created by dexter on 01.04.16.
 */
public class Chat extends Thread {

    private final static Logger LOGGER = Logger.getLogger(Server.class);

    private final Socket chatSocket;
    private final Client friend;
    private final Client client;

    public Chat(Socket chatSocket, Client friend, Client client) {
        this.chatSocket = chatSocket;
        this.friend = friend;
        this.client = client;
    }

    @Override
    public void run(){

        LOGGER.info("chat between " + client + " and " + friend + "was started");


    }
}
