package ua.dexchat.server.utils;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import ua.dexchat.model.Client;
import ua.dexchat.server.service.ClientService;
import ua.dexchat.server.service.GetSpringContext;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by dexter on 09.05.16.
 */
public class EmailUtils {

    private static final Logger LOGGER = Logger.getLogger(EmailUtils.class);

    public static final String DEX_CHAT_USER_NAME = "dr.dexter.ua@gmail.com";
    private static final String PASS = "11235813lateralusemailpass";
    public static final String HOST = "smtp.gmail.com";

    public static void sendMessage(String login, String email, WebSocket clientSocket, String pass){

        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.user", DEX_CHAT_USER_NAME);
        props.put("mail.smtp.password", PASS);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");

        Session getMailSession = Session.getDefaultInstance(props, new GMailAuthenticator());


        MimeMessage generateMailMessage = new MimeMessage(getMailSession);

        try{

            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(DEX_CHAT_USER_NAME));
            generateMailMessage.setSubject("Recover pass for " + login);


            String emailBody = "your pass : " + pass;

            generateMailMessage.setText(emailBody);

            Transport transport = getMailSession.getTransport("smtp");

            transport.connect(HOST, DEX_CHAT_USER_NAME, PASS);
            generateMailMessage.saveChanges();
            transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
            transport.close();


            WebSocketUtils.sendTextMessageToClient("Password on your email was sent", clientSocket);
            LOGGER.info("***Pass on Client email was sent");

        }catch (Exception e) {
            LOGGER.error("Something wrong during sendMessage in EmailUtils : ", e);
        }
    }

    static class GMailAuthenticator extends Authenticator {
        GMailAuthenticator (){
            super();
        }
        public PasswordAuthentication getPasswordAuthentication(){
            return new PasswordAuthentication(DEX_CHAT_USER_NAME, PASS);
        }
    }
}
