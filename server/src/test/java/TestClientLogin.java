import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by dexter on 02.04.16.
 */
public class TestClientLogin {

    private final static Logger LOGGER = Logger.getLogger(TestClientLogin.class);

    @After
    public void tearDown() throws ClassNotFoundException {

        Class.forName("com.mysql.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dexchat", "root", "root")) {

            Statement statement = connection.createStatement();
            statement.execute("drop table hibernate_sequence");
            statement.execute("drop table clients_clients");
            statement.execute("drop table messages");
            statement.execute("drop table Message_buffer");
            statement.execute("drop table clients");
            LOGGER.info("********DataBase was removed");

        } catch (SQLException e) {
            LOGGER.error("Error connection");
        }

    }

    @Test
    public void testRegistration(){

        new TestServerForClientLogin().start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            LOGGER.debug("sleep to wait for run server");
        }

        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 8080);
        } catch (IOException e) {
            LOGGER.error("IOException from Socket in connection " + e.getMessage());
        }

        LOGGER.debug("read Welcome message start");
        String welcomeMessage = getMessage(socket);
        Assert.assertEquals("Welcome", welcomeMessage);
        LOGGER.debug("read Welcome message end");

        LOGGER.debug("send Login start");
        Login login = new Login("name", "pass", "login");
        sendLogin(socket, login);
        LOGGER.debug("send Login end");

        LOGGER.debug("read return message start");
        String registrationMessage = getMessage(socket);
        Assert.assertEquals("client was saved", registrationMessage);
        LOGGER.debug("read return message end");

        LOGGER.debug("send double Login start");
        Login nextLogin = new Login("name", "pass", "login");
        sendLogin(socket, nextLogin);
        LOGGER.debug("send double Login start");

        LOGGER.debug("read return message start");
        String secondRegistrationMessage = getMessage(socket);
        Assert.assertEquals("client was saved", registrationMessage);
        LOGGER.debug("read return message end");

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
            SERVER_LOGGER.debug("test server was run");
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
            SERVER_LOGGER.debug("killed test server");
        }

    }

}
