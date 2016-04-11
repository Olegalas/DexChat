package ua.dexchat.model;

/**
 * Created by dexter on 11.04.16.
 */
public class WebSocketMessage {

    private String message;
    private MessageType type;

    public WebSocketMessage(String message, MessageType type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public enum MessageType {

        MESSAGE,
        FILE,
        FRIEND,
        TEXT,
        EXIT,
        LOGIN,
        REGISTRASTION;

    }
}
