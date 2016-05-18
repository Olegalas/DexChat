package ua.dexchat.model;

/**
 * Created by dexter on 11.04.16.
 */
public class WebSocketMessage {

    private Object message;
    private MessageType type;

    public WebSocketMessage(Object message, MessageType type) {
        this.message = message;
        this.type = type;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
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
        HISTORY,
        TEXT,
        EXIT,
        LOGIN,
        REGISTRATION,
        CONFIRMATION,
        EMAIL,
        ID

    }
}
