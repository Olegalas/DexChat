package ua.dexchat.server.utils;

import org.apache.log4j.Logger;
import ua.dexchat.model.Login;

import java.io.*;
import java.net.Socket;

/**
 * Created by dexter on 05.04.16.
 */
public class StreamUtils {

    private static final Logger LOGGER = Logger.getLogger(StreamUtils.class);
    private static final int DEFAULT_SIZE = 8000;

    public static void sendString(String message, Socket clientSocket){
        try {
            PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
            pw.print(message + '\n');
            pw.flush();
        } catch (IOException e) {
            LOGGER.error("***ClientLogin cant send message to client "+ e.getMessage());
        }
    }

    public static void sendObject(Object object, Socket clientSocket){
        String json = JsonUtils.transformObjectInJson(object);
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
        return JsonUtils.parseLoginJson(jsonLogin);
    }

    public static void reSendFile(Socket client, Socket friend){

        try(InputStream is = friend.getInputStream()) {

            OutputStream os = client.getOutputStream();

            byte[] buffer = new byte[DEFAULT_SIZE];
            int isRead = 0;

            while((isRead = is.read(buffer)) != -1){
                os.write(buffer, 0, isRead);
                buffer = new byte[DEFAULT_SIZE];
            }
            LOGGER.info("***File was send (before flush)");
            os.flush();
            os.close();
        } catch (IOException e) {
            LOGGER.info("***IOException in reSendFile Method");
            LOGGER.info("***" + e.getMessage());
            return;
        }

        LOGGER.info("***File was correctly send");
    }
}
