package ua.dexchat.server;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import ua.dexchat.model.Confirmation;
import ua.dexchat.server.utils.JsonUtils;

import java.util.Map;

/**
 * Created by dexter on 27.04.16.
 */
public class ReceiverConfirmSendFileThread extends Thread{

    private static final Logger LOGGER = Logger.getLogger(ReceiverConfirmSendFileThread.class);

    private final WebSocket receiver;
    private final Map<Integer, WebSocket> sockets;
    private final Confirmation confirmation;
    private final Map<String, WebSocket> socketsWaiting;

    public ReceiverConfirmSendFileThread(WebSocket receiver, Map<Integer, WebSocket> sockets, Confirmation confirmation, Map<String, WebSocket> socketsWaiting){
        this.receiver = receiver;
        this.sockets = sockets;
        this.confirmation = confirmation;
        this.socketsWaiting = socketsWaiting;
    }

    @Override
    public void run(){

        LOGGER.info("***Thread was run");

        WebSocket sender = sockets.get(confirmation.idSender);

        if (confirmation.confirm) {
            LOGGER.info("***Receiver confirmed file send");
            socketsWaiting.put(sender.getRemoteSocketAddress().toString(), receiver);
        } else{
            LOGGER.info("***Receiver doesn't confirmed file send");
        }

        // anyway notify sender
        sender = sockets.get(confirmation.idSender);
        sender.send(JsonUtils.transformObjectInJson(confirmation));

        LOGGER.info("***Thread was killed");
    }

}
