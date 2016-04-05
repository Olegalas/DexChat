package ua.dexchat.model;

import java.net.Socket;

/**
 * Created by dexter on 28.03.16.
 */
public class ClientInfoSocket {

    public String ip;
    public int port;

    public ClientInfoSocket(Socket socket){
        this.ip = socket.getInetAddress().getCanonicalHostName();
        this.port = socket.getPort();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientInfoSocket that = (ClientInfoSocket) o;

        return ip != null ? ip.equals(that.ip) : that.ip == null;
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + port;
        return result;
    }

    @Override
    public String toString() {
        return ip + ":" + port;
    }
}
