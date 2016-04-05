package ua.dexchat.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexter on 01.04.16.
 */
@Entity
@Table(name = "Temporary_buffers")
public class TemporaryBuffer extends IdGenerate{

    @OneToMany(mappedBy = "tempBuffer", cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private List<Message> messages = new ArrayList<>();

    private int idOwner;

    public TemporaryBuffer() {
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public int getIdOwnerr() {
        return idOwner;
    }

    public void setIdOwnerr(int idOwnerr) {
        this.idOwner = idOwnerr;
    }

    @Override
    public String toString() {
        return "TemporaryBuffer{" +
                "messages=" + messages +
                ", idOwner=" + idOwner +
                '}';
    }
}
