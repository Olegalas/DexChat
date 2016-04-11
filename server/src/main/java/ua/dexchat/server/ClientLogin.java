package ua.dexchat.server;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.dexchat.model.Client;
import ua.dexchat.model.ClientInfoSocket;
import ua.dexchat.model.Login;
import ua.dexchat.server.dao.ClientDao;
import ua.dexchat.server.service.ClientService;
import ua.dexchat.server.service.GetSpringContext;
import ua.dexchat.server.stream.StreamUtils;

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

        ApplicationContext context = GetSpringContext.getContext();
        ClientService service = context.getBean(ClientService.class);


        LOGGER.info("***ClientLogin " + clientInfo + " thread was run");
        StreamUtils.sendString("Welcome", clientSocket);

        while (!isInterrupted()){

            try {
                LOGGER.info("***" + clientInfo +" - Wait for Login object from client");

                Login loginFromClient = StreamUtils.readJsonMessage(clientSocket);

                LOGGER.info("***" + clientInfo +" - parse json in : " + loginFromClient + " from client");

                if(loginFromClient == null || loginFromClient.login.isEmpty() || loginFromClient.pass.isEmpty()){
                    continue;
                }

                if(loginFromClient.name.isEmpty()){ // login client
                    Client client = service.findClient(loginFromClient);
                    if(client != null){
                        LOGGER.error("*** client passed");
                        StreamUtils.sendString("login passed", clientSocket);
                        new Conversation(clientSocket, clientInfo, client, service).start();
                        interrupt();
                        break;
                    }
                } else { // registration client
                    try{
                        if(service.saveClient(loginFromClient) != -1){
                            LOGGER.error("*** client was saved");
                            StreamUtils.sendString("client was saved", clientSocket);
                        } else {
                            LOGGER.error("*** client was not saved");
                            StreamUtils.sendString("client was not saved", clientSocket);
                        }
                    }catch (Exception ignore){
                        StreamUtils.sendString("client was not saved", clientSocket);
                        LOGGER.error("*** client was not saved");
                    }

                }


            } catch (InterruptedException e) {
                LOGGER.error("***"+ clientInfo +" - " + e.getMessage());
                break;
            }
        }

        LOGGER.info("***ClientLogin " + clientInfo + " thread was correctly killed");
    }
}
