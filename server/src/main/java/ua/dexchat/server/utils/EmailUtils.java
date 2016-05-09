package ua.dexchat.server.utils;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import ua.dexchat.model.Client;
import ua.dexchat.server.service.ClientService;
import ua.dexchat.server.service.GetSpringContext;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by dexter on 09.05.16.
 */
public class EmailUtils {

    private static final Logger LOGGER = Logger.getLogger(EmailUtils.class);

    public static final String DEXCHAT_EMAIL = "dexchat@dexchat.com";
    public static final String HOST = "localhost";

    public static void sendMessage(String login, String email, WebSocket clientSocket){

        ClientService service = GetSpringContext.getContext().getBean(ClientService.class);
        Client client = service.findByLogin(login);

        if(client == null || !client.getEmail().equals(email)){
            WebSocketUtils.sendTextMessageToClient("Incorrect Email or Login", clientSocket);
        }

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", HOST);

        Session session = Session.getDefaultInstance(properties);

        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(DEXCHAT_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Your password to DexChat");

            message.setText("Hi... take your password and don't forget it anymore ;) \n pass : " + client.getPass());

            // Send message
            Transport.send(message);

            LOGGER.info("***Pass on Client email was sent");

        }catch (MessagingException mex) {
            LOGGER.error("Something wrong during sendMessage in EmailUtils : ", mex);
        }
    }
}
