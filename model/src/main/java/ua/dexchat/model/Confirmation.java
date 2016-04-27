package ua.dexchat.model;

/**
 * Created by dexter on 14.04.16.
 */
public class Confirmation {

    public boolean confirm;
    public int idSender;

    public Confirmation(boolean confirm, int idSender) {
        this.confirm = confirm;
        this.idSender = idSender;
    }

    public Confirmation(boolean confirm) {
        this.confirm = confirm;
    }

    @Override
    public String toString() {
        return "Confirmation{" +
                "confirm=" + confirm +
                ", idSender=" + idSender +
                '}';
    }
}
