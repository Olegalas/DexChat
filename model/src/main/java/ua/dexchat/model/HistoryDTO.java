package ua.dexchat.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexter on 22.05.16.
 */
public class HistoryDTO {

    private List<MessageDTO> messages = new ArrayList<>();
    private HistoryType type;

    public HistoryDTO(List<MessageDTO> messages, HistoryType type) {
        this.messages = messages;
        this.type = type;
    }

    public enum HistoryType {
        EMPTY,
        FULL
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }

    public HistoryType getType() {
        return type;
    }

    public void setType(HistoryType type) {
        this.type = type;
    }
}
