package ua.dexchat.server;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import ua.dexchat.model.FileMessage;
import ua.dexchat.server.utils.JsonUtils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by dexter on 14.04.16.
 */
public class ConfirmFileResendThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(ConfirmFileResendThread.class);

    private final WebSocket sender;
    private final FileMessage fileMessage;
    private final Map<Integer, WebSocket> socketMap;
    private final Map<Integer, AtomicBoolean> confirms;
    private final Map<String, WebSocket> socketsWaiting;

    public ConfirmFileResendThread(WebSocket sender, FileMessage fileMessage,
                            Map<Integer, WebSocket> sockets,
                            Map<Integer, AtomicBoolean> confirms, Map<String , WebSocket> socketsWaiting){
        this.sender = sender;
        this.fileMessage = fileMessage;
        this.socketMap = sockets;
        this.confirms = confirms;
        this.socketsWaiting = socketsWaiting;
    }

    @Override
    public void run(){

        LOGGER.info("***thread was run for " + fileMessage);

        WebSocket receiver = socketMap.get(fileMessage.idReceiver);
        if(receiver == null){
            // notify sender that receiver is not online
        }
        AtomicBoolean confirmFromClient = new AtomicBoolean(false);
        confirms.put(fileMessage.idReceiver, confirmFromClient);

        String jsonFileMessage =  JsonUtils.transformObjectInJson(fileMessage);
        receiver.send(jsonFileMessage);

        while(!confirmFromClient.get()){
            // notify sender that receiver want to receive the file
            socketsWaiting.put(sender.getRemoteSocketAddress().toString(), receiver);
        }
    }
}
