import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import ua.dexchat.model.ClientInfoSocket;
import ua.dexchat.model.Login;
import ua.dexchat.server.ClientLogin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by dexter on 02.04.16.
 */
public class TestClientLogin {

    private final static Logger LOGGER = Logger.getLogger(TestClientLogin.class);

    @After
    public void tearDown() throws ClassNotFoundException {
        DropTables.dropTables();
    }

    @Test
    public void testRegistration(){

        new TestServerForClientLogin().start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            LOGGER.info("sleep to wait for run server");
        }

        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 8080);
        } catch (IOException e) {
            LOGGER.error("IOException from Socket in connection " + e.getMessage());
        }

        LOGGER.info("read Welcome message start");
        String welcomeMessage = getMessage(socket);
        Assert.assertEquals("Welcome", welcomeMessage);
        LOGGER.info("read Welcome message end");

        LOGGER.info("send newLogin start");
        Login login = new Login("name", "pass", "login");
        sendLogin(socket, login);
        LOGGER.info("send newLogin end");

        LOGGER.info("read return message after newLogin start");
        String registrationMessage = getMessage(socket);
        Assert.assertEquals("client was saved", registrationMessage);
        LOGGER.info("read return message after newLogin end");

        LOGGER.info("send double oldLogin start");
        Login nextLogin = new Login("name", "pass", "login");
        sendLogin(socket, nextLogin);
        LOGGER.info("send double oldLogin end");

        LOGGER.info("read return message after oldLogin start");
        String secondRegistrationMessage = getMessage(socket);
        Assert.assertEquals("client was saved", registrationMessage);
        LOGGER.info("read return message after oldLogin end");

        Assert.assertEquals("client was not saved", secondRegistrationMessage);

    }

    private String getMessage(Socket socket){

        String message= "";

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            message = br.readLine();
        } catch (IOException e) {
            LOGGER.error("IOException from BufferedReader " + e.getMessage());
        }
        return message;
    }

    private void sendLogin(Socket socket, Login login){

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(login);
            oos.flush();
        } catch (IOException e) {
            LOGGER.error("IOException from ObjectOutputStream " + e.getMessage());
        }
    }

    private static class TestServerForClientLogin extends Thread{

        private final static Logger SERVER_LOGGER = Logger.getLogger(TestServerForClientLogin.class);
        private ServerSocket server;

        @Override
        public void run(){
            SERVER_LOGGER.info("test server was run");
            Socket testSocket = null;
            ClientInfoSocket info = null;

            try {
                server = new ServerSocket(8080);
                testSocket = server.accept();
                info = new ClientInfoSocket();
                info.ip = testSocket.getInetAddress().getCanonicalHostName();
                info.port = testSocket.getPort();

            } catch (IOException e) {
                SERVER_LOGGER.error("IOException from SererSocket " + e.getMessage());
            }

            new ClientLogin(testSocket, info).start();
            SERVER_LOGGER.info("killed test server");
        }
    }
}
