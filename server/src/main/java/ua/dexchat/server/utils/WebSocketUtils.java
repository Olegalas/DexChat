package ua.dexchat.server.utils;

import org.java_websocket.WebSocket;
import ua.dexchat.model.*;

import java.util.List;

/**
 * Created by dexter on 12.04.16.
 */
public class WebSocketUtils {

    public static void sendTextMessageToClient(String text, WebSocket clientSocket){
        WebSocketMessage message = new WebSocketMessage(text, WebSocketMessage.MessageType.TEXT);
        String jsonMessage = JsonUtils.transformObjectInJson(message);
        clientSocket.send(jsonMessage);
    }

    public static void sendMessageToClient(Message message, WebSocket clientSocket){

        String messageJson = JsonUtils.transformObjectInJson(message);

        WebSocketMessage webMessage = new WebSocketMessage(messageJson, WebSocketMessage.MessageType.MESSAGE);
        String jsonWebMessage = JsonUtils.transformObjectInJson(webMessage);
        clientSocket.send(jsonWebMessage);
    }

    public static void sendHistoryToClient(Client client, WebSocket clientSocket){
        List<History> histories = client.getHistory();
        String historiesJson = JsonUtils.transformObjectInJson(histories);
        WebSocketMessage historyPackage = new WebSocketMessage(historiesJson, WebSocketMessage.MessageType.HISTORY);
        String historyPackageJson = JsonUtils.transformObjectInJson(historyPackage);
        clientSocket.send(historyPackageJson);
    }

    public static void sendConfirmationToClient(Confirmation confirmation, WebSocket webSocket){
        String confirmationJson = JsonUtils.transformObjectInJson(confirmation);
        WebSocketMessage webSocketMessage = new WebSocketMessage(confirmationJson, WebSocketMessage.MessageType.CONFIRMATION);
        String webSocketMessageJson = JsonUtils.transformObjectInJson(webSocketMessage);
        webSocket.send(webSocketMessageJson);
    }

    public static void sendFileMessageToClient(FileMessage fileMessage, WebSocket webSocket){
        String fileMessageJson = JsonUtils.transformObjectInJson(fileMessage);
        WebSocketMessage webSocketMessage = new WebSocketMessage(fileMessageJson, WebSocketMessage.MessageType.CONFIRMATION);
        String webSocketMessageJson = JsonUtils.transformObjectInJson(webSocketMessage);
        webSocket.send(webSocketMessageJson);
    }

    public static void sendFriendsMessageToClient(ClientDTO friends, WebSocket webSocket){
        String friendsJson = JsonUtils.transformObjectInJson(friends);
        WebSocketMessage webSocketMessage = new WebSocketMessage(friendsJson, WebSocketMessage.MessageType.FRIEND);
        String webSocketMessageJson = JsonUtils.transformObjectInJson(webSocketMessage);
        webSocket.send(webSocketMessageJson);
    }

}
