import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by dexter on 28.03.16.
 */
public class ClientConnect extends Thread {

    private final Socket client;
    private final static Logger LOGGER = Logger.getLogger(Server.class);
    private final ClientInfoSocket clientInfo;

    ClientConnect(Socket client, ClientInfoSocket info){
        this.client = client;
        clientInfo = info;
    }

    @Override
    public void run() {

        LOGGER.debug("ClientConnect " + clientInfo + " thread was run");
        int choose = 0;

        while (isInterrupted()){

            try {
                InputStream is = client.getInputStream();
                choose = is.read();
            } catch (IOException e) {
                LOGGER.error("IOException from input stream in "+ clientInfo +" - " + e.getMessage());
            }

            switch (choose){

                case 1:
                    registration();
                    break;
                case 2:
                    if(validation()){
                        /* run new thread and interrupted this thread*/
                    }
                    break;

            }
        }

        LOGGER.debug("ClientConnect " + clientInfo + " thread was killed");
    }

    private boolean validation() {
        getLoginAndPassFromClient();

        return false;
    }
    private void registration() {
        getLoginAndPassFromClient();
    }

    private Client getLoginAndPassFromClient() {
        try {
            Login login = new Login();
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            if(br.ready()){
                login.name = br.readLine();
                login.pass = br.readLine();
            }

            return new Client(login);

        } catch (IOException e) {
            LOGGER.error("IOException from input stream in "+ clientInfo +" - " + e.getMessage());
            LOGGER.error("getLoginAndPassFromClient returned null");
            return null;
        }
    }


}
