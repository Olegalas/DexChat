package ua.dexchat.server;

import org.apache.log4j.Logger;
import ua.dexchat.model.Message;
import ua.dexchat.server.service.ClientService;
import ua.dexchat.server.service.GetSpringContext;

/**
 * Created by dexter on 15.04.16.
 */
public class SendMessageThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(SendMessageThread.class);
    private final Message message;

    public SendMessageThread(Message message){
        this.message = message;
    }

    @Override
    public void run() {
        ClientService service = GetSpringContext.getContext().getBean(ClientService.class);
        service.sendMessageToFriend(message);
        LOGGER.info("***Message was added to friends TemporaryBuffer");
    }
}
