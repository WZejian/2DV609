package plantpal.model.SQLqueries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is a static reference to establish a DB connection.
 * Connection can be modified from within the package, for example
 * when changing users.
 * More methods can be added to allow further manipulation.
 * True status signifies successfull connection.
 */
public class SQLConnector {

    public static Connection conn;
    static boolean status = false;
    static String error_msg = "";

    private static void setStatus(boolean status) {
        SQLConnector.status = status;
    }

    public static boolean getStatus() {
        return SQLConnector.status;
    }

    public static void setError(String error) {
        SQLConnector.error_msg = error;
    }

    public static String getError() {
        return SQLConnector.error_msg;
    }

    /**
     * A static method to connect to the DB.
     */
    public static void connect() {
        String connection = "jdbc:mysql://ya.nsong.net:3306/plantpal?user=remoteuser&password=Password1!";
        try {
            SQLConnector.conn = DriverManager.getConnection(connection);
            setStatus(true);

        } catch (SQLException e) {
            setError(e.toString());
            setStatus(false);
        }
    }

    /**
     * Run select query.
     */
    public static ResultSet runQuery(String query, Object... args) {
        try {
            if (conn.isClosed()) {
                SQLConnector.connect();
            }
            PreparedStatement stmt = conn.prepareStatement(query);
            int column = 1;
            for (Object arg : args) {
                stmt.setObject(column, arg);
                column++;
            }
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            System.out.println("..........." + e.toString());
            SQLConnector.setError(e.toString());
            return null;
        }
    }

    /**
     * Run update query.
     */
    public static void runUpdateQuery(String query, Object... args) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(query);
        int column = 1;
        for (Object arg : args) {
            stmt.setObject(column, arg);
            column++;
        }
        stmt.executeUpdate();
    }

    
}
