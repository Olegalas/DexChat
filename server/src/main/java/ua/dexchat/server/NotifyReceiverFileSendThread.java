package ua.dexchat.server;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import ua.dexchat.model.Confirmation;
import ua.dexchat.model.FileMessage;
import ua.dexchat.server.utils.JsonUtils;

import java.util.Map;

/**
 * Created by dexter on 14.04.16.
 */
public class NotifyReceiverFileSendThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(NotifyReceiverFileSendThread.class);

    private final WebSocket sender;
    private final FileMessage fileMessage;
    private final Map<Integer, WebSocket> socketMap;
    private final Map<String, WebSocket> socketsWaiting;

    public NotifyReceiverFileSendThread(WebSocket sender, FileMessage fileMessage,
                                        Map<Integer, WebSocket> sockets, Map<String , WebSocket> socketsWaiting){
        this.sender = sender;
        this.fileMessage = fileMessage;
        this.socketMap = sockets;
        this.socketsWaiting = socketsWaiting;
    }

    @Override
    public void run(){

        LOGGER.info("***thread was run for " + fileMessage);

        WebSocket receiver = socketMap.get(fileMessage.idReceiver);
        if(receiver == null){
            Confirmation confirmation = new Confirmation(false);
            sender.send(JsonUtils.transformObjectInJson(confirmation));
        }

        String jsonFileMessage =  JsonUtils.transformObjectInJson(fileMessage);
        receiver.send(jsonFileMessage);

    }
}
