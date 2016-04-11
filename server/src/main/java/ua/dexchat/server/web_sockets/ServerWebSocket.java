package ua.dexchat.server.web_sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import ua.dexchat.model.WebSocketMessage;
import ua.dexchat.server.json.JsonUtils;


/**
 * Created by dexter on 11.04.16.
 */
public class ServerWebSocket extends WebSocketServer {

    private static final Logger LOGGER = Logger.getLogger(ServerWebSocket.class);

    private Map<String, WebSocket> mySockets = new HashMap<>();

    public ServerWebSocket( int port ) throws UnknownHostException {
        super( new InetSocketAddress( port ) );
    }

    public ServerWebSocket( InetSocketAddress address ) {
        super( address );
    }

    @Override
    public void onOpen( WebSocket conn, ClientHandshake handshake ) {
        WebSocketMessage message = new WebSocketMessage("connection complete", WebSocketMessage.MessageType.TEXT);
        LOGGER.info("***" + conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!" );
        conn.send(JsonUtils.transformObjectInJson(message));
    }

    @Override
    public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
//        this.sendToAll( conn + " has left the room!" );
//        System.out.println( conn + " has left the room!" );
        // TODO: 11.04.16
    }

    @Override
    public void onMessage( WebSocket conn, String message ) {
//        System.out.println(conn.getDraft().createFrames(message, true).get(0).getOpcode());
//        System.out.println( conn + ": " + message );
        // TODO: 11.04.16
    }

    @Override
    public void onMessage( WebSocket conn, ByteBuffer message ) {
//        System.out.println( conn + ": " + message );
        // TODO: 11.04.16
    }

    public void onFragment( WebSocket conn, Framedata fragment ) {
//        System.out.println( "received fragment: " + fragment );
        // TODO: 11.04.16
    }

    @Override
    public void onError( WebSocket conn, Exception ex ) {
//        ex.printStackTrace();
//        if( conn != null ) {
//            // some errors like port binding failed may not be assignable to a specific websocket
//        }
        // TODO: 11.04.16  
    }

    /**
     * Sends <var>text</var> to all currently connected WebSocket clients.
     *
     * @param text
     *            The String to send across the network.
     * @throws InterruptedException
     *             When socket related I/O errors occur.
     */
    public void sendToAll( String text ) {
//        Collection<WebSocket> con = connections();
//        synchronized ( con ) {
//            for( WebSocket c : con ) {
//                c.send( text );
//            }
//        }
        // TODO: 11.04.16  
    }
}
