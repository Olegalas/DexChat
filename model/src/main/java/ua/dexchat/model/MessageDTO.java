package ua.dexchat.model;

import java.util.Date;

/**
 * Created by dexter on 22.05.16.
 */
public class MessageDTO {

    private String message;
    private int idSender;
    private int idReceiver;
    private long date;
    private String loginFriend;

    public MessageDTO(String message, int idSender, int idReceiver, Date date) {
        this.message = message;
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.date = date.getTime();
    }

    public MessageDTO(Message message){
        this.message = message.getMessage();
        this.idSender = message.getIdSender();
        this.idReceiver = message.getIdReceiver();
        this.date = message.getDate().getTime();
    }

    public MessageDTO(Message message, String loginFriend){
        this.message = message.getMessage();
        this.idSender = message.getIdSender();
        this.idReceiver = message.getIdReceiver();
        this.date = message.getDate().getTime();
        this.loginFriend = loginFriend;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIdSender() {
        return idSender;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }

    public int getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(int idReceiver) {
        this.idReceiver = idReceiver;
    }

    public long getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date.getTime();
    }

    public String getLoginFriend() {
        return loginFriend;
    }

    public void setLoginFriend(String loginFriend) {
        this.loginFriend = loginFriend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageDTO that = (MessageDTO) o;

        if (idSender != that.idSender) return false;
        if (idReceiver != that.idReceiver) return false;
        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + idSender;
        result = 31 * result + idReceiver;
        return result;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "message='" + message + '\'' +
                ", idSender=" + idSender +
                ", idReceiver=" + idReceiver +
                '}';
    }
}
