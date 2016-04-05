package ua.dexchat.server;

import org.apache.log4j.Logger;
import ua.dexchat.model.Client;
import ua.dexchat.model.ClientInfoSocket;
import ua.dexchat.server.stream.StreamUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Created by dexter on 31.03.16.
 */
public class Conversation extends Thread {

    private final static Logger LOGGER = Logger.getLogger(Server.class);

    // number of the own clients port is his id + 8080
    private ServerSocket clientFileServerSocket;

    private final Socket clientSocket;
    private final ClientInfoSocket infoSocket;
    private final Client client;

    Conversation(Socket clientSocket, ClientInfoSocket infoSocket, Client client){
        this.client = client;
        this.clientSocket = clientSocket;
        this.infoSocket = infoSocket;
    }

    @Override
    public void run() {
        LOGGER.info(infoSocket +" - conversation session was run");

        LOGGER.info(infoSocket +" - search old friends");
        List<Client> friends = client.getiFriendTo();
        LOGGER.info(infoSocket +" - " + friends.size() + " friends was found");

        if(friends.size() != 0){
            StreamUtils.sendObject(friends, clientSocket);
        }

        Thread getFiles = new GetFiles();
        getFiles.setDaemon(true);
        getFiles.start();

        Thread getMessages = new GetMessages();
        getMessages.setDaemon(true);
        getMessages.start();


        while (!isInterrupted()){

            LOGGER.debug(infoSocket +" - Wait for choose friend to talk");

            // TODO: 05.04.16 Yo
            LOGGER.debug(infoSocket +" - Gut " +  " NOP " + " from client");

        }
    }

    private class GetFiles extends Thread {

        @Override
        public void run(){
            int port = client.getId() + 8080;

            try {

                ClientInfoSocket infoClientFileSocket = null;
                Socket clientFileSocket = null;

                while(!interrupted()){

                    do{
                        clientFileServerSocket = new ServerSocket(port);
                        clientFileSocket = clientFileServerSocket.accept();
                        infoClientFileSocket = new ClientInfoSocket(clientFileSocket);
                    } while(!infoSocket.equals(infoClientFileSocket));

                    Socket fileSocketFromFriend = clientFileServerSocket.accept();
                    StreamUtils.reSendFile(clientFileSocket, fileSocketFromFriend);
                }

            } catch (IOException e) {
                LOGGER.error("***IOException in GetFile thread in " + infoSocket);
                LOGGER.error("*** : " + e.getMessage());
            }

        }
    }

    private class GetMessages extends Thread {

        @Override
        public void run(){



        }
    }
}
