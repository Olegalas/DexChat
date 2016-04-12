package ua.dexchat.server.service;

import org.java_websocket.WebSocket;
import ua.dexchat.model.WebSocketMessage;
import ua.dexchat.server.json.JsonUtils;

/**
 * Created by dexter on 12.04.16.
 */
public class WebSocketUtils {

    public static void sendTextMessageToClient(String text, WebSocket clientSocket){
        WebSocketMessage message = new WebSocketMessage("Incorrect pass or login", WebSocketMessage.MessageType.TEXT);
        String jsonMessage = JsonUtils.transformObjectInJson(message);
        clientSocket.send(jsonMessage);
    }
}
