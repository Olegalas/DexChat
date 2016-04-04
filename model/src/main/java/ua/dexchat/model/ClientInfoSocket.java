package ua.dexchat.model;

/**
 * Created by dexter on 28.03.16.
 */
public class ClientInfoSocket {
    public String ip;
    public int port;

    @Override
    public String toString() {
        return ip + ":" + port;
    }
}
