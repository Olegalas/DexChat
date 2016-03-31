import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Created by dexter on 31.03.16.
 */
public class Conversation extends Thread {

    private final static Logger LOGGER = Logger.getLogger(Server.class);

    private ServerSocket serverSocketForConversation;

    private Socket clientSocketForFiles;
    private Socket clientSocketForSearchFriends;

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

        LOGGER.info(infoSocket +" - search for old friends");
        List<Client> friends = client.getiFriendTo();
        LOGGER.info(infoSocket +" - " + friends.size() + " friends was found");

        if(friends.size() != 0){
            sendFriends(friends);
        }

        new CatchFile().run();
        new SearchNewFriend().run();

        while (!isInterrupted()){

            try {
                LOGGER.debug(infoSocket +" - Wait for choose friend to talk");

                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                Client friend = (Client) ois.readObject();

                LOGGER.debug(infoSocket +" - Gut " + friend + " from client");

                /* there is must be run new thread to chat with friend */

            } catch (IOException e) {
                LOGGER.error("IOException from input stream in "+ infoSocket +" - " + e.getMessage());
                break;
            } catch (ClassNotFoundException e){
                LOGGER.fatal("ClassCastException "+ infoSocket+" Client send something wrong");
                break;
            }

        }
    }

    private void sendFriends(List<Client> friends) {
        try {

            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(friends);
            oos.flush();


        } catch (IOException e) {
            LOGGER.error(infoSocket + " - OutPutStreamException " + e.getMessage());
        }
    }

    private void checkExit(String exitCommand) throws InterruptedException {
        if("@exit".equals(exitCommand) || exitCommand == null){
            throw new InterruptedException("user want exit from registration");
        }
    }

    private class CatchFile extends Thread{

        @Override
        public void run(){
            try {
                LOGGER.debug(infoSocket +" - CatchFile thread was run");
                clientSocketForFiles  = serverSocketForConversation.accept();
                InputStream is = clientSocketForFiles.getInputStream();
                OutputStream os = clientSocketForFiles.getOutputStream();

                byte[] buff = new byte[8000];
                int count = 0;

                while (!interrupted()){


                    while((count = is.read(buff)) != -1){
                        os.write(buff, 0, count);
                        os.flush();
                        buff = new byte[8000];
                        count = 0;
                    }


                }

            } catch (IOException e) {
                LOGGER.error(infoSocket + " IOException in CatchFile thread " + e.getMessage());
                interrupt();
            }
        }
    }


    private class SearchNewFriend extends Thread{

        @Override
        public void run(){

        }
    }

    private void initSockets(){
        int currentConversationPort = 8080 + client.getId();
        try {
            serverSocketForConversation = new ServerSocket(currentConversationPort);
            // for accept client-side .. for send files from friends to client
            clientSocketForFiles = serverSocketForConversation.accept();
            clientSocketForSearchFriends = serverSocketForConversation.accept();
        } catch (IOException e) {
            LOGGER.error(infoSocket + " IOException from initial ServerSocket " + e.getMessage());
        }
    }
}
