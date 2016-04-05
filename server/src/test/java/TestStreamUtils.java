import org.apache.log4j.Logger;
import org.junit.Test;
import ua.dexchat.model.ClientInfoSocket;
import ua.dexchat.server.stream.StreamUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by dexter on 05.04.16.
 */
public class TestStreamUtils {
    
    private static final int DEFAULT_SIZE = 8000;
    private static final Logger LOGGER = Logger.getLogger(TestStreamUtils.class);

    @Test
    public void deleteFile(){
        File file = new File("done.txt");
        file.delete();
    }
    
    @Test
    public void testResendFile(){

        new TestServerForClientLogin().start();
        new SecondThread().start();

        try(FileOutputStream fos = new FileOutputStream("done.txt")) {

            Thread.sleep(5000);

            Socket socket = new Socket("127.0.0.1", 8080);
            LOGGER.info("***First connection completed");

            InputStream fis = socket.getInputStream();

            byte[] buff = new byte[DEFAULT_SIZE];
            int isRead = 0;

            while((isRead = fis.read(buff)) != -1){
                fos.write(buff, 0, isRead);
                buff = new byte[DEFAULT_SIZE];
            }
            LOGGER.info("***Save file completed (before flush)");
            fos.flush();

            LOGGER.info("***Save file was completed");
        } catch (InterruptedException ignore){
            LOGGER.info("***InterruptException");
        } catch (IOException ignore){
            LOGGER.info("***IOException");
        }
    }

    private class SecondThread extends Thread {

        @Override
        public void run(){
            LOGGER.info("***SecondThread was run");
            File file = new File("src/test/resorces/testFileToCheckSendFile.txt");

            try(FileInputStream fis = new FileInputStream(file)){
                Thread.sleep(10000);

                Socket socket = new Socket("127.0.0.1", 8080);
                LOGGER.info("***SecondThread connection completed");

                byte[] buff = new byte[DEFAULT_SIZE];
                int isRead = 0;
                
                OutputStream os = socket.getOutputStream();

                while((isRead = fis.read(buff)) != -1){
                    os.write(buff, 0, isRead);
                    buff = new byte[DEFAULT_SIZE];
                }

                os.flush();
                os.close();

                LOGGER.info("***File was send");
            } catch (InterruptedException ignore){
                LOGGER.info("***InterruptException");
            } catch (IOException ignore){
                LOGGER.info("***IOException");
            }
            LOGGER.info("***SecondThread was killed");
        }
    }

    private static class TestServerForClientLogin extends Thread{

        private final static Logger SERVER_LOGGER = Logger.getLogger(TestServerForClientLogin.class);
        private ServerSocket server;

        @Override
        public void run(){
            SERVER_LOGGER.info("test server was run");
            Socket testSocket1 = null;
            Socket testSocket2 = null;
            ClientInfoSocket info = null;

            try {
                server = new ServerSocket(8080);
                testSocket1 = server.accept();
                info = new ClientInfoSocket(testSocket1);
                SERVER_LOGGER.info("***First client was accept");

                testSocket2 = server.accept();
                info = new ClientInfoSocket(testSocket1);
                SERVER_LOGGER.info("***Second client was accept");

            } catch (IOException e) {
                SERVER_LOGGER.error("IOException from SererSocket " + e.getMessage());
            }

            StreamUtils.reSendFile(testSocket1, testSocket2);
            SERVER_LOGGER.info("killed test server");
        }
    }
}
