import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by dexter on 04.04.16.
 */
public class DropTables {
    private final static Logger LOGGER = Logger.getLogger(DropTables.class);
    public static void dropTables() throws ClassNotFoundException {
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
}
