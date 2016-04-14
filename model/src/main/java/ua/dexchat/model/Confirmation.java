package ua.dexchat.model;

/**
 * Created by dexter on 14.04.16.
 */
public class Confirmation {

    public boolean confirm;
    public int idSender;
    public int idReceiver;

    public Confirmation(boolean confirm, int idSender, int idReceiver) {
        this.confirm = confirm;
        this.idSender = idSender;
        this.idReceiver = idReceiver;
    }

    @Override
    public String toString() {
        return "Confirmation{" +
                "confirm=" + confirm +
                ", idSender=" + idSender +
                ", idReceiver=" + idReceiver +
                '}';
    }
}
