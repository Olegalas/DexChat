package ua.dexchat.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

/**
 * Created by dexter on 01.04.16.
 */
@Entity
@Table(name = "messages")
public class Message extends IdGenerate{

    private String message;
    private int idSender;
    private int idReceiver;

    @ManyToOne()
    @JoinColumn(name = "buffer_id",
            referencedColumnName = "id")
    private History buffer;

    @ManyToOne()
    @JoinColumn(name = "temp_buffer_id",
            referencedColumnName = "id")
    private TemporaryBuffer tempBuffer;

    @Temporal(TemporalType.TIME)
    private Date date;

    public Message() {
    }

    public Message(String message, int idSender, int idReceiver, Date date) {
        this.message = message;
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.date = date;
    }

    public Message(String message, String idSender, String idReceiver, Double date) {
        this.message = message;
        this.idSender = Integer.parseInt(idSender);
        this.idReceiver = Integer.parseInt(idReceiver);
        this.date = new Date(date.intValue()) ;
    }

    public Message(String message, int idSender, int idReceiver, Date date, History buffer) {
        this.message = message;
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.buffer = buffer;
        this.date = date;
    }

    public Message(Message message){
        this.message = message.message;
        this.idSender = message.idSender;
        this.idReceiver = message.idReceiver;
        this.date = message.date;
    }

    public String getMessage() {
        return message;
    }

    public int getIdSender() {
        return idSender;
    }

    public int getIdReceiver() {
        return idReceiver;
    }

    public Date getDate() {
        return date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }

    public void setIdReceiver(int idReceiver) {
        this.idReceiver = idReceiver;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public History getHistory() {
        return buffer;
    }

    public TemporaryBuffer getTempBuffer() {
        return tempBuffer;
    }

    public void setTempBuffer(TemporaryBuffer tempBuffer) {
        this.tempBuffer = tempBuffer;
    }

    public void setHistory(History buffer) {
        this.buffer = buffer;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", idSender=" + idSender +
                ", idReceiver=" + idReceiver +
                ", date=" + date +
                '}';
    }


}
