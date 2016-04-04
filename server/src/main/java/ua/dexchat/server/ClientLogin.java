package ua.dexchat.server;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.dexchat.model.Client;
import ua.dexchat.model.ClientInfoSocket;
import ua.dexchat.model.Login;
import ua.dexchat.server.dao.ClientDao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by dexter on 02.04.16.
 */
public class ClientLogin extends Thread {

    private final Socket clientSocket;
    private final static Logger LOGGER = Logger.getLogger(Server.class);
    private final ClientInfoSocket clientInfo;

    public ClientLogin(Socket client, ClientInfoSocket info){
        this.clientSocket = client;
        clientInfo = info;
    }

    @Override
    public void run() {

        ApplicationContext context =
                new ClassPathXmlApplicationContext("/spring-context.xml");

        ClientDao clientDao = context.getBean(ClientDao.class);


        LOGGER.info("***ClientLogin " + clientInfo + " thread was run");
        sendToClient("Welcome");

        while (!isInterrupted()){

            try {
                LOGGER.info("***" + clientInfo +" - Wait for Login object from client");

                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                Login loginFromClient = (Login) ois.readObject();

                LOGGER.info("***" + clientInfo +" - Gut " + loginFromClient + " from client");

                if(loginFromClient == null || loginFromClient.login.isEmpty() || loginFromClient.pass.isEmpty()){
                    continue;
                }

                if(loginFromClient.name.isEmpty()){ // login client
                    Client client = clientDao.findClient(loginFromClient);
                    if(client != null){
                        LOGGER.error("*** client passed");
                        sendToClient("login passed");
                        new Conversation(clientSocket, clientInfo, client).run();
                        interrupt();
                        break;
                    }
                } else { // registration client
                    try{
                        if(clientDao.saveClient(loginFromClient) != -1){
                            LOGGER.error("*** client was saved");
                            sendToClient("client was saved");
                        } else {
                            LOGGER.error("*** client was not saved");
                            sendToClient("client was not saved");
                        }
                    }catch (Exception ignore){
                        sendToClient("client was not saved");
                        LOGGER.error("*** client was not saved");
                    }

                }


            } catch (IOException e) {
                LOGGER.error("***IOException from input stream in "+ clientInfo +" - " + e.getMessage());
                break;
            } catch (ClassNotFoundException e){
                LOGGER.error("***ClassNotFoundException from input stream in "+ clientInfo +" - " + e.getMessage());
            }
        }

        LOGGER.info("***ClientLogin " + clientInfo + " thread was correctly killed");
    }

    private void sendToClient(String message){
        try {
            PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
            pw.print(message + '\n');
            pw.flush();
        } catch (IOException e) {
            LOGGER.error("***ClientLogin cant send message to client "+ e.getMessage());
        }
    }
}
