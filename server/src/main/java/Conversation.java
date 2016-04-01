import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
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

    private EntityManager manager;

    Conversation(Socket clientSocket, ClientInfoSocket infoSocket, Client client){
        this.client = client;
        this.clientSocket = clientSocket;
        this.infoSocket = infoSocket;

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("dexunit");
        manager = entityManagerFactory.createEntityManager();

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

                startNewChar(friend);

            } catch (IOException e) {
                LOGGER.error("IOException from input stream in "+ infoSocket +" - " + e.getMessage());
                break;
            } catch (ClassNotFoundException e){
                LOGGER.fatal("ClassCastException "+ infoSocket+" Client send something wrong");
                break;
            }

        }
    }

    private void startNewChar(Client friend){

        try {
            Socket socketFroNewChat = serverSocketForConversation.accept();
            //new Chat(socketFroNewChat, friend).run();
        } catch (IOException e) {
            LOGGER.error("IOException from accept new socket in startNewChat in "+ infoSocket +" - " + e.getMessage());
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
            try {

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(clientSocketForFiles.getInputStream()));
                ObjectOutputStream oos = new ObjectOutputStream(
                        clientSocketForSearchFriends.getOutputStream());


                String friendLogin = "";

                while(!interrupted()){

                    friendLogin = br.readLine();

                    LOGGER.info(infoSocket + "SearchNewFriend - client sent " + friendLogin + " for search in database");
                    manager.getTransaction().begin();
                    Query query =
                            manager.createQuery("SELECT c FROM Client c WHERE c.login LIKE :login");

                    LOGGER.debug(infoSocket +"SearchNewFriend  - Query was created for validation");

                    List<Client> queryFriends =  query.setParameter("login", friendLogin + "%").getResultList();

                    manager.getTransaction().commit();
                    LOGGER.info(infoSocket +" SearchNewFriend founded "+queryFriends.size() +" fiends");
                    LOGGER.info(infoSocket + queryFriends.toString());

                    oos.writeObject(queryFriends);
                    oos.flush();
                }


            } catch (IOException e) {
                LOGGER.error(infoSocket + "IOException in SearchNewFriend " + e.getMessage());
            }
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
