package ua.dexchat.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexter on 01.04.16.
 */
@Entity
@Table(name ="Message_buffer")
public class History extends IdGenerate {

    @OneToMany(mappedBy = "buffer", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval=true)
    private List<Message> messages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="client_id", referencedColumnName = "id")
    private Client idOwner;

    @Column(nullable = false, unique = true)
    private int idSender;

    public History() {
    }

    public Client getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(Client idOwner) {
        this.idOwner = idOwner;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public int getIdSender() {
        return idSender;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }

    @Override
    public String toString() {
        return "ua.dexchat.model.History{" +
                "messages=" + messages +
                ", idOwner=" + idOwner +
                ", idSender=" + idSender +
                '}';
    }
}
