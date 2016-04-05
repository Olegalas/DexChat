package ua.dexchat.server.stream;

import org.apache.log4j.Logger;
import ua.dexchat.model.Login;
import ua.dexchat.server.json.JsonClientUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by dexter on 05.04.16.
 */
public class StreamUtils {

    private static final Logger LOGGER = Logger.getLogger(StreamUtils.class);

    public static void sendString(String message, Socket clientSocket){
        try {
            PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
            pw.print(message + '\n');
            pw.flush();
        } catch (IOException e) {
            LOGGER.error("***ClientLogin cant send message to client "+ e.getMessage());
        }
    }

    public static void sendLogin(Login login, Socket clientSocket){
        String json = JsonClientUtil.transformLoginInJson(login);
        sendString(json, clientSocket);
    }

    public static String readMessage(Socket clientSocket) throws InterruptedException {
        String message = "";
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            message = bf.readLine();
        } catch (IOException e) {
            LOGGER.error("***ClientLogin cant take message to client "+ e.getMessage());
        }
        if(message == null){
            throw new InterruptedException("Client closed the socket");
        }

        return message;
    }

    public static Login readJsonMessage(Socket clientSocket) throws InterruptedException {
        String jsonLogin = readMessage(clientSocket);
        LOGGER.info("***Gut json : " + jsonLogin + " from client");
        return JsonClientUtil.parseLoginJson(jsonLogin);
    }
}
